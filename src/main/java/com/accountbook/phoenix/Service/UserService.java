package com.accountbook.phoenix.Service;

import com.accountbook.phoenix.DTO.*;
import com.accountbook.phoenix.DTOResponse.LoginResponse;
import com.accountbook.phoenix.DTOResponse.MessageResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    ResponseEntity<MessageResponse> userRegistration(UserRequest userRequest);

    ResponseEntity<LoginResponse> userLogin(LoginRequest loginRequest);

    ResponseEntity<MessageResponse> fetchAllNonFriends();

    ResponseEntity<MessageResponse> setProfilePic(MultipartFile file);

    ResponseEntity<?> profile();
}
