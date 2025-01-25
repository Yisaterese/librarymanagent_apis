package com.example.lirarymagement_api.service;

import com.example.lirarymagement_api.data.repository.BookRepository;
import com.example.lirarymagement_api.dto.request.AddBookRequest;
import com.example.lirarymagement_api.dto.response.GetBookResponse;
import com.example.lirarymagement_api.exception.BookNotFoundException;
import com.example.lirarymagement_api.exception.ExistingBookException;
import com.example.lirarymagement_api.exception.FailedToUploadBookException;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.example.lirarymagement_api.testUtils.TestUtil.buildAddBookRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
public class BookServiceTest {

    @Autowired
    private BookService bookService;
    @Mock
    private BookRepository bookRepository;

    @Test
    public void addBook_successTest() {
            AddBookRequest addBookRequest = buildAddBookRequest();
            addBookRequest.setIsbn("0-306-49605-2");
            Book addBookResponse = bookService.create(addBookRequest);
            assertThat(addBookResponse).isNotNull();
            assertThat(addBookResponse.getMessage()).isEqualTo("Success");
            assertThat(addBookResponse.getCoverImageUrl()).isNotNull();
    }


    @Test public void addBookTwoBooksWithSameIsbn_throwsException() throws IOException {

            AddBookRequest addBookRequest = buildAddBookRequest();
            AddBookRequest addBookRequest1 = buildAddBookRequest();
            addBookRequest.setIsbn("0-306-45605-2");
            addBookRequest1.setIsbn("0-306-45605-2");
            when(bookRepository.existsByIsbn("0-306-45605-2")).thenReturn(false).thenReturn(true);
            bookService.create(addBookRequest);
            assertThrows(ExistingBookException.class, () -> bookService.create(addBookRequest1));
    }

    @Test
    public void addBookWithInvalidCoverImagePath_throwsExceptionTest(){
        Path bookPath = Paths.get("invalid_cover_image_path");
        FailedToUploadBookException exception = assertThrows(FailedToUploadBookException.class, () -> {
            try (var inputStream = Files.newInputStream(bookPath)) {
                AddBookRequest addBookRequest = buildAddBookRequest();
                addBookRequest.setIsbn("0-306-41615-2");
                bookService.create(addBookRequest);
            }
        });
        assertThat(exception.getMessage()).contains("Failed to add book");
    }

    @Test
    public void addBookWithInvalidIsbn_throwsExceptionTest() {

            AddBookRequest addBookRequest = buildAddBookRequest();
            addBookRequest.setIsbn("invalid-isbn");
            assertThrows(IllegalArgumentException.class, () ->  bookService.create(addBookRequest));
    }

    @Test
    public void addBookWithOnlyWhiteSpaces_throwsExceptionTest() {

            AddBookRequest addBookRequest = buildAddBookRequest();
            addBookRequest.setIsbn("   ");
            addBookRequest.setTitle("   ");
            addBookRequest.setAuthor("   ");
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                bookService.create(addBookRequest);
            });
            assertThat(exception.getMessage()).contains("ISBN cannot be empty or whitespace.");
    }

    @Test
    public void addBookWithTitleWhiteSpaces_throwsExceptionTest() {

            AddBookRequest addBookRequest = buildAddBookRequest();
            addBookRequest.setIsbn("0-306-42615-2");
            addBookRequest.setTitle("   ");
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                bookService.create(addBookRequest);
            });
            assertThat(exception.getMessage()).contains("Book title cannot be empty or whitespace.");
    }
    @Test
    public void addBookWithAuthorWhiteSpaces_throwsExceptionTest() {

            AddBookRequest addBookRequest = buildAddBookRequest();
            addBookRequest.setIsbn("0-306-30615-2");
            addBookRequest.setAuthor(" ");
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                bookService.create(addBookRequest);
            });
            assertThat(exception.getMessage()).contains("Author name cannot be empty or whitespace.");
    }


    @Test
    public void getBookById(){
        Long id = 1L;
       GetBookResponse response = bookService.getBookById(id);
       assertThat(response.getMessage()).contains( "Success");
       assertThat(response.getId()).isEqualTo(1L);
    }
    @Test
    public void getBookByIdWithInvalidId_throwsExceptionTest(){
        Long id  = 2L;
        assertThrows(BookNotFoundException.class, () -> bookService.getBookById(id));
    }
}
