package com.accountbook.phoenix.Service;

import com.accountbook.phoenix.Configuration.JwtService;
import com.accountbook.phoenix.Configuration.Utils;
import com.accountbook.phoenix.DTO.LoginRequest;
import com.accountbook.phoenix.DTO.UserData;
import com.accountbook.phoenix.DTO.UserRequest;
import com.accountbook.phoenix.DTOResponse.LoginResponse;
import com.accountbook.phoenix.DTOResponse.MessageResponse;
import com.accountbook.phoenix.DTOResponse.ProfileResponse;
import com.accountbook.phoenix.DTOResponse.UserResponse;
import com.accountbook.phoenix.Entity.FriendRequest;
import com.accountbook.phoenix.Entity.Post;
import com.accountbook.phoenix.Entity.User;
import com.accountbook.phoenix.Exception.InvalidUserException;
import com.accountbook.phoenix.Exception.UserFoundException;
import com.accountbook.phoenix.Repository.FriendRequestRepository;
import com.accountbook.phoenix.Repository.PostRepository;
import com.accountbook.phoenix.Repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
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
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImp implements UserService {

    private final JwtService jwtService;

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final PostRepository postRepository;

    private final Utils utils;

    private final FriendRequestRepository friendRequestRepository;

    @Autowired
    AuthenticationManager authenticationManager;

    @Override
    public ResponseEntity<UserResponse> userRegistration(UserRequest userRequest) {
        try {
            log.info("" + userRequest);
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


    public ResponseEntity<?> profile() {
        try {
            User user = utils.getUser();
            if (user == null) {
                throw new Exception("");
            }

            List<Post> posts = postRepository.findAllPostsByUser(user);
            ProfileResponse userData = new ProfileResponse();
            userData.setFirstName(user.getFirstName());
            userData.setLastName(user.getLastName());
            userData.setUserName(user.getUsername());
            userData.setEmail(user.getEmail());
            userData.setMobileNumber(user.getMobileNumber());
            userData.setProfilePic(user.getProfilePic());
            userData.setFollowerCount(user.getFollower());
            userData.setFollowingCount(user.getFollowing());
            userData.setPostCOUNT(posts.stream().count());

            return ResponseEntity.ok(userData);
        } catch (Exception e) {
            return null;
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

            System.out.println(" friends "+friends);
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
            System.out.println(""+nonFriends);

            List<ObjectNode> not = nonFriends
                    .stream()
                    .map(user -> {
                        ObjectNode userNode = new ObjectMapper().createObjectNode();
                        userNode.put("userId", user.getId());
                        userNode.put("firstName", user.getFirstName());
                        userNode.put("lastName", user.getLastName());
                        userNode.put("userName", user.getUsername());
                        userNode.put("email", user.getEmail());
                        userNode.put("profilePic", String.valueOf(user.getProfilePic()));
                        userNode.put("following", user.isFollow());
                        return userNode;
                    }).collect(Collectors.toList());
//

            System.out.println(nonFriends);
            LoginResponse userDtoResponse = new LoginResponse();
            return ResponseEntity.ok(new MessageResponse("not friends ", not));
        } catch (InvalidUserException exception) {

            return ResponseEntity.notFound().build();
        }
    }


    public ResponseEntity<?> fetchAllUsersPosts() {
        try {
            Optional<User> existingUser = userRepository.findById(utils.getUser().getId());
            if (existingUser.isEmpty()) {
                throw new InvalidUserException("user not found");
            }

            List<User> users = userRepository.findAll();
            List<FriendRequest> friends = friendRequestRepository.findByUser(existingUser.get());


            List<User> friendUsers = friends.stream()
                    .flatMap(fr -> Stream.of(fr.getUser(), fr.getFriend()))
                    .distinct()
                    .collect(Collectors.toList());

            List<Post> friendPosts = friendUsers.stream()
                    .map(u -> postRepository.findByUser(u))
                    .collect(Collectors.toList());

            friendPosts.stream()
                    .map(post -> {
                        ObjectNode userNode = new ObjectMapper().createObjectNode();
                        userNode.put("postId", post.getId());
                        userNode.put("localDate", String.valueOf(post.getLocalDateTime()));
                        userNode.put("firstName", post.getUser().getFirstName());
                        userNode.put("lastName", post.getUser().getLastName());
                        userNode.put("email", post.getUser().getEmail());
                        userNode.put("userName", post.getUser().getUsername());
                        userNode.put("mobileNumber", post.getUser().getMobileNumber());
                        userNode.put("profilePic", String.valueOf(post.getUser().getProfilePic()));
                        userNode.put("like", post.isLike());
                        userNode.put("likeCount", post.getLikeCount());
                        int commentCount = 0;
                        if (post.getComment() != null) {
                            commentCount = post.getComment().getCommentCount();
                        }
                        userNode.put("commentCount", commentCount);
                        return userNode;
                    }).collect(Collectors.toList());


            LoginResponse userDtoResponse = new LoginResponse();
            userDtoResponse.setMessage("Login Successfully ");
            return ResponseEntity.ok(new MessageResponse("post found", friendPosts));
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
