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
        return ShoppingListItem.fromDb(
                ShoppingListItemId.parse(rs.getString("i_id")),
                ItemType.fromDb(
                        ItemTypeId.parse(rs.getString("it_id")),
                        rs.getString("it_name"),
                        rs.getTimestamp("it_created_at").toInstant(),
                        rs.getTimestamp("it_updated_at").toInstant(),
                        PersistenceStatus.PERSISTED
                ),
                rs.getInt("i_quantity"),
                rs.getBoolean("i_in_cart"),
                rs.getTimestamp("i_created_at").toInstant(),
                rs.getTimestamp("i_updated_at").toInstant(),
                PersistenceStatus.PERSISTED
        );
    }
}
