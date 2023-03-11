package com.accountbook.phoenix.DTOResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {

    private int userId;
    private String firstName;
    private String lastName;
    private String userName;
    private String profilePic;
    private PostResponseDto post;
}
