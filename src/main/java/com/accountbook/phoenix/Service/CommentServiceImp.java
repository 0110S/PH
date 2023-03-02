package com.accountbook.phoenix.Service;

import com.accountbook.phoenix.Configuration.Utils;
import com.accountbook.phoenix.DTO.CommentRequest;
import com.accountbook.phoenix.DTO.MessageResponse;
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

import java.util.List;
import java.util.Optional;

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
            Optional<Post> post = postRepository.findById(commentRequest.getRefId());
            if (post.isEmpty()) {
                throw new PostNotFoundException(" post not found ");
            }
            Comment comment = Comment.builder()
                    .comment(commentRequest.getComment())
                    .refId(commentRequest.getRefId())
                    .refType(commentRequest.getRefType())
                    .user(utils.getUser())
                    .build();
            if (comment.getUser() == null) {
                throw new InvalidUserException("user not found");
            }

            commentRepository.save(comment);
            return ResponseEntity.ok(new MessageResponse(true, comment));
        } catch (PostNotFoundException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(false, commentRequest.getRefId() + " not found"));
        } catch (InvalidUserException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(false, commentRequest + "user not found "));
        }
    }


    @Override
    public ResponseEntity<MessageResponse> addCommentToComment(CommentRequest commentRequest) {
        try {
            Optional<Comment> comment = commentRepository.findById(commentRequest.getRefId());
            if (comment.isEmpty()) {
                throw new CommentNotFoundException("comment ot found");
            }
            Comment newComment = Comment.builder()
                    .comment(commentRequest.getComment())
                    .refId(commentRequest.getRefId())
                    .refType(commentRequest.getRefType())
                    .build();
            commentRepository.save(newComment);
            return ResponseEntity.ok(new MessageResponse(true, comment));
        } catch (CommentNotFoundException exception) {
            return ResponseEntity.badRequest().body(new MessageResponse(false, commentRequest.getRefId() + " not found"));
        }
    }

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
            commentRepository.delete(comment.get());
            return ResponseEntity.ok(new MessageResponse(true, comment.get().getId() + "successfully deleted"));
        } catch (PostNotFoundException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(false, postId));
        } catch (CommentNotFoundException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(false, commentId));
        } catch (InvalidUserException exception) {
            return ResponseEntity.badRequest().body(new MessageResponse(false, "user not found"));
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
            List<Comment> comments = commentRepository.findAllByRefIdAndRefType(id,"comment");
            return ResponseEntity.ok(new MessageResponse(true, "response: " + comments));
        } catch (InvalidUserException exception) {
            return ResponseEntity.badRequest().body(new MessageResponse(false, " user not found"));
        } catch (PostNotFoundException exception) {
            return ResponseEntity.badRequest().body(new MessageResponse(false, "post not found "));
        }
    }
}
