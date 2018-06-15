package com.bhegstam.shoppinglist.domain;

import lombok.EqualsAndHashCode;

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
}
