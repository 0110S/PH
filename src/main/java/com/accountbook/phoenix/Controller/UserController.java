package com.accountbook.phoenix.Controller;

import com.accountbook.phoenix.DTO.*;
import com.accountbook.phoenix.DTOResponse.LoginResponse;
import com.accountbook.phoenix.DTOResponse.MessageResponse;
import com.accountbook.phoenix.DTOResponse.UserResponse;
import com.accountbook.phoenix.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/")
@CrossOrigin("*")
public class UserController {

    private final UserService userService;

    @PostMapping("signUp")
    public ResponseEntity<MessageResponse> userRegistration(@RequestBody UserRequest userRequest) {
        return userService.userRegistration(userRequest);
    }

    @PostMapping("login")
    public ResponseEntity<LoginResponse> userLogin(@RequestBody LoginRequest loginRequest) {
        return userService.userLogin(loginRequest);
    }

    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }

    @GetMapping("allUsers")
    public ResponseEntity<MessageResponse> fetchAllUsers() {
        return userService.fetchAllNonFriends();
    }

    @PostMapping("profilePic")
    public ResponseEntity<MessageResponse> setProfilePic(@RequestParam MultipartFile file) {
        return userService.setProfilePic(file);
    }

    @GetMapping("profile")
    public ResponseEntity<?> getProfile(){
        return userService.profile();
    }
}
