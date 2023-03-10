package com.accountbook.phoenix.DTOResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponse {
    private int id;
    private LocalDateTime time;
    private String comment;
}
