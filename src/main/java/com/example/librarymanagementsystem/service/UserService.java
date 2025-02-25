package com.example.librarymanagementsystem.service;


import com.example.librarymanagementsystem.dto.request.AddBookRequest;
import com.example.librarymanagementsystem.dto.request.RegisterUserRequest;
import com.example.librarymanagementsystem.dto.request.UpdateUserRequest;
import com.example.librarymanagementsystem.dto.response.*;
import com.github.fge.jsonpatch.JsonPatch;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

    RegisterUserResponse register(RegisterUserRequest request);

    AssignRoleResponse assignRole(Long userId);

    BorrowBookResponse borrowBook(Long userId, Long bookId);

    BorrowerResponse getBookBorrower(Long id);

    AddBookResponse addBook(AddBookRequest request);

    ReturnBookResponse returnBook(Long userId, Long bookId);

    UserResponse getUserById(Long userId);

    List<UserResponse> getAllUsers();

    UpdateUserResponse updateUser(Long userId, JsonPatch patch, UpdateUserRequest request);

    DeleteUserResponse deleteUser(Long userId);
}
  
