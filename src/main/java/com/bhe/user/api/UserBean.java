package com.bhe.user.api;

import com.bhe.user.Role;
import com.bhe.user.User;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class UserBean {
    private Integer id;
    private String username;
    private String email;
    private Boolean verified;
    private Role role;

    public UserBean() {
    }

    public static UserBean fromUser(User user) {
        UserBean bean = new UserBean();
        bean.id = user.getId();
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean isVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
