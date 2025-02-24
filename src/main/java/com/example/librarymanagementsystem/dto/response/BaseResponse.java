package com.example.librarymanagementsystem.dto.response;

import lombok.*;
import org.springframework.http.HttpStatus;
@NoArgsConstructor
public class BaseResponse  <T>{
    private HttpStatus code;
    private boolean status;

    public HttpStatus getCode() {
        return code;
    }

    public void setCode(HttpStatus code) {
        this.code = code;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    private T  data;

    public BaseResponse(HttpStatus code, boolean status, T data) {
        this.code = code;
        this.status = status;
        this.data = data;
    }
}
