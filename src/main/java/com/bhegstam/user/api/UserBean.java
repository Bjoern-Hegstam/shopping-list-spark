package com.bhegstam.user.api;

import com.bhegstam.user.Role;
import com.bhegstam.user.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.io.IOException;

@Data
class UserBean {
    private String id;
    private String username;
    private String email;
    private Boolean verified;
    private Role role;

    static UserBean fromUser(User user) {
        UserBean bean = new UserBean();
        bean.id = Integer.toString(user.getId().getId());
        bean.username = user.getUsername();
        bean.email = user.getEmail();
        bean.verified = user.isVerified();
        bean.role = user.getRole();
        return bean;
    }

    static UserBean fromJson(String json) {
        try {
            return new ObjectMapper().readValue(json, UserBean.class);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }
}