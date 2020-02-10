package com.bhegstam.shoppinglist.port.rest.shoppinglist;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.validation.ValidationMethod;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class CreateShoppingListItemRequest {
    private final String itemTypeId;
    private final String itemTypeName;

    @NotNull
    @Min(0)
    private final Integer quantity;

    @JsonCreator
    public CreateShoppingListItemRequest(
            @JsonProperty("itemTypeId") String itemTypeId,
            @JsonProperty("itemTypeName") String itemTypeName,
            @JsonProperty("quantity") Integer quantity
    ) {
        this.itemTypeId = itemTypeId;
        this.itemTypeName = itemTypeName;
        this.quantity = quantity;
    }

    @ValidationMethod(message = "Must specify exactly one of itemTypeId or itemTypeName")
    public boolean hasValidItemTypeSpecification() {
        boolean hasItemTypeId = itemTypeId != null && !itemTypeId.isEmpty();
        boolean hasItemTypeName = itemTypeName != null && !itemTypeName.isEmpty();
        return hasItemTypeId ^ hasItemTypeName;
    }

    public String getItemTypeId() {
        return itemTypeId;
    }

    public String getItemTypeName() {
        return itemTypeName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
