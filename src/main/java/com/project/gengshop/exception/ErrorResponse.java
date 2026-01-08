package com.project.gengshop.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ErrorResponse {

    private Integer status;
    private String error;
    private String message;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime timestamp;


    // Constructor to initialize ErrorResponse with HttpStatus and message
    public ErrorResponse(HttpStatus httpStatus, String message) {
        this();
        this.status = httpStatus.value();
        this.error = httpStatus.getReasonPhrase();
        this.message = message;
    }

    // Default constructor to set the timestamp
    public ErrorResponse() {
        this.timestamp = LocalDateTime.now();
    }
}
