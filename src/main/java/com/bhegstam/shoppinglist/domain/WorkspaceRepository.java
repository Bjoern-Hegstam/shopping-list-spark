package com.bhegstam.shoppinglist.domain;

import java.util.List;

public interface WorkspaceRepository {
    void create(UserId userId, Workspace workspace);

    Workspace getDefaultWorkspace(UserId userId);

    List<Workspace> getWorkspaces(UserId userId);
}
