package com.accountbook.phoenix.DTOResponse;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileResponse {
    private String firstName;
    private String lastName;
    private String userName;
    private String email;
    private String profilePic;
    private int followerCount;
    private int followingCount;
    private long postCOUNT;
}
