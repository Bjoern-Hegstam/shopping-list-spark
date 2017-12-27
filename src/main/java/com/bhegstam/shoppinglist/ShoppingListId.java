package com.bhegstam.shoppinglist;

import lombok.EqualsAndHashCode;

import java.util.UUID;

@EqualsAndHashCode
public class ShoppingListId {

    private final UUID id;

    ShoppingListId() {
        id = UUID.randomUUID();
    }

    ShoppingListId(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }
}
