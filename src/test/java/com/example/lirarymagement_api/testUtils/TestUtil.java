package com.example.lirarymagement_api.testUtils;

import com.example.lirarymagement_api.dto.request.AddBookRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

public class TestUtil {
    public static AddBookRequest buildAddBookRequest()  {
        AddBookRequest addBookRequest = new AddBookRequest();
        addBookRequest.setTitle("Book Title");
        addBookRequest.setAuthor("Author Name");
        addBookRequest.setIsbn("ISBN-12341967890");
        return addBookRequest;
    }
}
