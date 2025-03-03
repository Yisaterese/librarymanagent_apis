package com.example.librarymanagementsystem.service;


import com.example.librarymanagementsystem.data.constant.ACTIVITY;
import com.example.librarymanagementsystem.data.constant.STATUS;
import com.example.librarymanagementsystem.data.model.Book;
import com.example.librarymanagementsystem.data.model.Log;
import com.example.librarymanagementsystem.data.model.User;
import com.example.librarymanagementsystem.data.repository.UserRepository;
import com.example.librarymanagementsystem.dto.request.AddBookRequest;
import com.example.librarymanagementsystem.dto.request.RegisterUserRequest;
import com.example.librarymanagementsystem.dto.request.UpdateUserRequest;
import com.example.librarymanagementsystem.dto.response.*;
import com.example.librarymanagementsystem.exception.ExistingUserException;
import com.example.librarymanagementsystem.exception.UserNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.librarymanagementsystem.data.constant.ROLE.ADMIN;
import static com.example.librarymanagementsystem.data.constant.ROLE.USER;
import static com.example.librarymanagementsystem.data.constant.STATUS.BORROWED;

@Slf4j
@Service
public class UserServiceImplementation implements UserService {
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
        validateRole(user);
        user.getRoles().add(ADMIN);
        user = userRepository.save(user);
       AssignRoleResponse response = modelMapper.map(user, AssignRoleResponse.class);
       response.setMessage("user assigned ADMIN role  successfully");
        return response;
    }

    private void validateRole(User user) {
        if (user.getRoles().contains( ADMIN)) {
            throw new RuntimeException("User has role ADMIN");
        }
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
        return new DeleteUserResponse("user deleted successfully");
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
        LogResponse logResponse = logService.logActivity(user.getId(), foundBook.getBookId(), "Borrowed book", ACTIVITY.BORROWED);
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
        Book book = bookService.create(request);
        LogResponse log = logService.logActivity(request.getUserId(), book.getBookId(), request.getDescription(), ACTIVITY.ADD_BOOK);
        AddBookResponse response = modelMapper.map(book, AddBookResponse.class);
        response.setMessage("Book added successfully");
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
        // Log the activity1
        LogResponse logResponse = logService.logActivity(user.getId(), foundBook.getBookId(), "Returned book", ACTIVITY.RETURNED);
        ReturnBookResponse returnBookResponse = modelMapper.map(logResponse, ReturnBookResponse.class);
        returnBookResponse.setLogId(logResponse.getId());
        returnBookResponse.setMessage("Book returned successfully");
        return returnBookResponse;
    }


    @Override
    public UserResponse getUserById(Long userId) {
        User user = getUser(userId);
        log.info("user {}",user );
        return modelMapper.map(user, UserResponse.class);
    }
    private  User getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow( ()-> new UserNotFoundException("User not found"));
    }

    private void validateBookIsBorrowedByTheUser(User user, Book book) {
        if (book.getStatus() != STATUS.BORROWED || !logService.isBookBorrowedByUser(book.getBookId(), user.getId())) { throw new RuntimeException("The book is not borrowed by this user"); } }


}
