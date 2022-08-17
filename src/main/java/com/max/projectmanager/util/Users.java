package com.max.projectmanager.util;

import com.max.projectmanager.entity.User;
import org.springframework.security.core.context.SecurityContextHolder;

public class Users {
    public static User getCurrentUser() {
        var user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (user == null) {
            return null;
        }

        if (!(user instanceof User)) {
            throw new RuntimeException("current user is not instance of entity.User");
        }

        return (User) user;
    }
}
