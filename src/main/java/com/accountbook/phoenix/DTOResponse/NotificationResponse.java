package com.accountbook.phoenix.DTOResponse;

import com.accountbook.phoenix.DTO.UserData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationResponse {
    private String message;
    private PostData post;
}
