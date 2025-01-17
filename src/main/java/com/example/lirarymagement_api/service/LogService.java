package com.example.lirarymagement_api.service;

import com.example.lirarymagement_api.data.constant.ACTIVITY;
import com.example.lirarymagement_api.data.model.Log;
import com.example.lirarymagement_api.dto.response.LogResponse;
import org.springframework.stereotype.Service;

@Service
public interface LogService {

    LogResponse logActivity(Long userId, Long bookId, String description, ACTIVITY action);

    Log getLog(Long id);

    boolean isBookBorrowedByUser(Long id, Long id1);
}
