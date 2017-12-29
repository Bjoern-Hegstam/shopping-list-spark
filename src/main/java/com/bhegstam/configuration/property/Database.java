package com.bhegstam.configuration.property;

import com.bhegstam.configuration.EnvironmentVariable;
import lombok.Data;

@Data
public class Database {
    private String url;
    private String user;
    private String password;

    public Database() {
        url = System.getenv(EnvironmentVariable.JDBC_DATABASE_URL);
        user = System.getenv(EnvironmentVariable.JDBC_DATABASE_USERNAME);
        password = System.getenv(EnvironmentVariable.JDBC_DATABASE_PASSWORD);
    }
}
