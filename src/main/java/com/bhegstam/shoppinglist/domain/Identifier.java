package com.bhegstam.shoppinglist.domain;

import lombok.EqualsAndHashCode;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.util.UUID;

@EqualsAndHashCode
public abstract class Identifier {
    private final UUID id;

    public Identifier() {
        id = UUID.randomUUID();
    }

    public Identifier(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
