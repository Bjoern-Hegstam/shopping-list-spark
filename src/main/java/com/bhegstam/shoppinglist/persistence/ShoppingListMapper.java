package com.bhegstam.shoppinglist.persistence;

import com.bhegstam.shoppinglist.domain.ShoppingList;
import com.bhegstam.shoppinglist.domain.ShoppingListId;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ShoppingListMapper implements RowMapper<ShoppingList> {
    @Override
    public ShoppingList map(ResultSet rs, StatementContext ctx) throws SQLException {
        return new ShoppingList(
                ShoppingListId.fromString(rs.getString("id")),
                rs.getString("name"),
                PersistenceStatus.PERSISTED
        );
    }
}
