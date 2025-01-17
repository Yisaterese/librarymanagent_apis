package com.example.lirarymagement_api.service;

import java.util.List;

import com.example.lirarymagement_api.data.model.Book;
import com.example.lirarymagement_api.dto.request.AddBookRequest;
import com.example.lirarymagement_api.dto.request.DeleteBookRequest;
import com.example.lirarymagement_api.dto.request.GetBookRequest;
import com.example.lirarymagement_api.dto.request.UpdateBookRequest;
import com.example.lirarymagement_api.dto.response.*;
import com.github.fge.jsonpatch.JsonPatch;
import org.springframework.stereotype.Service;

public interface BookService {
  public AddBookResponse addBook(AddBookRequest request);
  public GetBookResponse getBookById(GetBookRequest request);
  public DeleteBookResponse deleteBook(DeleteBookRequest request);
  public List<BooksResponse> getAllBooks();
  public GetBookResponse getBookByIsbn(GetBookRequest request);
  UpdateBookResponse updateBook(JsonPatch patch, UpdateBookRequest request);

  Book getBookById(Long userId);

  Book persist(Book foundbook);
}