package com.accountbook.phoenix.Service;

import com.accountbook.phoenix.DTO.LoginRequest;
import com.accountbook.phoenix.DTO.MessageResponse;
import com.accountbook.phoenix.DTO.ResourceResponse;
import com.accountbook.phoenix.DTO.UserRequest;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<MessageResponse> userRegistration(UserRequest userRequest);

    ResponseEntity<ResourceResponse> userLogin(LoginRequest loginRequest);

    ResponseEntity<MessageResponse> fetchAllUsers();
}
