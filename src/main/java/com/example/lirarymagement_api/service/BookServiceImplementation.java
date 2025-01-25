package com.example.lirarymagement_api.service;

import com.cloudinary.Cloudinary;
import com.example.lirarymagement_api.data.model.Book;
import com.example.lirarymagement_api.data.repository.BookRepository;
import com.example.lirarymagement_api.dto.request.AddBookRequest;
import com.example.lirarymagement_api.dto.request.UpdateBookRequest;
import com.example.lirarymagement_api.dto.response.*;
import com.example.lirarymagement_api.exception.BookNotFoundException;
import com.example.lirarymagement_api.exception.BookUpdateFailedException;
import com.example.lirarymagement_api.exception.ExistingBookException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.lirarymagement_api.data.constant.STATUS.AVAILABLE;

@Service
public class BookServiceImplementation implements BookService {
    private final BookRepository bookRepository;
    private final ModelMapper modelMapper;
    private final ObjectMapper objectMapper;
    private final Cloudinary cloudinary;
    private LogService logService;

    public BookServiceImplementation(BookRepository bookRepository, ModelMapper modelMapper, ObjectMapper objectMapper, Cloudinary cloudinary, LogService logService) {
        this.bookRepository = bookRepository;
        this.modelMapper = modelMapper;
        this.objectMapper = objectMapper;
        this.cloudinary = cloudinary;
        this.logService = logService;
    }

    @Override
    public Book create(AddBookRequest request) {
            validateRequest(request);
            Book book = modelMapper.map(request, Book.class);
            book.setStatus(AVAILABLE);
            book =  bookRepository.save(book);
            return book;
    }


    private void validateRequest(AddBookRequest request) {
        if (request.getIsbn() == null || request.getIsbn().trim().isEmpty()) {
            throw new IllegalArgumentException("ISBN cannot be empty or whitespace.");
        }
        if (bookRepository.existsByIsbn(request.getIsbn())) {
            throw new ExistingBookException("A book with this ISBN already exists.");
        }
        if (!isValidIsbn(request.getIsbn())) {
            throw new IllegalArgumentException("Invalid ISBN format.");
        }
        if (request.getTitle() == null || request.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Book title cannot be empty or whitespace.");
        }
        if (request.getAuthor() == null || request.getAuthor().trim().isEmpty()) {
            throw new IllegalArgumentException("Author name cannot be empty or whitespace.");
        }

    }

    private boolean isValidIsbn(String isbn) {
        java.lang.String isbn10Format = "^(?:\\d[\\ |-]?){9}[\\d|X]$";
        java.lang.String isbn13Format = "^(?:\\d[\\ |-]?){13}$";
        return isbn.matches(isbn10Format) || isbn.matches(isbn13Format);
    }


@Override
public GetBookResponse getBookByIsbn(String isbn) {
    com.example.lirarymagement_api.data.model.Book book = bookRepository.findByIsbn(isbn).orElseThrow(()-> new BookNotFoundException("Book not found"));
    GetBookResponse response = modelMapper.map(book, GetBookResponse.class);
    response.setMessage("Success");
    return response;
}


@Override
public DeleteBookResponse deleteBook(Long id) {
    com.example.lirarymagement_api.data.model.Book book = getBook(id);
    bookRepository.delete(book);
    DeleteBookResponse response = modelMapper.map(book,DeleteBookResponse.class);
    response.setMessage("Book deleted successfully");
    return response;
}

 private static void validateBookNotNull(com.example.lirarymagement_api.data.model.Book book){
  if (book == null) {
    throw new BookNotFoundException("Book not found");
  }
}



@Override
public List<BooksResponse> getAllBooks() {
  List<com.example.lirarymagement_api.data.model.Book> books = bookRepository.findAll();
  if (books.isEmpty()) {
      throw new BookNotFoundException("No books found");
  }
  return books.stream()
          .map(book -> modelMapper.map(book, BooksResponse.class))
          .toList();
}

    @Override
    public GetBookResponse getBookById(Long id) {
        com.example.lirarymagement_api.data.model.Book book = getBook(id);
        GetBookResponse response = modelMapper.map(book, GetBookResponse.class);
        response.setMessage("Success");
        return response;
    }
    @Override
    public com.example.lirarymagement_api.data.model.Book getBook(Long id){
        com.example.lirarymagement_api.data.model.Book book = bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException("Book not found"));
        validateBookNotNull(book);
        return book;
    }

    @Override
public UpdateBookResponse updateBook(JsonPatch patch, UpdateBookRequest request) {
    try {
        com.example.lirarymagement_api.data.model.Book book = getBook(request.getId());
        // Apply the JSON Patch to the book
        com.example.lirarymagement_api.data.model.Book bookPatched = applyPatch(patch, book);
        // Save the updated book
        com.example.lirarymagement_api.data.model.Book updatedBook = bookRepository.save(bookPatched);
        // Map the updated book to the response
        UpdateBookResponse response = modelMapper.map(updatedBook, UpdateBookResponse.class);
        response.setMessage("Book updated successfully");
        return response;
    } catch (JsonPatchException | JsonProcessingException e) {
        throw new BookUpdateFailedException("Failed to update book details");
    }
}


    @Override
    public com.example.lirarymagement_api.data.model.Book persist(com.example.lirarymagement_api.data.model.Book foundbook) {
        return bookRepository.save(foundbook);
    }


    private com.example.lirarymagement_api.data.model.Book applyPatch(JsonPatch patch, com.example.lirarymagement_api.data.model.Book book) throws JsonPatchException, JsonProcessingException {
   // Convert the book to a JSON Node
    JsonNode patched = patch.apply(objectMapper.convertValue(book, JsonNode.class));
    // Convert the patched JSON Node back to a Book object
    return objectMapper.treeToValue(patched, com.example.lirarymagement_api.data.model.Book.class);
}

}
