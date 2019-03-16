package com.bhegstam.shoppinglist.port.persistence;

import com.bhegstam.shoppinglist.domain.ShoppingList;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ShoppingListMapper implements RowMapper<ShoppingList> {
    @Override
    public ShoppingList map(ResultSet rs, StatementContext ctx) throws SQLException {
        return ShoppingList.fromDb(
                rs.getString("id"),
                rs.getString("name"),
                rs.getTimestamp("created_at").toInstant(),
                rs.getTimestamp("updated_at").toInstant()
        );
    }
}
