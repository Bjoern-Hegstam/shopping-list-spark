package com.bhegstam.shoppinglist.domain;

import lombok.EqualsAndHashCode;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.util.UUID;

@EqualsAndHashCode
public abstract class Identifier {
    private final String id;

    public Identifier() {
        id = UUID.randomUUID().toString();
    }

    public Identifier(String id) {
        UUID.fromString(id); // Ensure that id is UUID
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
