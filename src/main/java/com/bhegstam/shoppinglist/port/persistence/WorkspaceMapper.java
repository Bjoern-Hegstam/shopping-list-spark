package com.bhegstam.shoppinglist.port.persistence;

import com.bhegstam.shoppinglist.domain.Workspace;
import com.bhegstam.shoppinglist.domain.WorkspaceId;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

public class WorkspaceMapper implements RowMapper<Workspace> {
    @Override
    public Workspace map(ResultSet rs, StatementContext ctx) throws SQLException {
        return Workspace.fromDb(
                WorkspaceId.from(rs.getString("workspace.id")),
                rs.getString("workspace.name"),
                rs.getString("workspace.created_by")
        );
    }
}
