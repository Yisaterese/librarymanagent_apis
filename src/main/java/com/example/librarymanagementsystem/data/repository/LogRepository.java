package com.example.librarymanagementsystem.data.repository;


import com.example.librarymanagementsystem.data.constant.ACTIVITY;
import com.example.librarymanagementsystem.data.model.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LogRepository extends JpaRepository<Log,Long> {

    @Query("SELECT CASE WHEN COUNT(l) > 0 THEN TRUE ELSE FALSE END FROM Log l WHERE l.bookId = :bookId AND l.userId = :userId AND l.action = :action")
    boolean existsByBookIdAndUserIdAndActivity(@Param("bookId") Long bookId, @Param("userId") Long userId, @Param("activity") ACTIVITY action);
}
