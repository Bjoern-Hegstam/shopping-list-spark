package com.bhegstam.shoppinglist.port.persistence;

import com.bhegstam.shoppinglist.domain.UserId;
import com.bhegstam.shoppinglist.domain.Workspace;
import com.bhegstam.shoppinglist.domain.WorkspaceId;
import com.bhegstam.shoppinglist.domain.WorkspaceRepository;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.jdbi.v3.sqlobject.transaction.Transaction;

import java.time.Instant;
import java.util.List;

@RegisterRowMapper(WorkspaceMapper.class)
public interface JdbiWorkspaceRepository extends WorkspaceRepository {
    @Transaction
    default void create(UserId userId, Workspace workspace) {
        createWorkspace(
                workspace.getId(),
                workspace.getName(),
                workspace.getCreatedBy(),
                workspace.getCreatedAt()
        );

        linkToUser(
                userId,
                workspace.getId(),
                true,
                workspace.getCreatedBy(),
                workspace.getCreatedAt()
        );
    }

    @Override
    @SqlQuery("select workspace.id, workspace.name, workspace.created_by" +
            " from workspace" +
            " inner join user_in_workspace on workspace.id = user_in_workspace.workspace_id" +
            " where user_in_workspace.user_id = :userId.id and user_in_workspace.is_default_workspace = true")
    Workspace getDefaultWorkspace(@BindBean("userId") UserId userId);

    @Override
    @SqlQuery("select workspace.id, workspace.name, workspace.created_by" +
            " from workspace" +
            " inner join user_in_workspace on workspace.id = user_in_workspace.workspace_id" +
            " where user_in_workspace.user_id = :userId.id")
    List<Workspace> getWorkspaces(@BindBean("userId") UserId userId);

    @SqlUpdate("insert into workspace(id, name, created_by, created_at) values (:workspaceId.id, :name, :createdBy, :createdAt)")
    void createWorkspace(
            @BindBean("workspaceId") WorkspaceId workspaceId,
            @Bind("name") String name,
            @Bind("createdBy") String createdBy,
            @Bind("createdAt") Instant createdAt
    );

    @SqlUpdate("insert into user_in_workspace(user_id, workspace_id, is_default_workspace, created_by, created_at) " +
            "values (:userId.id, :workspaceId.id, :isDefaultWorkspace, :createdBy, :createdAt)")
    void linkToUser(
            @BindBean("userId") UserId userId,
            @BindBean("workspaceId") WorkspaceId workspaceId,
            @Bind("isDefaultWorkspace") boolean isDefaultWorkspace,
            @Bind("createdBy") String createdBy,
            @Bind("createdAt") Instant createdAt
    );
}
