package com.example.lirarymagement_api.service;

import java.util.List;

import com.example.lirarymagement_api.data.model.Book;
import com.example.lirarymagement_api.dto.request.AddBookRequest;
import com.example.lirarymagement_api.dto.request.UpdateBookRequest;
import com.example.lirarymagement_api.dto.response.*;
import com.github.fge.jsonpatch.JsonPatch;

public interface BookService {
  public Book create(AddBookRequest request);
  public DeleteBookResponse deleteBook(Long request);


  Book getBook(Long id);

  List<BooksResponse> getAllBooks();

  GetBookResponse getBookById(Long id);
  GetBookResponse getBookByIsbn(String request);

  UpdateBookResponse updateBook(JsonPatch patch, UpdateBookRequest request);


  com.example.lirarymagement_api.data.model.Book persist(com.example.lirarymagement_api.data.model.Book book);
}