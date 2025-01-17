package com.example.lirarymagement_api.service;

import com.example.lirarymagement_api.data.constant.ACTIVITY;
import com.example.lirarymagement_api.data.model.Log;
import com.example.lirarymagement_api.data.repository.LogRepository;
import com.example.lirarymagement_api.dto.response.LogResponse;
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
