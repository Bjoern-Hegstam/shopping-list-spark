package com.bhegstam.shoppinglist.port.persistence;

import com.bhegstam.shoppinglist.domain.Workspace;
import com.bhegstam.shoppinglist.domain.WorkspaceId;
import com.bhegstam.shoppinglist.domain.WorkspaceRepository;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.time.Instant;

public interface JdbiWorkspaceRepository extends WorkspaceRepository {
    default void persist(Workspace workspace) {
        persist(
                workspace.getId(),
                workspace.getName(),
                workspace.getCreatedBy(),
                workspace.getCreatedAt()
        );
    }

    @SqlUpdate("insert into workspace(id, name, createdBy, createdAt) values (:workspaceId.id, :name, :createdBy, :createdAt)")
    void persist(WorkspaceId workspaceId, String name, String createdBy, Instant createdAt);
}
