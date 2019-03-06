package com.bhegstam.shoppinglist.port.rest.user;

import com.bhegstam.shoppinglist.domain.Role;
import lombok.Data;

@Data
public class UserBean {
    private String id;
    private String username;
    private String email;
    private Boolean verified;
    private Role role;
}
