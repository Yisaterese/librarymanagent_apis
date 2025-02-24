package com.example.librarymanagementsystem.controller;


import com.example.librarymanagementsystem.dto.request.UpdateBookRequest;
import com.example.librarymanagementsystem.dto.response.*;
import com.example.librarymanagementsystem.service.BookServiceImplementation;
import com.github.fge.jsonpatch.JsonPatch;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/book")
public class BookController {

    private final BookServiceImplementation bookService;

    public BookController(BookServiceImplementation bookService) {
        this.bookService = bookService;
    }

   

    @GetMapping("/{id}")
    public ResponseEntity<?> getBookById(@PathVariable Long id) {
       GetBookResponse response = bookService.getBookById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{isbn}")
    public ResponseEntity<?> getBookByIsbn(@PathVariable String isbn) {
        GetBookResponse response = bookService.getBookByIsbn(isbn);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("all_books")
    public ResponseEntity<?> getAllBooks() {
        List<BooksResponse> books = bookService.getAllBooks();
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateBook(@PathVariable Long id, @RequestBody JsonPatch patch,
                                                         @RequestBody UpdateBookRequest request) {
        request.setId(id);
        UpdateBookResponse response = bookService.updateBook(patch, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable Long id) {
        DeleteBookResponse response = bookService.deleteBook(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
