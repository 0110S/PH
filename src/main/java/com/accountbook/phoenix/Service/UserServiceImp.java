package com.accountbook.phoenix.Service;

import com.accountbook.phoenix.Configuration.JwtService;
import com.accountbook.phoenix.DTO.LoginRequest;
import com.accountbook.phoenix.DTO.MessageResponse;
import com.accountbook.phoenix.DTO.ResourceResponse;
import com.accountbook.phoenix.DTO.UserRequest;
import com.accountbook.phoenix.Entity.User;
import com.accountbook.phoenix.Exception.InvalidUserException;
import com.accountbook.phoenix.Exception.UserFoundException;
import com.accountbook.phoenix.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImp implements UserService {

    private final JwtService jwtService;

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    @Autowired
    AuthenticationManager authenticationManager;

    @Override
    public ResponseEntity<MessageResponse> userRegistration(UserRequest userRequest) {
        try {
            User user = (User) userRepository.findByEmail(userRequest.getEmail());

            if (user != null) {
                throw new UserFoundException("user already exists");
            }
            User newUser = User.builder()
                    .firstName(userRequest.getFirstName())
                    .lastName(userRequest.getLastName())
                    .email(userRequest.getEmail())
                    .userName(userRequest.getUserName())
                    .role("USER")
                    .mobileNumber(userRequest.getMobileNumber())
                    .password(passwordEncoder.encode(userRequest.getPassword()))
                    .build();
            userRepository.save(newUser);
            return ResponseEntity.ok(new MessageResponse(true, newUser));
        } catch (UserFoundException exception) {
            return ResponseEntity.badRequest().body(
                    new MessageResponse(false, userRequest.getEmail() + " already exists"));
        }
    }

    @Override
    public ResponseEntity<ResourceResponse> userLogin(LoginRequest loginRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(), loginRequest.getPassword()
                    )
            );
            User user = (User) userRepository.findByEmail(loginRequest.getEmail());

            if (user == null) {
                throw new InvalidUserException("user not found");
            }
            String accessToken = jwtService.generateAccessToken( user);
            String refreshToken = "";
            return ResponseEntity.ok(new ResourceResponse(true, accessToken, refreshToken));
        } catch (InvalidUserException exception) {
            ResourceResponse response = ResourceResponse.builder()
                    .status(false)
                    .accessToken("user not found")
                    .build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    @Override
    public ResponseEntity<MessageResponse> fetchAllUsers() {
        try {
            List<User> users = userRepository.findAll();
            return ResponseEntity.ok(new MessageResponse(true, users));
        } catch (Exception exception) {
            return ResponseEntity.notFound().build();
        }
    }
}
