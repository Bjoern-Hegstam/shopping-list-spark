package com.bhegstam.shoppinglist.port.rest.shoppinglist;

import com.bhegstam.shoppinglist.domain.ItemType;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;

import static java.util.stream.Collectors.toList;

class ItemTypesResponse {
    @JsonProperty
    List<ItemTypeResponse> itemTypes;

    ItemTypesResponse(List<ItemType> itemTypes) {
        this.itemTypes = itemTypes
                .stream()
                .map(ItemTypeResponse::new)
                .collect(toList());
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
