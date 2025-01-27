package com.example.lirarymagement_api.service;

import com.example.lirarymagement_api.data.constant.ACTIVITY;
import com.example.lirarymagement_api.data.constant.STATUS;
import com.example.lirarymagement_api.data.model.Book;
import com.example.lirarymagement_api.data.model.Log;
import com.example.lirarymagement_api.data.model.User;
import com.example.lirarymagement_api.data.repository.UserRepository;
import com.example.lirarymagement_api.dto.request.AddBookRequest;
import com.example.lirarymagement_api.dto.request.RegisterUserRequest;
import com.example.lirarymagement_api.dto.request.UpdateUserRequest;
import com.example.lirarymagement_api.dto.response.*;
import com.example.lirarymagement_api.exception.ExistingUserException;
import com.example.lirarymagement_api.exception.UserNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.lirarymagement_api.data.constant.ROLE.ADMIN;
import static com.example.lirarymagement_api.data.constant.ROLE.USER;
import static com.example.lirarymagement_api.data.constant.STATUS.BORROWED;

@Service
public class UserServiceImplementation implements UserService{
   private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final BookService bookService;
    private final PasswordEncoder passwordEncoder;
    private final LogService logService;

    public UserServiceImplementation(UserRepository userRepository,
                                     ModelMapper modelMapper,
                                     BookService bookService,
                                     PasswordEncoder passwordEncoder,
                                     LogService logService) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.bookService = bookService;
        this.passwordEncoder = passwordEncoder;
        this.logService = logService;
    }

    @Override
    public RegisterUserResponse register(RegisterUserRequest request){
        validateUserRequest(request);
        User newUser = modelMapper.map(request, User.class);
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        newUser.setRoles(new HashSet<>());
        newUser.getRoles().add(USER);
        newUser = userRepository.save(newUser);
        RegisterUserResponse response = modelMapper.map(newUser, RegisterUserResponse.class);
        response.setMessage("User registered successfully");
        return response;
    }

    @Override
    public AssignRoleResponse assignRole(Long userId) {
        User user = getUser(userId);
        user.getRoles().add(ADMIN);
        user = userRepository.save(user);
        return modelMapper.map(user, AssignRoleResponse.class);
    }

    private  User getUser(Long userId) {
            return userRepository.findById(userId).orElseThrow( ()-> new UserNotFoundException("User not found"));
    }

    @Override
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> modelMapper.map(user, UserResponse.class))
                .collect(Collectors.toList());
    }


    @Override
    public UpdateUserResponse updateUser(Long userId, JsonPatch patch, UpdateUserRequest request) {
        try {
            User user = getUser(userId);
            User updatedUser = applyPatchToUser(patch, user);
            modelMapper.map(request, updatedUser);
            user = userRepository.save(updatedUser);
            return modelMapper.map(user, UpdateUserResponse.class);
        } catch (JsonPatchException | JsonProcessingException e) {
            throw new RuntimeException("Failed to update user", e);
        }
    }

    private User applyPatchToUser(JsonPatch patch, User targetUser) throws JsonPatchException, JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode patched = patch.apply(objectMapper.convertValue(targetUser, JsonNode.class));
        return objectMapper.treeToValue(patched, User.class);
    }


    @Override
    public DeleteUserResponse deleteUser(Long userId) {
        User user = getUser(userId);
        userRepository.delete(user);
        return new DeleteUserResponse("Book deleted");
    }

    private void validateUserRequest(RegisterUserRequest request) {
        if(userRepository.existsByEmail(request.getEmail()))throw new ExistingUserException("Email taken");
    }

    @Override
    public BorrowBookResponse borrowBook(Long userId, Long bookId) {
        validateIdNotNull(userId,bookId);// validate the user id and book id to avoid null inputs
        Book foundBook = bookService.getBook(bookId);
        User user = getUser(userId);
        validateBookStatus(foundBook);//check the status of the book if  borrowed or not
        foundBook.setStatus(BORROWED);
        foundBook = bookService.persist(foundBook); //save the updated state of the book to the database
        LogResponse logResponse = logService.logActivity(user.getUserId(), foundBook.getBookId(), "Borrowed book", ACTIVITY.BORROWED);
        BorrowBookResponse borrowBookResponse = modelMapper.map(logResponse,BorrowBookResponse.class);
        borrowBookResponse.setLogId(logResponse.getId());
        return borrowBookResponse;
    }

    private void validateBookStatus(Book book) {
        if (book.getStatus() == BORROWED) {
            throw new RuntimeException("The book is already borrowed");
        }
    }

    private void validateIdNotNull(Long userId, Long bookId) {
        if (userId == null || bookId == null) {
            throw new IllegalArgumentException("Invalid user ID or book ID");
        }
    }

    @Override
    public BorrowerResponse getBookBorrower(Long id) {
        Log log = logService.getLog(id);
        BorrowerResponse response = new BorrowerResponse();
        response.setBorrowerId(log.getUserId());
        response.setMessage("success");
        return response;
    }
    @Override
    public AddBookResponse addBook(AddBookRequest request){
       // validateUserRole(request);
        Book book = bookService.create(request);
        LogResponse log = logService.logActivity(request.getUserId(), book.getBookId(), request.getDescription(), ACTIVITY.ADD_BOOK);
        AddBookResponse response = modelMapper.map(book, AddBookResponse.class);
        response.setLogId(log.getId());
        return  response;
    }


    private void validateUserRole(AddBookRequest request) {
        User user = getUser(request.getUserId());
        if(!user.getRoles().contains(ADMIN)) throw new IllegalStateException("User not allowed");
    }

    @Override
    public ReturnBookResponse returnBook(Long userId, Long bookId) {
        validateIdNotNull(userId, bookId);
        Book foundBook = bookService.getBook(bookId);
        User user = getUser(userId);
        // Check if the book is currently borrowed by the user
        validateBookIsBorrowedByTheUser(user,foundBook);
        // Update book status and persist
        foundBook.setStatus(STATUS.AVAILABLE);
        foundBook = bookService.persist(foundBook);
        // Log the activity
        LogResponse logResponse = logService.logActivity(user.getUserId(), foundBook.getBookId(), "Returned book", ACTIVITY.RETURNED);
        ReturnBookResponse returnBookResponse = modelMapper.map(logResponse, ReturnBookResponse.class);
        returnBookResponse.setLogId(logResponse.getId());
        returnBookResponse.setMessage("Book returned successfully");
        return returnBookResponse;
    }

    @Override
    public UserResponse getUserById(Long userId) {
        User user = getUser(userId);
        return modelMapper.map(user, UserResponse.class);
    }





    private void validateBookIsBorrowedByTheUser(User user, Book book) {
        if (book.getStatus() != STATUS.BORROWED || !logService.isBookBorrowedByUser(book.getBookId(), user.getUserId())) { throw new RuntimeException("The book is not borrowed by this user"); } }


}
