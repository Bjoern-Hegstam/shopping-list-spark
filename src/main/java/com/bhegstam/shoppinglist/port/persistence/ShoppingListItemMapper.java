package com.bhegstam.shoppinglist.port.persistence;

import com.bhegstam.shoppinglist.domain.ItemType;
import com.bhegstam.shoppinglist.domain.ItemTypeId;
import com.bhegstam.shoppinglist.domain.ShoppingListItem;
import com.bhegstam.shoppinglist.domain.ShoppingListItemId;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ShoppingListItemMapper implements RowMapper<ShoppingListItem> {
    @Override
    public ShoppingListItem map(ResultSet rs, StatementContext ctx) throws SQLException {
        return new ShoppingListItem(
                ShoppingListItemId.fromString(rs.getString("i_id")),
                new ItemType(
                        ItemTypeId.parse(rs.getString("it_id")),
                        rs.getString("it_name"),
                        PersistenceStatus.PERSISTED
                ),
                rs.getInt("i_quantity"),
                rs.getBoolean("i_in_cart"),
                PersistenceStatus.PERSISTED
        );
    }
}
