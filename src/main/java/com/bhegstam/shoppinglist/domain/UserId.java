package com.bhegstam.shoppinglist.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

@EqualsAndHashCode
public class UserId {
    @Getter
    private final int id;

    private UserId(int id) {
        this.id = id;
    }

    public static UserId from(int id) {
        return new UserId(id);
    }

    public static UserId from(String id) {
        return UserId.from(Integer.parseInt(id));
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
