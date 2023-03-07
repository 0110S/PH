package com.accountbook.phoenix.Exception;

import com.accountbook.phoenix.DTOResponse.MessageResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ResponseHandler {

    @ExceptionHandler(InvalidUserException.class)
    public ResponseEntity<MessageResponse> getResponse(InvalidUserException e) {
        MessageResponse messageResponse = new MessageResponse();
        messageResponse.setMessage(e.getMessage());
        messageResponse.setResponse(HttpStatus.NOT_FOUND);
        return ResponseEntity.badRequest().body(messageResponse);
    }
}
