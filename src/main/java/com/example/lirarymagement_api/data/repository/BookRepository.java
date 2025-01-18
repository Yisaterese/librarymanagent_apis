package com.example.lirarymagement_api.data.repository;

import com.example.lirarymagement_api.data.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book,Long>{
//    @Query("SELECT u FROM Book u WHERE u.isbn= :isbn")
   Optional <Book>  findByIsbn(String isbn);
    boolean existsByIsbn(String isbn);
}
