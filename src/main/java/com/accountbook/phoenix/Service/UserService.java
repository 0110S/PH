package com.accountbook.phoenix.Service;

import com.accountbook.phoenix.DTO.*;
import com.accountbook.phoenix.DTOResponse.LoginResponse;
import com.accountbook.phoenix.DTOResponse.MessageResponse;
import com.accountbook.phoenix.DTOResponse.UserResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    ResponseEntity<UserResponse> userRegistration(UserRequest userRequest);

    ResponseEntity<LoginResponse> userLogin(LoginRequest loginRequest);

    ResponseEntity<MessageResponse> fetchAllUsers();

    ResponseEntity<UserResponse> setProfilePic(MultipartFile file);
}
