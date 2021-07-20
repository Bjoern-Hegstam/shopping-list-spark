package com.bhegstam.shoppinglist.domain;

public interface WorkspaceRepository {
    void add(UserId userId, Workspace workspace);

    Workspace getDefaultWorkspace(UserId userId);
}
