package com.example.librarymanagementsystem.service;

import com.cloudinary.Cloudinary;

import com.example.librarymanagementsystem.data.model.Book;
import com.example.librarymanagementsystem.data.repository.BookRepository;
import com.example.librarymanagementsystem.dto.request.AddBookRequest;
import com.example.librarymanagementsystem.dto.request.UpdateBookRequest;
import com.example.librarymanagementsystem.dto.response.BooksResponse;
import com.example.librarymanagementsystem.dto.response.DeleteBookResponse;
import com.example.librarymanagementsystem.dto.response.GetBookResponse;
import com.example.librarymanagementsystem.dto.response.UpdateBookResponse;
import com.example.librarymanagementsystem.exception.BookNotFoundException;
import com.example.librarymanagementsystem.exception.BookUpdateFailedException;
import com.example.librarymanagementsystem.exception.ExistingBookException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.librarymanagementsystem.data.constant.STATUS.AVAILABLE;


@Service
public class BookServiceImplementation implements BookService {
    private final BookRepository bookRepository;
    private final ModelMapper modelMapper;
    private final ObjectMapper objectMapper;
    private final Cloudinary cloudinary;
    private LogService logService;

    public BookServiceImplementation(BookRepository bookRepository,
                                     ModelMapper modelMapper,
                                     ObjectMapper objectMapper,
                                     Cloudinary cloudinary,
                                     LogService logService) {
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
    Book book = bookRepository.findByIsbn(isbn).orElseThrow(()-> new BookNotFoundException("Book not found"));
    GetBookResponse response = modelMapper.map(book, GetBookResponse.class);
    response.setMessage("Success");
    return response;
}


@Override
public DeleteBookResponse deleteBook(Long id) {
    Book book = getBook(id);
    bookRepository.delete(book);
    DeleteBookResponse response = modelMapper.map(book,DeleteBookResponse.class);
    response.setMessage("Book deleted successfully");
    return response;
}

 private static void validateBookNotNull(Book book){
  if (book == null) {
    throw new BookNotFoundException("Book not found");
  }
}



@Override
public List<BooksResponse> getAllBooks() {
  List<Book> books = bookRepository.findAll();
  if (books.isEmpty()) {
      throw new BookNotFoundException("No books found");
  }
  return books.stream()
          .map(book -> modelMapper.map(book, BooksResponse.class))
          .toList();
}

    @Override
    public GetBookResponse getBookById(Long id) {
       Book book = getBook(id);
        GetBookResponse response = modelMapper.map(book, GetBookResponse.class);
        response.setMessage("Success");
        return response;
    }
    @Override
    public Book getBook(Long id){
        Book book = bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException("Book not found"));
        validateBookNotNull(book);
        return book;
    }

    @Override
public UpdateBookResponse updateBook(JsonPatch patch, UpdateBookRequest request) {
    try {
        Book book = getBook(request.getId());
        // Apply the JSON Patch to the book
        Book bookPatched = applyPatch(patch, book);
        // Save the updated book
        Book updatedBook = bookRepository.save(bookPatched);
        // Map the updated book to the response
        UpdateBookResponse response = modelMapper.map(updatedBook, UpdateBookResponse.class);
        response.setMessage("Book updated successfully");
        return response;
    } catch (JsonPatchException | JsonProcessingException e) {
        throw new BookUpdateFailedException("Failed to update book details");
    }
}


    @Override
    public Book persist(Book foundbook) {
        return bookRepository.save(foundbook);
    }


    private Book applyPatch(JsonPatch patch, Book book) throws JsonPatchException, JsonProcessingException {
   // Convert the book to a JSON Node
    JsonNode patched = patch.apply(objectMapper.convertValue(book, JsonNode.class));
    // Convert the patched JSON Node back to a Book object
    return objectMapper.treeToValue(patched, Book.class);
}

}
