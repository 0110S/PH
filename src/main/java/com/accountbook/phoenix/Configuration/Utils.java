package com.accountbook.phoenix.Configuration;

import com.accountbook.phoenix.Entity.User;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;
@Configuration
public class Utils {
    public User getUser() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user;
    }
}
