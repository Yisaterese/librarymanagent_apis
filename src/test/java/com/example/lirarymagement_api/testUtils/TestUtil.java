package com.example.lirarymagement_api.testUtils;

import com.example.lirarymagement_api.dto.request.AddBookRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

public class TestUtil {
    public static final String STRING_COVERIMAGE_LOCATION= "C:\\Users\\Semicolon Labs\\Pictures\\git commit git push leaving.jpeg";
    public static AddBookRequest buildAddBookRequest(InputStream inputStream) throws IOException {
        AddBookRequest addBookRequest = new AddBookRequest();
        MultipartFile file = new MockMultipartFile("book cover image",inputStream);
        addBookRequest.setTitle("Book Title");
        addBookRequest.setAuthor("Author Name");
        addBookRequest.setIsbn("ISBN-12341967890");
        addBookRequest.setCoverImageUrl(file);
        return addBookRequest;
    }
}
