package com.example.librarymanagementsystem.testUtils;

import com.example.librarymanagementsystem.dto.request.AddBookRequest;


public class TestUtil {
    public static AddBookRequest buildAddBookRequest()  {
        AddBookRequest addBookRequest = new AddBookRequest();
        addBookRequest.setTitle("Book Title");
        addBookRequest.setAuthor("Author Name");
        addBookRequest.setIsbn("ISBN-12341967890");
        return addBookRequest;
    }
}
