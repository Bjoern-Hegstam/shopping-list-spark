package com.bhegstam.shoppinglist.configuration.property;

import com.bhegstam.shoppinglist.configuration.EnvironmentVariable;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Database {
    private String url;
    private String username;
    private String password;

    public Database() {
        url = System.getenv(EnvironmentVariable.JDBC_DATABASE_URL);
        username = System.getenv(EnvironmentVariable.JDBC_DATABASE_USERNAME);
        password = System.getenv(EnvironmentVariable.JDBC_DATABASE_PASSWORD);
    }
}
