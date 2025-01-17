package com.example.lirarymagement_api.data.repository;

import com.example.lirarymagement_api.data.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface UserRepository extends  JpaRepository<User, Long> {
    boolean existsByEmail(String email);
  
}
