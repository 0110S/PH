package com.accountbook.phoenix.DTOResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostResponseDto {

    private int postId;
    private String post;
    private LocalDateTime time;
    private  long likeCount;
    private  boolean like;
    private  long commentCount;
    private LocalDateTime likedTime;


}
