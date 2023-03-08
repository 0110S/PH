package com.accountbook.phoenix.Service;

import com.accountbook.phoenix.Configuration.Utils;
import com.accountbook.phoenix.DTO.FriendRequestDto;
import com.accountbook.phoenix.DTO.UserData;
import com.accountbook.phoenix.DTOResponse.MessageResponse;
import com.accountbook.phoenix.DTOResponse.UserResponse;
import com.accountbook.phoenix.Entity.FriendRequest;
import com.accountbook.phoenix.Entity.User;
import com.accountbook.phoenix.Exception.DuplicateFollowException;
import com.accountbook.phoenix.Exception.InvalidUserException;
import com.accountbook.phoenix.Repository.FriendRequestRepository;
import com.accountbook.phoenix.Repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FriendRequestService {

    private final FriendRequestRepository friendRequestRepository;

    private final UserRepository userRepository;

    private final Utils utils;


    public ResponseEntity<UserResponse> addFriend(FriendRequestDto friendRequestDto) {
        try {
            Optional<User> user = userRepository.findById(friendRequestDto.getUserId());
            if (user.isEmpty()) {
                throw new InvalidUserException("user not found");
            }

            if (utils.getUser().getId() == user.get().getId()) {
                throw new InvalidUserException("user and friend cannot be the same");
            }

            Optional<FriendRequest> existingFriendRequest = friendRequestRepository.findByUserAndFriend(user.get(), utils.getUser());
            boolean isFollowing = existingFriendRequest.isPresent() && existingFriendRequest.get().isFollowing();
            boolean isUnfollowing = existingFriendRequest.isPresent() && !existingFriendRequest.get().isFollowing();

            if (friendRequestDto.isFollow() && isFollowing) {
                throw new DuplicateFollowException("already following");
            } else if (!friendRequestDto.isFollow() && isUnfollowing) {
                throw new InvalidUserException("already unfollowing");
            }

            UserResponse userResponse = new UserResponse();
            UserData userData = new UserData();
            userData.setUserName(user.get().getUsername());
            userData.setFirstName(user.get().getFirstName());
            userData.setLastName(user.get().getLastName());
            userData.setEmail(user.get().getEmail());
            userData.setMobileNumber(user.get().getMobileNumber());

            if (friendRequestDto.isFollow()) {
                user.get().setFollowing(+1);
                userData.setFollow(true);
                userResponse.setMessage("following");
            } else {
                user.get().setFollowing(-1);
                userData.setFollow(false);
                userResponse.setMessage("unfollowed");
            }

            userRepository.save(user.get());

            if (existingFriendRequest.isPresent()) {
                existingFriendRequest.get().setFollowing(friendRequestDto.isFollow());
            } else {
                FriendRequest friendRequest = new FriendRequest();
                friendRequest.setFollowing(friendRequestDto.isFollow());
                friendRequest.setFriend(user.get());
                friendRequest.setUser(utils.getUser());
                friendRequestRepository.save(friendRequest);
            }

            userResponse.setResponse(userData);
            return ResponseEntity.ok(userResponse);

        } catch (InvalidUserException exception) {
            UserResponse userResponse = new UserResponse();
            userResponse.setMessage(exception.getMessage());
            return ResponseEntity.badRequest().body(userResponse);
        } catch (DuplicateFollowException exception) {
            UserResponse userResponse = new UserResponse();
            userResponse.setMessage(exception.getMessage());
            return ResponseEntity.badRequest().body(userResponse);
        }
    }



    public ResponseEntity<MessageResponse> listOfFriends() {
        try {
            List<FriendRequest> following = friendRequestRepository.findByUser(utils.getUser());

            List<ObjectNode> friendIds = following.stream()
                    .map(user -> {
                        ObjectNode userNode = new ObjectMapper().createObjectNode();
                        userNode.put("firstName", user.getFriend().getFirstName());
                        userNode.put("lastName", user.getFriend().getLastName());
                        userNode.put("email", user.getFriend().getEmail());
                        return userNode;
                    }).collect(Collectors.toList());

            if (friendIds.isEmpty()) {
                return ResponseEntity.ok(new MessageResponse("true", "no followers found"));
            }
            return ResponseEntity.ok(new MessageResponse("true", friendIds));
//            return ResponseEntity.ok(new MessageResponse(true, following));
        } catch (Exception exception) {
            return ResponseEntity.notFound().build();

        }
    }

    public ResponseEntity<MessageResponse> unfriend(FriendRequestDto friendRequestDto) {
        try {
            Optional<User> user = userRepository.findById(friendRequestDto.getUserId());
            if (user.isEmpty()) {
                throw new InvalidUserException("user not found");
            }
            Optional<FriendRequest> friendRequest = friendRequestRepository.findById(friendRequestDto.getUserId());

            if (friendRequest.isEmpty()) {
                throw new InvalidUserException("user not found");
            }
            friendRequestRepository.delete(friendRequest.get());
            return ResponseEntity.ok(new MessageResponse("true", "deleted successfully" + friendRequest));
        } catch (InvalidUserException e) {
            return ResponseEntity.badRequest().body(new MessageResponse("false", "user not found"));
        }
    }
}
