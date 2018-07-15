package com.bhegstam.shoppinglist.port.rest;

import com.bhegstam.shoppinglist.domain.Role;
import com.bhegstam.shoppinglist.domain.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.io.IOException;

@Data
public class UserBean {
    private String id;
    private String username;
    private String email;
    private Boolean verified;
    private Role role;

    public static UserBean fromUser(User user) {
        UserBean bean = new UserBean();
        bean.id = user.getId().getId();
        bean.username = user.getUsername();
        bean.email = user.getEmail();
        bean.verified = user.isVerified();
        bean.role = user.getRole();
        return bean;
    }

    public static UserBean fromJson(String json) {
        try {
            return new ObjectMapper().readValue(json, UserBean.class);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
