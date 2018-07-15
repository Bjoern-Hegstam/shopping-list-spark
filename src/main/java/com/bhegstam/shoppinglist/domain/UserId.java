package com.bhegstam.shoppinglist.domain;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class UserId extends Identifier {
    public UserId() {
    }

    private UserId(String id) {
        super(id);
    }

    public static UserId from(String id) {
        return new UserId(id);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
