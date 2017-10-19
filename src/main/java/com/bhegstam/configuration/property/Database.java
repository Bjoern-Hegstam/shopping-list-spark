package com.bhegstam.configuration.property;

import lombok.Data;

@Data
public class Database {
    private String url;
    private String user;
    private String password;
}
