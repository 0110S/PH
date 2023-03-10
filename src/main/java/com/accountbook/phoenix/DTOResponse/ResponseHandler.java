package com.accountbook.phoenix.DTOResponse;

import com.accountbook.phoenix.Exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@ControllerAdvice
public class ResponseHandler {

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Object> handleUsernameNotFoundException(
            UsernameNotFoundException ex) {
        String errorMessage = "User not found";
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(errorMessage);
    }
    @ExceptionHandler(CommentNotFoundException.class)
    public ResponseEntity<String> comment(CommentNotFoundException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }

    @ExceptionHandler(FriendsNotFoundException.class)
    public ResponseEntity<String> friendNotFound(FriendsNotFoundException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }

    @ExceptionHandler(InvalidUserException.class)
    public ResponseEntity<String> invalidUser(InvalidUserException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }

    @ExceptionHandler(NoFollowerException.class)
    public ResponseEntity<String> noFollower(NoFollowerException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }

    @ExceptionHandler(PostAlreadyLikedException.class)
    public ResponseEntity<String> postLiked(PostAlreadyLikedException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }

    @ExceptionHandler(PostNotFoundException.class)
    public ResponseEntity<String> postNotFound(PostNotFoundException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }

    @ExceptionHandler(UserFoundException.class)
    public ResponseEntity<String> userFound(UserFoundException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<String> tokenExpired(TokenExpiredException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }


}
