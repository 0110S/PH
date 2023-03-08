package com.accountbook.phoenix.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Builder
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String post;
    private LocalDateTime localDateTime;
    private boolean like = false;
    private boolean dislike = false;
    private int likeCount = 0;
    private int dislikeCount = 0;
    private int postCount = 0;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_post")
    private User user;

    @JoinColumn (name = "user")
    private int reactedUserId;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "comment", nullable = true, referencedColumnName = "id")
    private Comment comment;

}
