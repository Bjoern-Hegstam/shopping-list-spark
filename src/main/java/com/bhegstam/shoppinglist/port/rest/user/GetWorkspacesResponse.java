package com.bhegstam.shoppinglist.port.rest.user;

import com.bhegstam.shoppinglist.domain.Workspace;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class GetWorkspacesResponse {
    @JsonProperty
    private final List<WorkspaceResponse> workspaces;

    public GetWorkspacesResponse(List<Workspace> workspaces) {
        this.workspaces = workspaces.stream().map(WorkspaceResponse::new).collect(toList());
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
