package com.accountbook.phoenix.Service;

import com.accountbook.phoenix.Configuration.Utils;
import com.accountbook.phoenix.DTOResponse.FolowingCount;
import com.accountbook.phoenix.DTOResponse.MessageResponse;
import com.accountbook.phoenix.Entity.FriendRequest;
import com.accountbook.phoenix.Entity.User;
import com.accountbook.phoenix.Exception.InvalidUserException;
import com.accountbook.phoenix.Repository.FriendRequestRepository;
import com.accountbook.phoenix.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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


    public ResponseEntity<?> followUser(int receiverId) {
        try {
            log.info("in the method");
            Optional<User> receiver = userRepository.findById(receiverId);
            if (receiver.isEmpty()) {
                throw new InvalidUserException("user not found");
            }
            User sender = utils.getUser();
            if (sender.getId() == receiver.get().getId()) {
                throw new InvalidUserException("user and friend cannot be the same");
            }
            FriendRequest friendRequest = friendRequestRepository.findBySenderAndReceiver(sender, receiver.get());
            log.info("friendRequest :: " + friendRequest);
            if (friendRequest == null) {
                friendRequest = new FriendRequest();
                friendRequest.setSender(sender);
                friendRequest.setReceiver(receiver.get());
                friendRequest.setFollowing(true);

            } else if (friendRequest != null && friendRequest.isFollowing()) {
                friendRequest.setFollowing(false);
                friendRequestRepository.delete(friendRequest);
                return ResponseEntity.ok("{\n" +
                        "   \"message\": \"Unfollowed user successfully\"\n" +
                        "}");
            }
            friendRequestRepository.save(friendRequest);
            return ResponseEntity.ok("{\n" +
                    "   \"message\": \"Following user successfully\"\n" +
                    "}");
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
        }
    }



    public ResponseEntity<MessageResponse> listOfFriends() {
        try {
            List<FriendRequest> following = friendRequestRepository.findAllBySender(utils.getUser());

            List<FolowingCount> friends = following.stream()
                    .map(user -> new FolowingCount(
                            following.size()
                    )).collect(Collectors.toList());
            return ResponseEntity.ok(new MessageResponse("SuccessFull ", friends));
        } catch (Exception exception) {
            return ResponseEntity.notFound().build();

        }
    }
}

