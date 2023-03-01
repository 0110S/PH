package com.accountbook.phoenix.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReactionRequest {

    private int like = 0;
    private int dislike = 0;
}
