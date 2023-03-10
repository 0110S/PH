package com.accountbook.phoenix.Service;

import com.accountbook.phoenix.Configuration.Utils;
import com.accountbook.phoenix.DTO.PostRequest;
import com.accountbook.phoenix.DTOResponse.MessageResponse;
import com.accountbook.phoenix.DTOResponse.PostResponseDto;
import com.accountbook.phoenix.DTOResponse.UserResponseDto;
import com.accountbook.phoenix.Entity.*;
import com.accountbook.phoenix.Exception.InvalidUserException;
import com.accountbook.phoenix.Exception.PostNotFoundException;
import com.accountbook.phoenix.Repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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
    private final ReactionRepository reactionRepository;

    private final UserRepository userRepository;
    private final FriendRequestRepository friendRequestRepository;

    @Override
    public ResponseEntity<MessageResponse> postSomething(PostRequest postRequest) {
        try {
            User user = utils.getUser();
            if (user.getId() == 0) {
                throw new InvalidUserException("user not valid ");
            }
            Post post = new Post();
            post.setPost(postRequest.getPost());
            post.setLocalDateTime(LocalDateTime.now());
            post.setUser(user);
            postRepository.save(post);

            UserResponseDto userResponseDto = new UserResponseDto();
            userResponseDto.setUserId(post.getUser().getId());
            userResponseDto.setFirstName(post.getUser().getFirstName());
            userResponseDto.setLastName(post.getUser().getLastName());
            userResponseDto.setUserName(post.getUser().getUsername());
            userResponseDto.setProfilePic(String.valueOf(post.getUser().getProfilePic()));
            PostResponseDto postResponseDto = new PostResponseDto();
            postResponseDto.setPostId(post.getId());
            postResponseDto.setPost(post.getPost());
            postResponseDto.setTime(post.getLocalDateTime());
            userResponseDto.setPost(postResponseDto);
            log.info(" " + post);
            return ResponseEntity.ok(new MessageResponse("SuccessFull", userResponseDto));
        } catch (InvalidUserException exception) {
            return ResponseEntity.badRequest().body(new MessageResponse(exception.getMessage(), null));
        }
    }

    @Override
    public ResponseEntity<MessageResponse> deletePost(int id) {
        try {
            Optional<Post> post = postRepository.findById(id);
            if (post.isEmpty()) {
                throw new PostNotFoundException("post Not found ");
            }
            User user = utils.getUser();
            if (post.get().getUser().getId() != user.getId()) {
                throw new InvalidUserException("user has no authority to delete post");
            }

            PostResponseDto postResponseDto = new PostResponseDto();
            postResponseDto.setPostId(post.get().getId());
            postResponseDto.setPost(post.get().getPost());
            postResponseDto.setTime(post.get().getLocalDateTime());

            postRepository.delete(post.get());
            return ResponseEntity.ok(new MessageResponse("SuccessFull", postResponseDto));
        } catch (PostNotFoundException | InvalidUserException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage(), null));
        }
    }

//    public ResponseEntity<MessageResponse> likePost(int postId) {
//        try {
//            log.info("In the method");
//            log.info(postId + " :id");
//            Optional<Post> post = postRepository.findById(postId);
//            log.info("post " + post);
//            if (!post.isPresent()) {
//                throw new PostNotFoundException("Post not found");
//            }
//
//            User user = utils.getUser();
//            if (user.getId() == 0) {
//                throw new InvalidUserException("User not found");
//            }
//
//            List<Comment> comment = commentRepository.findAllByPostId(postId);
//            List<ObjectNode> allComments = comment.stream()
//                    .map(comments -> {
//                        ObjectNode userNode = new ObjectMapper().createObjectNode();
//                        userNode.put("comments", comments.getComment());
//                        return userNode;
//                    }).collect(Collectors.toList());
//
//            boolean isAlreadyLiked = post.get().isLike();
//            boolean isLiking = true;
//            UserResponseDto userResponseDto = new UserResponseDto();
//            userResponseDto.setUserId(post.get().getUser().getId());
//            userResponseDto.setFirstName(post.get().getUser().getFirstName());
//            userResponseDto.setUserName(post.get().getUser().getUsername());
//            userResponseDto.setLastName(post.get().getUser().getLastName());
//            userResponseDto.setProfilePic(String.valueOf(post.get().getUser().getProfilePic()));
//            // If post is already liked and the user wants to like it again, make it unlike
//            if (isAlreadyLiked && isLiking) {
//                log.info("Disliking post...");
//                post.get().setLike(false);
//                post.get().setLikeCount(Math.max(0, post.get().getLikeCount() - 1));
//                post.get().setReactedUserId(0);
//                postRepository.save(post.get());
//                PostResponseDto postResponseDto = new PostResponseDto();
//                postResponseDto.setPostId(post.get().getId());
//                postResponseDto.setPost(post.get().getPost());
//                postResponseDto.setLike(post.get().isLike());
//                postResponseDto.setLikeCount(post.get().getLikeCount());
//                postResponseDto.setTime(post.get().getLocalDateTime());
//                postResponseDto.setCommentCount((int) allComments.stream().count());
//                userResponseDto.setPost(postResponseDto);
//                return ResponseEntity.ok(new MessageResponse("UnLiked Successfully", userResponseDto));
//            }
//
//            // If post is not liked like it
//            if (!isAlreadyLiked && isLiking) {
//                log.info("Liking post...");
//                post.get().setLike(true);
//                post.get().setLikeCount(post.get().getLikeCount() + 1);
//                post.get().setReactedUserId(user.getId());
//                postRepository.save(post.get());
//                PostResponseDto postResponseDto = new PostResponseDto();
//                postResponseDto.setPostId(post.get().getId());
//                postResponseDto.setPost(post.get().getPost());
//                postResponseDto.setLike(post.get().isLike());
//                postResponseDto.setLikeCount(post.get().getLikeCount());
//                postResponseDto.setTime(post.get().getLocalDateTime());
//                postResponseDto.setCommentCount((int) allComments.stream().count());
//                userResponseDto.setPost(postResponseDto);
//                return ResponseEntity.ok(new MessageResponse("Liked Successfully", userResponseDto));
//            }
//
//        } catch (PostNotFoundException | InvalidUserException e) {
//            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage(), null));
//        }
//        return ResponseEntity.notFound().build();
//    }

    @Override
    public ResponseEntity<MessageResponse> fetchPostById(int id) {
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
            Reaction reaction = reactionRepository.findByRefIdAndUser(id, post.get().getUser());
            log.info("before reaction");
            log.info("before all comments");
            List<Comment> comment = commentRepository.findAllByPostId(id);
            log.info("after all comments");

            UserResponseDto userResponseDto = new UserResponseDto();
            userResponseDto.setUserId(post.get().getUser().getId());
            userResponseDto.setFirstName(post.get().getUser().getFirstName());
            userResponseDto.setLastName(post.get().getUser().getLastName());
            userResponseDto.setUserName(post.get().getUser().getUsername());
            userResponseDto.setProfilePic(String.valueOf(post.get().getUser().getProfilePic()));

            PostResponseDto postResponseDto = new PostResponseDto();
            postResponseDto.setPostId(post.get().getId());
            postResponseDto.setPost(post.get().getPost());
            postResponseDto.setLikeCount(post.get().getLikeCount());
            postResponseDto.setLike(reaction.isLike());
            postResponseDto.setCommentCount((int) comment.stream().count());
            postResponseDto.setTime(post.get().getLocalDateTime());
            userResponseDto.setPost(postResponseDto);

            return ResponseEntity.ok(new MessageResponse("Successfully", userResponseDto));
        } catch (InvalidUserException | PostNotFoundException exception) {

            return ResponseEntity.badRequest().body(new MessageResponse(exception.getMessage(), null));
        }
    }

    @Override
    public ResponseEntity<MessageResponse> getAllPosts() {
        try {
            Optional<User> user = userRepository.findById(utils.getUser().getId());
            if (user.isEmpty()) {
                throw new InvalidUserException("user not found ");
            }


            List<Post> posts = postRepository.findAllPostsByUser(user.get());
            log.info("posts" + posts);
            List<PostResponseDto> postResponseDtos = posts.stream().map(post -> {
                List<Comment> comment = commentRepository.findAllByPostId(post.getId());
                log.info("comment " + comment);
                Reaction reaction = reactionRepository.findByRefIdAndUser(post.getId(), user.get());
                log.info("reaction    " + reaction);
                PostResponseDto postResponseDto = new PostResponseDto();
                postResponseDto.setPostId(post.getId());
                postResponseDto.setPost(post.getPost());
                postResponseDto.setTime(post.getLocalDateTime());
                boolean isLike = false;
                if (reaction != null) {
                    isLike = reaction.isLike();
                }
                postResponseDto.setLike(isLike);
                LocalDateTime time = LocalDateTime.parse("");
                if (reaction != null) {
                    time = reaction.getLikedTime();
                }
                postResponseDto.setLikedTime(time);
                postResponseDto.setLikeCount(post.getLikeCount());
                postResponseDto.setCommentCount((int) comment.stream().count());
                return postResponseDto;
            }).collect(Collectors.toList());
            return ResponseEntity.ok(new MessageResponse("Successfully", postResponseDtos));
        } catch (InvalidUserException exception) {
            return ResponseEntity.badRequest().body(new MessageResponse(exception.getMessage(), null));
        }
    }

    public ResponseEntity<MessageResponse> getAllFriendsPost() {
        try {
            Optional<User> existingUser = userRepository.findById(utils.getUser().getId());

            if (!existingUser.isPresent()) {
                throw new InvalidUserException("user not found ");
            }

            List<FriendRequest> friendships = friendRequestRepository.findAllBySender(existingUser.get());
            log.info("friendships" + friendships);
            List<User> friends = new ArrayList<>();
            for (FriendRequest friendship : friendships) {
                if (friendship.isFollowing()) {
                    friends.add(friendship.getReceiver());
                }
            }

            log.info("friends" + friends);
            List<Post> posts = new ArrayList<>();
            for (User friend : friends) {
                List<Post> friendPosts = postRepository.findAllPostsByUser(friend);
                posts.addAll(friendPosts);
            }
            log.info("posts" + posts);
            List<UserResponseDto> userResponse = posts.stream()
                    .map(post -> {
                        List<Comment> comment = commentRepository.findAllByPostId(post.getId());
                        log.info("comment " + comment.stream().count());

                        log.info("postId  :" + post.getId());
                        Reaction reaction = reactionRepository.findByRefIdAndUser(post.getId(), post.getUser());

                        log.info("reaction" + reaction);

                        UserResponseDto userResponseDto = new UserResponseDto();
                        userResponseDto.setUserId(post.getUser().getId());
                        userResponseDto.setFirstName(post.getUser().getFirstName());
                        userResponseDto.setLastName(post.getUser().getLastName());
                        userResponseDto.setUserName(post.getUser().getUsername());
                        userResponseDto.setProfilePic(String.valueOf(post.getUser().getProfilePic()));
                        PostResponseDto postResponseDto = new PostResponseDto();
                        postResponseDto.setPostId(post.getId());
                        postResponseDto.setPost(post.getPost());
                        boolean isLike = false;
                        if (reaction != null) {
                            isLike = reaction.isLike();
                        }
                        postResponseDto.setLike(isLike);
                        postResponseDto.setTime(post.getLocalDateTime());
                        postResponseDto.setLikeCount(post.getLikeCount());
                        postResponseDto.setCommentCount((int) comment.stream().count());
                        userResponseDto.setPost(postResponseDto);
                        return userResponseDto;
                    })
                    .collect(Collectors.toList());
            return ResponseEntity.ok(new MessageResponse("successful", userResponse));
        } catch (Exception exception) {
            return ResponseEntity.notFound().build();
        }
    }
}