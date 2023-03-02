package com.accountbook.phoenix.Service;

import com.accountbook.phoenix.Configuration.JwtService;
import com.accountbook.phoenix.DTO.*;
import com.accountbook.phoenix.Entity.User;
import com.accountbook.phoenix.Exception.InvalidUserException;
import com.accountbook.phoenix.Exception.UserFoundException;
import com.accountbook.phoenix.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
    public ResponseEntity<RegistrationResponse> userRegistration(UserRequest userRequest) {
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
            UserData userData = new UserData();
            userData.setFirstName(newUser.getFirstName());
            userData.setLastName(newUser.getLastName());
            userData.setUserName(newUser.getUsername());
            userData.setEmail(newUser.getEmail());
            userData.setMobileNumber(newUser.getMobileNumber());

            RegistrationResponse userDtoResponse = new RegistrationResponse();
            userDtoResponse.setMessage("user signed successfully ");
            userDtoResponse.setResponse(userData);

            return ResponseEntity.ok(userDtoResponse);
        } catch (UserFoundException exception) {
            RegistrationResponse userDtoResponse = new RegistrationResponse();
            userDtoResponse.setMessage("user already exists");
            return ResponseEntity.badRequest().body(userDtoResponse);
        }
    }

    @Override
    public ResponseEntity<LoginResponse> userLogin(LoginRequest loginRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(), loginRequest.getPassword()
                    )
            );
            User user = (User) userRepository.findByEmail(loginRequest.getEmail());

            if (user == null) {
                throw new InvalidUserException("user not found");
            }
            String accessToken = jwtService.generateAccessToken(user);

            UserData userData = new UserData();
            userData.setFirstName(user.getFirstName());
            userData.setLastName(user.getLastName());
            userData.setUserName(user.getUsername());
            userData.setEmail(user.getEmail());
            userData.setMobileNumber(user.getMobileNumber());

            LoginResponse userDtoResponse = new LoginResponse();
            userDtoResponse.setMessage("Login Successfully ");
            userDtoResponse.setAccessToken(accessToken);
            userDtoResponse.setUserResponse(userData);
            return ResponseEntity.ok(userDtoResponse);
        } catch (InvalidUserException exception) {
          LoginResponse loginResponse = new LoginResponse();
          loginResponse.setMessage("invalid user ");
            return ResponseEntity.badRequest().body(loginResponse);
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
