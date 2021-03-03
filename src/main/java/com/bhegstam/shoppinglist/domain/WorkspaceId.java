package com.bhegstam.shoppinglist.domain;

public class WorkspaceId extends Identifier {
    public WorkspaceId() {
    }

    private WorkspaceId(String id) {
        super(id);
    }

    public static WorkspaceId from(String id) {
        return new WorkspaceId(id);
    }
}
