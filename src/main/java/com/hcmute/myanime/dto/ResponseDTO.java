package com.hcmute.myanime.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

@JsonIgnoreProperties(value= {"handler","hibernateLazyInitializer","FieldHandler"})
public class ResponseDTO {
    private HttpStatus status;
    private String message;
    private Object data;

    public ResponseDTO(HttpStatus status, String message) {
        this.status = status;
        this.message = message;

    }

    public ResponseDTO(HttpStatus status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
