package com.example.librarymanagementsystem.controller;


import com.example.librarymanagementsystem.data.repository.UserRepository;
import com.example.librarymanagementsystem.dto.request.AddBookRequest;
import com.example.librarymanagementsystem.dto.request.RegisterUserRequest;
import com.example.librarymanagementsystem.dto.request.UpdateUserRequest;
import com.example.librarymanagementsystem.dto.response.*;
import com.example.librarymanagementsystem.service.UserService;
import com.github.fge.jsonpatch.JsonPatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(path = "/api/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterUserRequest request) {
        RegisterUserResponse response = userService.register(request);
        return new ResponseEntity<>(new BaseResponse(HttpStatus.CREATED, true, response), HttpStatus.CREATED);
    }
    @PostMapping("add_book")
    public ResponseEntity<?> addBook(@RequestBody AddBookRequest request) {
        AddBookResponse response = userService.addBook(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/assign-role/{userId}")
    public ResponseEntity<?> assignRole(@PathVariable Long userId) {
        AssignRoleResponse response = userService.assignRole(userId);
        return new ResponseEntity<>(new BaseResponse(HttpStatus.OK, true, response), HttpStatus.OK);
    }




    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable Long userId) {
       UserResponse response = userService.getUserById(userId);
        return new ResponseEntity<>(new BaseResponse(HttpStatus.OK, true, response), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        List<UserResponse> response = userService.getAllUsers();
        return new ResponseEntity<>(new BaseResponse(HttpStatus.OK, true, response), HttpStatus.OK);
    }

    @PutMapping("/update/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable Long userId, JsonPatch patch, @RequestBody UpdateUserRequest request) {
        UpdateUserResponse response = userService.updateUser(userId, patch, request);
        return new ResponseEntity<>(new BaseResponse(HttpStatus.OK, true, response), HttpStatus.OK);
    }


    @DeleteMapping("delete_user/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        DeleteUserResponse response = userService.deleteUser(userId);
        return new ResponseEntity<>(new BaseResponse(HttpStatus.OK, true, response), HttpStatus.OK);
    }

    @PostMapping("/borrow/{userId}/{bookId}")
    public ResponseEntity<?> borrowBook(@PathVariable Long userId, @PathVariable Long bookId) {
        BorrowBookResponse response = userService.borrowBook(userId, bookId);
        return new ResponseEntity<>(new BaseResponse(HttpStatus.OK, true, response), HttpStatus.OK);
    }

    @PostMapping("/return/{userId}/{bookId}")
    public ResponseEntity<?> returnBook(@PathVariable Long userId, @PathVariable Long bookId) {
        ReturnBookResponse response = userService.returnBook(userId, bookId);
        return new ResponseEntity<>(new BaseResponse(HttpStatus.OK, true, response), HttpStatus.OK);
    }

    @GetMapping("/borrower/{logId}")
    public ResponseEntity<?> getBookBorrower(@PathVariable Long logId) {
        BorrowerResponse response = userService.getBookBorrower(logId);
        return new ResponseEntity<>(new BaseResponse(HttpStatus.OK, true, response), HttpStatus.OK);
    }
}
