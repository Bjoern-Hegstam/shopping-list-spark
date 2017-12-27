package com.bhegstam.shoppinglist.domain;

import lombok.EqualsAndHashCode;

import java.util.UUID;

@EqualsAndHashCode
public class ShoppingListId {

    private final UUID id;

    public ShoppingListId() {
        id = UUID.randomUUID();
    }

    public ShoppingListId(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }
}
