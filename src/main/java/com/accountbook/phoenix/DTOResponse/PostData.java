package com.accountbook.phoenix.DTOResponse;

import com.accountbook.phoenix.DTO.UserData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostData {
    private  String post;
    private UserData postData;
}
