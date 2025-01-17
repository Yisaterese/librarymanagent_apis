package com.example.lirarymagement_api.service;

import com.example.lirarymagement_api.data.constant.ACTIVITY;
import com.example.lirarymagement_api.data.constant.ROLE;
import com.example.lirarymagement_api.data.constant.STATUS;
import com.example.lirarymagement_api.data.model.Book;
import com.example.lirarymagement_api.data.model.Log;
import com.example.lirarymagement_api.data.model.User;
import com.example.lirarymagement_api.data.repository.UserRepository;
import com.example.lirarymagement_api.dto.request.RegisterUserRequest;
import com.example.lirarymagement_api.dto.response.*;
import com.example.lirarymagement_api.exception.ExistingUserException;
import com.example.lirarymagement_api.exception.UserNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import static com.example.lirarymagement_api.data.constant.ROLE.ADMIN;
import static com.example.lirarymagement_api.data.constant.ROLE.USER;
import static com.example.lirarymagement_api.data.constant.STATUS.BORROWED;

@Service
public class UserServiceImplementation implements UserService{
   private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final BookService bookService;
    private final LogService logService;

    public UserServiceImplementation(UserRepository userRepository, ModelMapper modelMapper, BookService bookService, LogService logService) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.bookService = bookService;
        this.logService = logService;
    }

    @Override
    public RegisterUserResponse register(RegisterUserRequest request){
        validateUserRequest(request);
        User newUser = modelMapper.map(request, User.class);
        newUser.setRole(USER);
        newUser = userRepository.save(newUser);
        RegisterUserResponse response = modelMapper.map(newUser, RegisterUserResponse.class);
        response.setMessage("User registered successfully");
        return response;
    }

    @Override
    public AssignRoleResponse assignRole(Long userId, ROLE role) {
        User user = getUserById(userId);
        user.setRole(ADMIN);
        user = userRepository.save(user);
        return modelMapper.map(user, AssignRoleResponse.class);
    }

    private User getUserById(Long userId) {
            return userRepository.findById(userId).orElseThrow( ()-> new UserNotFoundException("User not found"));
    }

    private void validateUserRequest(RegisterUserRequest request) {
        if(userRepository.existsByEmail(request.getEmail()))throw new ExistingUserException("Email taken");
    }

    @Override
    public BorrowBookResponse borrowBook(Long userId, Long bookId) {
        validateIdNotNull(userId,bookId);// validate the user id and book id to avoid null inputs
        Book foundBook = bookService.getBookById(bookId);
        User user = getUserById(userId);
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
    public ReturnBookResponse returnBook(Long userId, Long bookId) {
        validateIdNotNull(userId, bookId);
        Book foundBook = bookService.getBookById(bookId);
        User user = getUserById(userId);
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

    private void validateBookIsBorrowedByTheUser(User user, Book book) {
        if (book.getStatus() != STATUS.BORROWED || !logService.isBookBorrowedByUser(book.getBookId(), user.getUserId())) { throw new RuntimeException("The book is not borrowed by this user"); } }


}
