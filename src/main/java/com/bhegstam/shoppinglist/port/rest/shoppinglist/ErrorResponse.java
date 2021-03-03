package com.bhegstam.shoppinglist.port.rest.shoppinglist;

import com.bhegstam.shoppinglist.domain.ItemType;
import com.bhegstam.shoppinglist.domain.ItemTypeNameAlreadyTakenException;
import com.bhegstam.shoppinglist.domain.ItemTypeUsedInShoppingListException;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

class ErrorResponse {
    @JsonProperty
    ErrorCode errorCode;

    @JsonProperty
    String message;

    private ErrorResponse(ErrorCode errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    static ErrorResponse exception(ItemTypeUsedInShoppingListException e) {
        return new ErrorResponse(ErrorCode.ITEM_TYPE_USED_IN_SHOPPING_LIST, e.getMessage());
    }

    static ErrorResponse exception(ItemTypeNameAlreadyTakenException e) {
        ItemType itemType = e.getItemType();
        return new ErrorResponse(ErrorCode.ITEM_TYPE_NAME_ALREADY_TAKEN, String.format("Name [%s] is used by item type with id [%s]", itemType.getName(), itemType.getId().getId()));
    }

    private enum ErrorCode {
        ITEM_TYPE_USED_IN_SHOPPING_LIST,
        ITEM_TYPE_NAME_ALREADY_TAKEN
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
