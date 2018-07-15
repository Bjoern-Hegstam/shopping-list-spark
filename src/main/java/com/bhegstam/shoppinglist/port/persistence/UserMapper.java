package com.bhegstam.shoppinglist.port.persistence;

import com.bhegstam.shoppinglist.domain.Role;
import com.bhegstam.shoppinglist.domain.User;
import com.bhegstam.shoppinglist.domain.UserId;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper implements RowMapper<User> {
    @Override
    public User map(ResultSet rs, StatementContext ctx) throws SQLException {
        return new User(
                UserId.from(rs.getString("id")),
                rs.getString("username"),
                rs.getString("email"),
                rs.getString("hashed_password"),
                rs.getString("salt"),
                rs.getBoolean("verified"),
                rs.getString("role").equals(Role.RoleName.ADMIN) ? Role.ADMIN : Role.USER
        );
    }
}
