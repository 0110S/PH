package com.accountbook.phoenix.Service;

import com.accountbook.phoenix.Configuration.Utils;
import com.accountbook.phoenix.DTO.PostRequest;
import com.accountbook.phoenix.DTO.UserData;
import com.accountbook.phoenix.DTOResponse.*;
import com.accountbook.phoenix.Entity.Comment;
import com.accountbook.phoenix.Entity.FriendRequest;
import com.accountbook.phoenix.Entity.Post;
import com.accountbook.phoenix.Entity.User;
import com.accountbook.phoenix.Exception.InvalidUserException;
import com.accountbook.phoenix.Exception.PostNotFoundException;
import com.accountbook.phoenix.Repository.CommentRepository;
import com.accountbook.phoenix.Repository.FriendRequestRepository;
import com.accountbook.phoenix.Repository.PostRepository;
import com.accountbook.phoenix.Repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostServiceImp implements PostService {

    private final PostRepository postRepository;

    private final CommentRepository commentRepository;

    private final Utils utils;

    private final UserRepository userRepository;
    private final FriendRequestRepository friendRequestRepository;

    @Override
    public ResponseEntity<PostResponse> postSomething(PostRequest postRequest) {
        try {
            User user = utils.getUser();
            if (user.getId() == 0) {
                throw new InvalidUserException("user not valid ");
            }
            Post post = new Post();
            post.setPost(postRequest.getPost());
            post.setLocalDateTime(LocalDateTime.now());
            post.setUser(user);
            post.setPostCount(+1);

            UserData userData = new UserData();
            userData.setFirstName(post.getUser().getFirstName());
            userData.setLastName(post.getUser().getLastName());
            userData.setMobileNumber(post.getUser().getMobileNumber());
            userData.setEmail(post.getUser().getEmail());
            userData.setUserName(post.getUser().getUsername());

            PostResponse postResponse = new PostResponse();
            postResponse.setStatus(post.getPost());
            postResponse.setStatus("posted Successfully ");
            postResponse.setPost((post.getPost()));
            postResponse.setUserData(userData);

            postRepository.save(post);
            log.info(" " + post);
            return ResponseEntity.ok(postResponse);
        } catch (InvalidUserException exception) {
            PostResponse postResponse = new PostResponse();
            postResponse.setStatus(" error ");
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<?> deletePost(int id) {
        try {
            Optional<Post> existingPost = postRepository.findById(id);
            if (existingPost.isEmpty()) {
                throw new PostNotFoundException("post Not found ");
            }

            User user = utils.getUser();
            if (existingPost.get().getUser().getId() != user.getId()) {
                throw new InvalidUserException("user has no authority to delete post");
            }
            List<Comment> comment = commentRepository.findAllByRefId(id);
            List<ObjectNode> allComments = comment.stream()
                    .filter(comment1 -> comment1.getRefType().equals("post"))
                    .map(comments -> {
                        ObjectNode userNode = new ObjectMapper().createObjectNode();
                        userNode.put("comments", comments.getComment());
                        return userNode;
                    }).collect(Collectors.toList());

            postRepository.delete(existingPost.get());
            log.info("working");
            List<Post> posts = postRepository.findAllPostsByUser(existingPost.get().getUser());

            List<ObjectNode> allPosts = posts.stream().map(post -> {
                ObjectNode userNode = new ObjectMapper().createObjectNode();
                userNode.put("message", "posts ");
                userNode.put("postId", post.getId());
                userNode.put("localDate", String.valueOf(post.getLocalDateTime()));
                userNode.put("post", post.getPost());
                userNode.put("firstName", post.getUser().getFirstName());
                userNode.put("lastName", post.getUser().getLastName());
                userNode.put("userName", post.getUser().getUsername());
                userNode.put("email", post.getUser().getEmail());
                userNode.put("like", post.isLike());
                userNode.put("profilePic", String.valueOf(post.getUser().getProfilePic()));
                userNode.put("mobileNumber", post.getUser().getMobileNumber());
                userNode.put("likeCount", post.getLikeCount());
                userNode.put("commentCount", allComments.stream().count());
                return userNode;
            }).collect(Collectors.toList());
            return ResponseEntity.ok(allPosts);
        } catch (PostNotFoundException | InvalidUserException e) {
            PostResponse postResponse = new PostResponse();
            postResponse.setStatus("post not found");
            return ResponseEntity.badRequest().body(postResponse);
        }
    }

    public ResponseEntity<?> likePost(int postId) {
        try {
            log.info("In the method");
            log.info(postId + " :id");
            Optional<Post> existingPost = postRepository.findById(postId);
            log.info("post " + existingPost);
            if (!existingPost.isPresent()) {
                throw new PostNotFoundException("Post not found");
            }

            User user = utils.getUser();
            if (user.getId() == 0) {
                throw new InvalidUserException("User not found");
            }

            List<Comment> comment = commentRepository.findAllByRefId(postId);
            List<ObjectNode> allComments = comment.stream()
                    .filter(comment1 -> comment1.getRefType().equals("post"))
                    .map(comments -> {
                        ObjectNode userNode = new ObjectMapper().createObjectNode();
                        userNode.put("comments", comments.getComment());
                        return userNode;
                    }).collect(Collectors.toList());


            boolean isAlreadyLiked = existingPost.get().isLike();
            boolean isLiking = true;

            // If post is already liked and the user wants to like it again, make it unlike
            if (isAlreadyLiked && isLiking) {
                log.info("Disliking post...");
                existingPost.get().setLike(false);
                existingPost.get().setLikeCount(Math.max(0, existingPost.get().getLikeCount() - 1));
                existingPost.get().setReactedUserId(0);
                postRepository.save(existingPost.get());
                List<Post> posts = postRepository.findAllPostsByUser(existingPost.get().getUser());
                List<ObjectNode> allPosts = posts.stream().map(post -> {
                    ObjectNode userNode = new ObjectMapper().createObjectNode();
                    userNode.put("message", "posts ");
                    userNode.put("postId", post.getId());
                    userNode.put("localDate", String.valueOf(post.getLocalDateTime()));
                    userNode.put("post", post.getPost());
                    userNode.put("firstName", post.getUser().getFirstName());
                    userNode.put("lastName", post.getUser().getLastName());
                    userNode.put("userName", post.getUser().getUsername());
                    userNode.put("email", post.getUser().getEmail());
                    userNode.put("like", post.isLike());
                    userNode.put("profilePic", String.valueOf(post.getUser().getProfilePic()));
                    userNode.put("mobileNumber", post.getUser().getMobileNumber());
                    userNode.put("likeCount", post.getLikeCount());
                    userNode.put("commentCount", allComments.stream().count());
                    return userNode;
                }).collect(Collectors.toList());
                return ResponseEntity.ok(allPosts);
            }

            // If post is not liked like it
            if (!isAlreadyLiked && isLiking) {
                log.info("Liking post...");
                existingPost.get().setLike(true);
                existingPost.get().setLikeCount(existingPost.get().getLikeCount() + 1);
                existingPost.get().setReactedUserId(user.getId());
                postRepository.save(existingPost.get());
                List<Post> posts = postRepository.findAllPostsByUser(existingPost.get().getUser());
                log.info("posts bigilu " + posts);
                List<ObjectNode> allPosts = posts.stream().map(post -> {
                    ObjectNode userNode = new ObjectMapper().createObjectNode();
                    userNode.put("message", "posts ");
                    userNode.put("postId", post.getId());
                    userNode.put("localDate", String.valueOf(post.getLocalDateTime()));
                    userNode.put("post", post.getPost());
                    userNode.put("firstName", post.getUser().getFirstName());
                    userNode.put("lastName", post.getUser().getLastName());
                    userNode.put("userName", post.getUser().getUsername());
                    userNode.put("email", post.getUser().getEmail());
                    userNode.put("profilePic", String.valueOf(post.getUser().getProfilePic()));
                    userNode.put("like", post.isLike());
                    userNode.put("mobileNumber", post.getUser().getMobileNumber());
                    userNode.put("likeCount", post.getLikeCount());
                    userNode.put("commentCount", allComments.stream().count());
                    return userNode;
                }).collect(Collectors.toList());
                return ResponseEntity.ok(allPosts);
            }
        } catch (PostNotFoundException | InvalidUserException e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
        return null;
    }

    @Override
    public ResponseEntity<?> fetchPostById(int id) {
        try {
            log.info("in the method");
            if (utils.getUser().getId() == 0) {
                throw new InvalidUserException("user is invalid");
            }

            Optional<Post> post = postRepository.findById(id);
            log.info("in the posts");
            if (post.isEmpty()) {
                log.warn("no post");
                throw new PostNotFoundException("post not found");
            }
            if (post.get().getUser().getId() != utils.getUser().getId()) {
                log.warn("user not found");
                throw new InvalidUserException("user not found");
            }
            log.info("before reaction");
            log.info("before all comments");
            List<Comment> comment = commentRepository.findAllByRefId(id);
            log.info("after all comments");
            List<ObjectNode> allComments = comment.stream()
                    .filter(comment1 -> comment1.getRefType().equals("post"))
                    .map(comments -> {
                        ObjectNode userNode = new ObjectMapper().createObjectNode();
                        userNode.put("comments", comments.getComment());
                        return userNode;
                    }).collect(Collectors.toList());

            NotificationResponse notificationResponse = new NotificationResponse();
            notificationResponse.setMessage("post found with  details");
            PostData userData = new PostData();
            userData.setLocalDateTime(post.get().getLocalDateTime());
            userData.setPost(post.get().getPost());
            userData.setFirstname(post.get().getUser().getFirstName());
            userData.setLastName(post.get().getUser().getLastName());
            userData.setMobileNumber(post.get().getUser().getMobileNumber());
            userData.setEmail(post.get().getUser().getEmail());
            userData.setUserName(post.get().getUser().getUsername());
            userData.setLike(post.get().isLike());

            userData.setLikeCount(post.get().getLikeCount());
            userData.setComments(allComments.stream().count());
            notificationResponse.setPost(userData);

            return ResponseEntity.ok(notificationResponse);
        } catch (InvalidUserException exception) {
            PostResponse postResponse = new PostResponse();
            postResponse.setStatus("user not found");
            return ResponseEntity.badRequest().body(postResponse);
        } catch (PostNotFoundException userException) {
            PostResponse postResponse = new PostResponse();
            postResponse.setStatus("post not found");
            return ResponseEntity.badRequest().body(postResponse);
        }
    }

    @Override
    public ResponseEntity<String> getAllPosts() throws JsonProcessingException {
        try {
            Optional<User> user = userRepository.findById(utils.getUser().getId());
            if (user.isEmpty()) {
                throw new InvalidUserException("user not found ");
            }

            List<Post> posts = postRepository.findAllPostsByUser(user.get());

            PResponse p = new PResponse();
            List<ObjectNode> userData = posts.stream().map(post -> {
                ObjectNode userNode = new ObjectMapper().createObjectNode();
                userNode.put("post", post.getPost());
                userNode.put("postId", post.getId());
                userNode.put("createdTime", String.valueOf(post.getLocalDateTime()));
                userNode.put("like", post.isLike());
                userNode.put("likeCount", post.getLikeCount());
                int commentCount = 0;
                if (post.getComment() != null) {
                    commentCount = post.getComment().getCommentCount();
                }
                userNode.put("commentCount", commentCount);
                return userNode;
            }).collect(Collectors.toList());
            p.setUserData(userData);
            p.setMessage("posts  ");
            ObjectMapper mapper = new ObjectMapper();
            String postData = mapper.writeValueAsString(p);
            return ResponseEntity.ok().body(postData);
        } catch (InvalidUserException exception) {
            PResponse postResponse = new PResponse();
            postResponse.setMessage("error");
            ObjectMapper mapper = new ObjectMapper();
            String responseJson = mapper.writeValueAsString(postResponse);
            return ResponseEntity.badRequest().body(responseJson);
        } catch (JsonProcessingException e) {
            log.error("Error converting response to JSON", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public ResponseEntity<MessageResponse> getAllFriendsPost() {
        try {
            Optional<User> existingUser = userRepository.findById(utils.getUser().getId());
            if (!existingUser.isPresent()) {
                throw new InvalidUserException("user not found ");
            }
            List<FriendRequest> friendships = friendRequestRepository.findBySender(existingUser.get());
            List<User> friends = friendships.stream()
                    .filter(friendship -> friendship.isFollowing())
                    .map(friendship -> friendship.getReceiver())
                    .collect(Collectors.toList());
            List<Post> posts = new ArrayList<>();
            for (User friend : friends) {
                List<Post> friendPosts = postRepository.findAllPostsByUser(friend);
                posts.addAll(friendPosts);
            }

            List<ObjectNode> postData = posts.stream().map(post -> {
                ObjectNode postNode = new ObjectMapper().createObjectNode();
                postNode.put("post", post.getPost());
                postNode.put("like", post.isLike());
                postNode.put("lieCount", post.getLikeCount());
                postNode.put("time", String.valueOf(post.getLocalDateTime()));
                return postNode;
            }).collect(Collectors.toList());

            List<ObjectNode> userData = posts.stream().map(post -> {
                ObjectNode userNode = new ObjectMapper().createObjectNode();
                userNode.put("firstName", post.getUser().getFirstName());
                userNode.put("lastName", post.getUser().getEmail());
                userNode.put("post", (BigDecimal) postData);
                return userNode;
            }).collect(Collectors.toList());
            return ResponseEntity.ok(new MessageResponse("successful", userData));
        } catch (Exception exception) {
            return ResponseEntity.notFound().build();
        }
    }
}