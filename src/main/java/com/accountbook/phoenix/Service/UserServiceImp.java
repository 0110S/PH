package com.accountbook.phoenix.Service;

import com.accountbook.phoenix.Configuration.JwtService;
import com.accountbook.phoenix.Configuration.Utils;
import com.accountbook.phoenix.DTO.*;
import com.accountbook.phoenix.DTOResponse.LoginResponse;
import com.accountbook.phoenix.DTOResponse.MessageResponse;
import com.accountbook.phoenix.DTOResponse.UserResponse;
import com.accountbook.phoenix.Entity.FriendRequest;
import com.accountbook.phoenix.Entity.User;
import com.accountbook.phoenix.Exception.InvalidUserException;
import com.accountbook.phoenix.Exception.UserFoundException;
import com.accountbook.phoenix.Repository.FriendRequestRepository;
import com.accountbook.phoenix.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImp implements UserService {

    private final JwtService jwtService;

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final Utils utils;

    private final FriendRequestRepository friendRequestRepository;

    @Autowired
    AuthenticationManager authenticationManager;

    @Override
    public ResponseEntity<UserResponse> userRegistration(UserRequest userRequest) {
        try {
            log.info(""+userRequest);
            Optional<User> user = userRepository.findByEmail(userRequest.getEmail());
            if (user.isPresent()) {
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

            UserResponse userDtoResponse = new UserResponse();
            userDtoResponse.setMessage("user signed successfully ");
            userDtoResponse.setResponse(userData);

            return ResponseEntity.ok(userDtoResponse);
        } catch (UserFoundException exception) {
            UserResponse userDtoResponse = new UserResponse();
            userDtoResponse.setMessage("user already exists");
            return ResponseEntity.badRequest().body(userDtoResponse);
        }
    }

    @Override
    public ResponseEntity<LoginResponse> userLogin(LoginRequest loginRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );
        try {
        Optional<User> user = userRepository.findByEmail(loginRequest.getEmail());
        if (!user.isPresent()) {
            throw new InvalidUserException("username or password is not valid ");
        }
            String accessToken = jwtService.generateAccessToken(user.get());

            UserData userData = new UserData();
            userData.setFirstName(user.get().getFirstName());
            userData.setLastName(user.get().getLastName());
            log.info(" user name " + user.get().getUsername());
            userData.setEmail(user.get().getEmail());
            userData.setMobileNumber(user.get().getMobileNumber());

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
            Optional<User> existingUser = userRepository.findById(utils.getUser().getId());
            if (existingUser.isEmpty()) {
                throw new InvalidUserException("user not found");
            }

            List<User> users = userRepository.findAll();
            List<FriendRequest> friends = friendRequestRepository.findByUser(existingUser.get());

            List<User> nonFriends = new ArrayList<>();
            for (User user : users) {
                boolean isFriend = false;
                for (FriendRequest friend : friends) {
                    if (friend.getFriend().getId() == user.getId()) {
                        isFriend = true;
                        break;
                    }
                }
                if (!isFriend && user.getId() != existingUser.get().getId()) {
                    nonFriends.add(user);
                }
            }
            nonFriends
                    .stream()
                    .map(user -> user.getFirstName()+""+user.getLastName());

            UserData userData = new UserData();
            userData.setFirstName(existingUser.get().getFirstName());
            userData.setLastName(existingUser.get().getLastName());
            userData.setUserName(existingUser.get().getUsername());
            userData.setEmail(existingUser.get().getEmail());
            userData.setMobileNumber(existingUser.get().getMobileNumber());

            LoginResponse userDtoResponse = new LoginResponse();
            userDtoResponse.setMessage("Login Successfully ");
//            userDtoResponse.setAccessToken(accessToken);
            userDtoResponse.setUserResponse(userData);
            return ResponseEntity.ok(new MessageResponse(true,nonFriends));
        } catch (InvalidUserException exception) {

            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<UserResponse> setProfilePic(MultipartFile file) {
        try {
            log.info("in the method ");
            Optional<User> user = userRepository.findById(utils.getUser().getId());
            if (user.isEmpty()) {
                log.warn("in the exception");
                throw new InvalidUserException("user not found");
            }
            File saveFile = saveToFile(file);
            log.info("got the file ");
            user.get().setProfilePic(saveFile);
            userRepository.save(user.get());
            UserData userData = new UserData();
            userData.setFirstName(user.get().getFirstName());
            userData.setLastName(user.get().getLastName());
            userData.setEmail(user.get().getEmail());
            userData.setMobileNumber(user.get().getMobileNumber());
            userData.setUserName(user.get().getUsername());
            userData.setProfilePic(user.get().getProfilePic());

            UserResponse response = new UserResponse();
            response.setResponse(userData);
            response.setMessage("Profile pic Updated Successfully");

            return ResponseEntity.ok(response);
        } catch (InvalidUserException exception) {
            UserResponse response = new UserResponse();
            response.setMessage("user not found ");
            return ResponseEntity.badRequest().body(response);
        } catch (IOException e) {
            UserResponse userResponse = new UserResponse();
            userResponse.setMessage("no image to upload");
            return ResponseEntity.badRequest().body(userResponse);
        }
    }

    private File saveToFile(MultipartFile file) throws IOException {
        log.info("in the file ");
        String fileName = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();

        File savedFile = new File("src/main/java/com/accountbook/phoenix/Files/", fileName);

        file.transferTo(savedFile);

        return savedFile;
    }

}
