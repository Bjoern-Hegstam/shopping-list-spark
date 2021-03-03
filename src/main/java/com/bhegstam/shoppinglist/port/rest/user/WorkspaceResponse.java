package com.bhegstam.shoppinglist.port.rest.user;

import com.bhegstam.shoppinglist.domain.Workspace;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class WorkspaceResponse {
    @JsonProperty
    private final String name;

    public WorkspaceResponse(Workspace workspace) {
        this.name = workspace.getName();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
