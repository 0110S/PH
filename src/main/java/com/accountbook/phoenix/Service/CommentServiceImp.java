package com.accountbook.phoenix.Service;

import com.accountbook.phoenix.Configuration.Utils;
import com.accountbook.phoenix.DTO.CommentRequest;
import com.accountbook.phoenix.DTOResponse.CommentResponse;
import com.accountbook.phoenix.DTOResponse.MessageResponse;
import com.accountbook.phoenix.DTOResponse.UserCommentResponseDto;
import com.accountbook.phoenix.Entity.Comment;
import com.accountbook.phoenix.Entity.Post;
import com.accountbook.phoenix.Entity.User;
import com.accountbook.phoenix.Exception.CommentNotFoundException;
import com.accountbook.phoenix.Exception.InvalidUserException;
import com.accountbook.phoenix.Exception.PostNotFoundException;
import com.accountbook.phoenix.Repository.CommentRepository;
import com.accountbook.phoenix.Repository.PostRepository;
import com.accountbook.phoenix.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImp implements CommentService {

    private final CommentRepository commentRepository;

    private final PostRepository postRepository;

    private final UserRepository userRepository;

    private final Utils utils;

    @Override
    public ResponseEntity<MessageResponse> addCommentToPost(CommentRequest commentRequest) {
        try {

            User user = utils.getUser();
            if (user == null) {
                throw new InvalidUserException(" user not found ");
            }

            Optional<Post> post = postRepository.findById(commentRequest.getPostId());
            if (post.isEmpty()) {
                throw new PostNotFoundException(" post not found ");
            }

            Comment comment = Comment.builder()
                    .comment(commentRequest.getComment())
                    .commentCount(+1)
                    .time(LocalDateTime.now())
                    .postId(commentRequest.getPostId())
                    .user(user)
                    .build();
            commentRepository.save(comment);
            CommentResponse commentResponse = new CommentResponse(
                    comment.getId(),
                    comment.getTime(),
                    comment.getComment()
            );

            return ResponseEntity.ok(new MessageResponse("success", commentResponse));
        } catch (PostNotFoundException e) {
            return ResponseEntity.badRequest().body(new MessageResponse("false", commentRequest.getPostId() + " not found"));
        } catch (InvalidUserException e) {
            return ResponseEntity.badRequest().body(new MessageResponse("false", commentRequest + "user not found "));
        }
    }

//
//    @Override
//    public ResponseEntity<MessageResponse> addCommentToComment(CommentRequest commentRequest) {
//        try {
//            Optional<Comment> comment = commentRepository.findById(commentRequest.getPostId());
//            if (comment.isEmpty()) {
//                throw new CommentNotFoundException("comment ot found");
//            }
//            Comment newComment = Comment.builder()
//                    .comment(commentRequest.getComment())
//                    .postId(commentRequest.getPostId())
//                    .refType(commentRequest.getRefType())
//                    .build();
//            commentRepository.save(newComment);
//            return ResponseEntity.ok(new MessageResponse("true", comment));
//        } catch (CommentNotFoundException exception) {
//            return ResponseEntity.badRequest().body(new MessageResponse("false", commentRequest.getRefId() + " not found"));
//        }
//    }

    @Override
    public ResponseEntity<MessageResponse> deleteComment(int postId, int commentId) {
        try {
            Optional<Post> post = postRepository.findById(postId);
            if (post.isEmpty()) {
                throw new PostNotFoundException("post Not found");
            }
            Optional<Comment> comment = commentRepository.findById(commentId);
            if (comment.isEmpty()) {
                throw new CommentNotFoundException("comment not found");
            }
            if (comment.get().getUser().getId() != utils.getUser().getId()) {
                throw new InvalidUserException(" user not found");
            }
            CommentResponse commentResponse = new CommentResponse(
                    comment.get().getId(),
                    comment.get().getTime(),
                    comment.get().getComment()
            );
            commentRepository.delete(comment.get());

            return ResponseEntity.ok(new MessageResponse("true", commentResponse));
        } catch (PostNotFoundException e) {
            return ResponseEntity.badRequest().body(new MessageResponse("false", postId));
        } catch (CommentNotFoundException e) {
            return ResponseEntity.badRequest().body(new MessageResponse("false", commentId));
        } catch (InvalidUserException exception) {
            return ResponseEntity.badRequest().body(new MessageResponse("false", "user not found"));
        }
    }

    @Override
    public ResponseEntity<MessageResponse> getAllComments(int id) {
        try {
            Optional<User> user = userRepository.findById(utils.getUser().getId());
            if (user.isEmpty()) {
                throw new InvalidUserException("user not found ");
            }

            Optional<Post> post = postRepository.findById(id);
            if (post.isEmpty()) {
                throw new PostNotFoundException("post not found ");
            }
            List<Comment> comments = commentRepository.findAllByPostId(id);
            List<UserCommentResponseDto> responseDtos = comments.stream().map(comment -> {
                CommentResponse commentResponse = new CommentResponse(
                        comment.getId(),
                        comment.getTime(),
                        comment.getComment()
                );
                UserCommentResponseDto responseDto = new UserCommentResponseDto(
                        comment.getUser().getId(),
                        comment.getUser().getFirstName(),
                        comment.getUser().getLastName(),
                        comment.getUser().getUsername(),
                        String.valueOf(comment.getUser().getProfilePic()),
                        commentResponse);
                return responseDto;
            }).collect(Collectors.toList());
            return ResponseEntity.ok(new MessageResponse("Successfully", responseDtos));
        } catch (InvalidUserException exception) {
            return ResponseEntity.badRequest().body(new MessageResponse("false", exception.getMessage()));
        } catch (PostNotFoundException exception) {
            return ResponseEntity.badRequest().body(new MessageResponse("false", exception.getMessage()));
        }
    }
}
