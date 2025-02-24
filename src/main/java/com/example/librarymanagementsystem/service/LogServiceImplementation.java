package com.example.librarymanagementsystem.service;


import com.example.librarymanagementsystem.data.constant.ACTIVITY;
import com.example.librarymanagementsystem.data.model.Log;
import com.example.librarymanagementsystem.data.repository.LogRepository;
import com.example.librarymanagementsystem.dto.response.LogResponse;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class LogServiceImplementation implements  LogService{
    private final ModelMapper modelMapper;
   private final LogRepository logRepository;

    public LogServiceImplementation(ModelMapper modelMapper, LogRepository logRepository) {
        this.modelMapper = modelMapper;
        this.logRepository = logRepository;
    }

    @Override
    public LogResponse logActivity(Long userId, Long bookId, String description, ACTIVITY action) {
        Log log = new Log();
        log.setUserId(userId);
        log.setBookId(bookId);
        log.setDescription(description);
        log.setAction(action);
        log.setTimestamp(LocalDateTime.now());
        log = logRepository.save(log);
        return modelMapper.map(log,LogResponse.class);
   }

    @Override
    public Log getLog(Long id) {
        return logRepository.findById(id).orElseThrow(()-> new RuntimeException("Could not find"));
    }

    @Override
    public boolean isBookBorrowedByUser(Long bookId, Long userId) {
        return logRepository.existsByBookIdAndUserIdAndActivity(bookId, userId, ACTIVITY.BORROWED);
    }

}
