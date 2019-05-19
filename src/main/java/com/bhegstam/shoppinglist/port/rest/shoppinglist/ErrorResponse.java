package com.bhegstam.shoppinglist.port.rest.shoppinglist;

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

    static Object exception(ItemTypeUsedInShoppingListException e) {
        return new ErrorResponse(ErrorCode.ITEM_TYPE_USED_IN_SHOPPING_LIST, e.getMessage());
    }

    private enum ErrorCode {
        ITEM_TYPE_USED_IN_SHOPPING_LIST
    }


    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
