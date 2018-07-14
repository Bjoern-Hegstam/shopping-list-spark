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
        return new ItemType(
                ItemTypeId.parse(rs.getString("id")),
                rs.getString("name")
        );
    }
}
