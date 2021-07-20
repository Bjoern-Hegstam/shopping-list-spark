package com.bhegstam.shoppinglist.domain;

import java.time.Instant;
import java.util.Objects;

public class Workspace {
    private final WorkspaceId id;
    private final String name;
    private final String createdBy;
    private final Instant createdAt;

    private Workspace(WorkspaceId id, String name, String createdBy) {
        this.id = id;
        this.name = name;
        this.createdBy = createdBy;
        this.createdAt = Instant.now();
    }

    public static Workspace create(String name, User createdBy) {
        return new Workspace(new WorkspaceId(), name, createdBy.getId().getId());
    }

    public static Workspace fromDb(WorkspaceId id, String name, String createdBy) {
        return new Workspace(id, name, createdBy);
    }

    public WorkspaceId getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Workspace workspace = (Workspace) o;
        return id.equals(workspace.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
