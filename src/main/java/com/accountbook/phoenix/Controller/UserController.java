package com.accountbook.phoenix.Controller;

import com.accountbook.phoenix.DTO.LoginRequest;
import com.accountbook.phoenix.DTO.MessageResponse;
import com.accountbook.phoenix.DTO.ResourceResponse;
import com.accountbook.phoenix.DTO.UserRequest;
import com.accountbook.phoenix.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/")
public class UserController {

    private  final UserService userService;

    @PostMapping("signUp")
    public ResponseEntity<MessageResponse> userRegistration(@RequestBody UserRequest userRequest){
        return userService.userRegistration(userRequest);
    }

    @PostMapping("login")
    public ResponseEntity<ResourceResponse> userLogin(@RequestBody LoginRequest loginRequest){
        return  userService.userLogin(loginRequest);
    }
    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }

    @GetMapping("allUsers")
    public  ResponseEntity<MessageResponse> fetchAllUsers(){
        return userService.fetchAllUsers();
    }
}
