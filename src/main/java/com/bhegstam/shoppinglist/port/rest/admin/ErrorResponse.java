package com.bhegstam.shoppinglist.port.rest.admin;

import com.bhegstam.shoppinglist.domain.InvalidEmailException;
import com.bhegstam.shoppinglist.domain.InvalidUsernameException;
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

    static ErrorResponse exception(InvalidUsernameException e) {
        return new ErrorResponse(ErrorCode.INVALID_USERNAME, e.getMessage());
    }

    static ErrorResponse exception(InvalidEmailException e) {
        return new ErrorResponse(ErrorCode.INVALID_EMAIL, e.getMessage());
    }

    private enum ErrorCode {
        INVALID_USERNAME,
        INVALID_EMAIL
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
