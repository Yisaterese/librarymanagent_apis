package com.example.lirarymagement_api.service;

import com.example.lirarymagement_api.data.constant.ROLE;
import com.example.lirarymagement_api.dto.request.RegisterUserRequest;
import com.example.lirarymagement_api.dto.response.*;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    RegisterUserResponse register(RegisterUserRequest request);

    AssignRoleResponse assignRole(Long userId, ROLE role);

    BorrowBookResponse borrowBook(Long userId, Long bookId);

    BorrowerResponse getBookBorrower(Long id);

    ReturnBookResponse returnBook(Long userId, Long bookId);
}
  
