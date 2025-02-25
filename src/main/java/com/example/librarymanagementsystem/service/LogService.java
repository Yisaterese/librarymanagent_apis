package com.example.librarymanagementsystem.service;


import com.example.librarymanagementsystem.data.constant.ACTIVITY;
import com.example.librarymanagementsystem.data.model.Log;
import com.example.librarymanagementsystem.dto.response.LogResponse;
import org.springframework.stereotype.Service;

@Service
public interface LogService {

    LogResponse logActivity(Long userId, Long bookId, String description, ACTIVITY action);

    Log getLog(Long id);

    boolean isBookBorrowedByUser(Long id, Long id1);
}
