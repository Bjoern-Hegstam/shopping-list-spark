package com.bhegstam.shoppinglist.port.persistence;

import com.bhegstam.shoppinglist.domain.ItemType;
import com.bhegstam.shoppinglist.domain.ItemTypeId;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ItemTypeMapper implements RowMapper<ItemType> {
    @Override
    public ItemType map(ResultSet rs, StatementContext ctx) throws SQLException {
        return ItemType.fromDb(
                ItemTypeId.parse(rs.getString("id")),
                rs.getString("name"),
                rs.getTimestamp("created_at").toInstant(),
                rs.getTimestamp("updated_at").toInstant(),
                PersistenceStatus.PERSISTED);
    }
}
