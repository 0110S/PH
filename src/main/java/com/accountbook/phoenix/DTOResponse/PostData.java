package com.accountbook.phoenix.DTOResponse;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostData {
    private String post;
    private LocalDateTime localDateTime;
    private String firstname;
    private String lastName;
    private String userName;
    private String email;
    private String mobileNumber;
    private boolean like;
    private int likeCount;
    private long  comments;
}
