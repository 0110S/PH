package com.accountbook.phoenix.Service;

import com.accountbook.phoenix.DTO.*;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<RegistrationResponse> userRegistration(UserRequest userRequest);

    ResponseEntity<LoginResponse> userLogin(LoginRequest loginRequest);

    ResponseEntity<MessageResponse> fetchAllUsers();
}
