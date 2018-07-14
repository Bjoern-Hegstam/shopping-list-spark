package com.bhegstam.shoppinglist.port.rest;

import com.bhegstam.shoppinglist.domain.ItemType;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class ItemTypeResponse {
    @JsonProperty
    private final String id;

    @JsonProperty
    private final String name;

    public ItemTypeResponse(ItemType itemType) {
        id = itemType.getId().getId();
        name = itemType.getName();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
