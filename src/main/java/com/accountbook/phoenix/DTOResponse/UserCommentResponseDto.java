package com.accountbook.phoenix.DTOResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCommentResponseDto {

    private  int id;
    private String firstName;
    private String lastName;
    private String profilePic;
    private CommentResponse commentsList;
}
