package com.example.librarymanagementsystem.service;

import java.util.List;

import com.example.librarymanagementsystem.data.model.Book;
import com.example.librarymanagementsystem.dto.request.AddBookRequest;
import com.example.librarymanagementsystem.dto.request.UpdateBookRequest;
import com.example.librarymanagementsystem.dto.response.BooksResponse;
import com.example.librarymanagementsystem.dto.response.DeleteBookResponse;
import com.example.librarymanagementsystem.dto.response.GetBookResponse;
import com.example.librarymanagementsystem.dto.response.UpdateBookResponse;
import com.github.fge.jsonpatch.JsonPatch;

public interface BookService {
  public Book create(AddBookRequest request);
  public DeleteBookResponse deleteBook(Long request);


  Book getBook(Long id);

  List<BooksResponse> getAllBooks();

  GetBookResponse getBookById(Long id);
  GetBookResponse getBookByIsbn(String request);

  UpdateBookResponse updateBook(JsonPatch patch, UpdateBookRequest request);


  Book persist(Book book);
}