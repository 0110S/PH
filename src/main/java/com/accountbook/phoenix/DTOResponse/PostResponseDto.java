package com.accountbook.phoenix.DTOResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostResponseDto {
    private int postId;
    private String post;
    private boolean like;
    private int likeCount;
    private LocalDateTime time;
    private int commentCount;
}
