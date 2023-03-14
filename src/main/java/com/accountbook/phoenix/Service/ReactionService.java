package com.accountbook.phoenix.Service;

import com.accountbook.phoenix.Configuration.Utils;
import com.accountbook.phoenix.DTOResponse.MessageResponse;
import com.accountbook.phoenix.Entity.Comment;
import com.accountbook.phoenix.Entity.Post;
import com.accountbook.phoenix.Entity.Reaction;
import com.accountbook.phoenix.Entity.User;
import com.accountbook.phoenix.Exception.InvalidUserException;
import com.accountbook.phoenix.Exception.PostNotFoundException;
import com.accountbook.phoenix.Repository.CommentRepository;
import com.accountbook.phoenix.Repository.PostRepository;
import com.accountbook.phoenix.Repository.ReactionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class ReactionService {

    private final Utils utils;

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    private final ReactionRepository reactionRepository;

    public ResponseEntity<?> togLike(int refId) {

        try {
            User user = utils.getUser();
            if (user == null) {
                throw new InvalidUserException("user not found");
            }

            Optional<Post> post = postRepository.findById(refId);

            if (post.isEmpty()) {
                throw new PostNotFoundException("post not found ");
            }
            log.info("post :" + post.get());
            List<Comment> comment = commentRepository.findAllByPostId(refId);
            log.info("comment" + comment);
            List<ObjectNode> allComments = comment.stream()
                    .map(comments -> {
                        ObjectNode userNode = new ObjectMapper().createObjectNode();
                        userNode.put("comments", comments.getComment());
                        return userNode;
                    }).collect(Collectors.toList());

            Optional<Reaction> reaction = Optional.ofNullable(reactionRepository.findByRefIdAndUser(refId, user));

            log.info("reaction" + reaction);
            // If post is already liked and the user wants to like it again, make it unlike
            if (reaction.isPresent() && reaction.get().isLike()) {
                post.get().setLikeCount(Math.max(0, post.get().getLikeCount() - 1));
                postRepository.save(post.get());
                reactionRepository.delete(reaction.get());
                return ResponseEntity.ok("{\n" +
                        "   \"message\": \"Dislike  successfully\"\n" +
                        "}");
            } else if (!reaction.isPresent()) {
                Reaction newReaction = new Reaction();
                newReaction.setLike(true);
                newReaction.setRefId(refId);
                newReaction.setLikedTime(LocalDateTime.now());
                newReaction.setRefType("post");
                newReaction.setUser(user);
                post.get().incrementLikeCount();
                postRepository.save(post.get());
                reactionRepository.save(newReaction);
                return ResponseEntity.ok("{\n" +
                        "   \"message\": \"Liked  successfully\"\n" +
                        "}");
            }
        } catch (InvalidUserException | PostNotFoundException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage(), null));
        }
        return ResponseEntity.notFound().build();
    }
}
