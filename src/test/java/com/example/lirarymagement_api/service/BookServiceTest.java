package com.example.lirarymagement_api.service;

import com.cloudinary.Cloudinary;
import com.example.lirarymagement_api.data.repository.BookRepository;
import com.example.lirarymagement_api.dto.request.AddBookRequest;
import com.example.lirarymagement_api.dto.response.AddBookResponse;
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

import static com.example.lirarymagement_api.testUtils.TestUtil.STRING_COVERIMAGE_LOCATION;
import static com.example.lirarymagement_api.testUtils.TestUtil.buildAddBookRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.when;

@SpringBootTest
public class BookServiceTest {

    @Autowired
    private Cloudinary cloudinary;
    @Autowired
    private BookService bookService;
    @Mock
    private BookRepository bookRepository;

    @Test
    public void addBook_successTest() {
        Path bookPath = Paths.get(STRING_COVERIMAGE_LOCATION);
        try(var inputStream  = Files.newInputStream(bookPath)){
            AddBookRequest addBookRequest = buildAddBookRequest(inputStream);
            addBookRequest.setIsbn("0-306-49605-2");
            AddBookResponse addBookResponse = bookService.addBook(addBookRequest);
            assertThat(addBookResponse).isNotNull();
            assertThat(addBookResponse.getMessage()).isEqualTo("Success");
            assertThat(addBookResponse.getCoverImageUrl()).isNotNull();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    @Test public void addBookTwoBooksWithSameIsbn_throwsException() throws IOException {
        Path bookPath = Paths.get(STRING_COVERIMAGE_LOCATION);
        try (var inputStream1 = Files.newInputStream(bookPath);
             var inputStream2 = Files.newInputStream(bookPath)) {
            AddBookRequest addBookRequest = buildAddBookRequest(inputStream1);
            AddBookRequest addBookRequest1 = buildAddBookRequest(inputStream2);
            addBookRequest.setIsbn("0-306-45605-2");
            addBookRequest1.setIsbn("0-306-45605-2");
            when(bookRepository.existsByIsbn("0-306-45605-2")).thenReturn(false).thenReturn(true);
            bookService.addBook(addBookRequest);
            assertThrows(ExistingBookException.class, () -> bookService.addBook(addBookRequest1));
    } }

    @Test
    public void addBookWithInvalidCoverImagePath_throwsExceptionTest() throws IOException {
        Path bookPath = Paths.get("invalid_cover_image_path");
        when(cloudinary.uploader().upload(any(byte[].class), anyMap())).thenThrow(new IOException("invalid/path/to/cover/image"));
        FailedToUploadBookException exception = assertThrows(FailedToUploadBookException.class, () -> {
            try (var inputStream = Files.newInputStream(bookPath)) {
                AddBookRequest addBookRequest = buildAddBookRequest(inputStream);
                addBookRequest.setIsbn("0-306-41615-2");
                bookService.addBook(addBookRequest);
            }
        });
        assertThat(exception.getMessage()).contains("Failed to add book");
    }

    @Test
    public void addBookWithInvalidIsbn_throwsExceptionTest() {
        Path bookPath = Paths.get(STRING_COVERIMAGE_LOCATION);
        try (var inputStream = Files.newInputStream(bookPath)) {
            AddBookRequest addBookRequest = buildAddBookRequest(inputStream);
            addBookRequest.setIsbn("invalid-isbn");
            assertThrows(IllegalArgumentException.class, () ->  bookService.addBook(addBookRequest));
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Test
    public void addBookWithOnlyWhiteSpaces_throwsExceptionTest() {
        Path bookPath = Paths.get(STRING_COVERIMAGE_LOCATION);
        try (var inputStream = Files.newInputStream(bookPath)) {
            AddBookRequest addBookRequest = buildAddBookRequest(inputStream);
            addBookRequest.setIsbn("   ");
            addBookRequest.setTitle("   ");
            addBookRequest.setAuthor("   ");
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                bookService.addBook(addBookRequest);
            });
            assertThat(exception.getMessage()).contains("ISBN cannot be empty or whitespace.");
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());

        }
    }

    @Test
    public void addBookWithTitleWhiteSpaces_throwsExceptionTest() {
        Path bookPath = Paths.get(STRING_COVERIMAGE_LOCATION);
        try (var inputStream = Files.newInputStream(bookPath)) {
            AddBookRequest addBookRequest = buildAddBookRequest(inputStream);
            addBookRequest.setIsbn("0-306-42615-2");
            addBookRequest.setTitle("   ");
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                bookService.addBook(addBookRequest);
            });
            assertThat(exception.getMessage()).contains("Book title cannot be empty or whitespace.");
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());

        }
    }
    @Test
    public void addBookWithAuthorWhiteSpaces_throwsExceptionTest() {
        Path bookPath = Paths.get(STRING_COVERIMAGE_LOCATION);
        try (var inputStream = Files.newInputStream(bookPath)) {
            AddBookRequest addBookRequest = buildAddBookRequest(inputStream);
            addBookRequest.setIsbn("0-306-30615-2");
            addBookRequest.setAuthor(" ");
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                bookService.addBook(addBookRequest);
            });
            assertThat(exception.getMessage()).contains("Author name cannot be empty or whitespace.");
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Test
    public void addBookWithNullCoverImage_throwsExceptionTest() {
        Path bookPath = Paths.get(STRING_COVERIMAGE_LOCATION);
        try (var inputStream = Files.newInputStream(bookPath)) {
            AddBookRequest addBookRequest = buildAddBookRequest(inputStream);
            addBookRequest.setIsbn("0-306-40615-4");
            addBookRequest.setCoverImageUrl(null);
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                bookService.addBook(addBookRequest);
            });
            assertThat(exception.getMessage()).contains("Cover image URL cannot be empty or whitespace.");
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Test
    public void getBookById(){
        String request = new String();
        request.setId(1L);
       GetBookResponse response = bookService.getBookById(request);
       assertThat(response.getMessage()).contains( "Success");
       assertThat(response.getId()).isEqualTo(1L);
    }
    @Test
    public void getBookByIdWithInvalidId_throwsExceptionTest(){
        String request = new String();
        request.setId(2L);
        assertThrows(BookNotFoundException.class, () -> bookService.getBookById(request));
    }
}
