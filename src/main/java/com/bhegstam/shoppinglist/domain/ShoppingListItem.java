package com.bhegstam.shoppinglist.domain;

import com.bhegstam.shoppinglist.port.persistence.PersistenceStatus;

import java.time.Instant;

public class ShoppingListItem extends Entity<ShoppingListItemId> {
    private final ItemType itemType;
    private int quantity;
    private boolean inCart;

    public static ShoppingListItem create(ItemType itemType, int quantity) {
        return new ShoppingListItem(
                new ShoppingListItemId(),
                itemType,
                quantity,
                false,
                Instant.now(),
                null,
                PersistenceStatus.INSERT_REQUIRED
        );
    }

    public static ShoppingListItem fromDb(
            ShoppingListItemId id,
            ItemType itemType,
            int quantity,
            boolean inCart,
            Instant createdAt,
            Instant updatedAt,
            PersistenceStatus persistenceStatus
    ) {
        return new ShoppingListItem(id, itemType, quantity, inCart, createdAt, updatedAt, persistenceStatus);
    }

    private ShoppingListItem(
            ShoppingListItemId id,
            ItemType itemType,
            int quantity,
            boolean inCart,
            Instant createdAt,
            Instant updatedAt,
            PersistenceStatus persistenceStatus
    ) {
        super(
                id,
                createdAt,
                updatedAt,
                persistenceStatus
        );
        this.itemType = itemType;
        this.quantity = quantity;
        this.inCart = inCart;
    }

    public ItemType getItemType() {
        return itemType;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        if (this.quantity != quantity) {
            this.quantity = quantity;
            markAsUpdated();
        }
    }

    public boolean isInCart() {
        return inCart;
    }

    public void setInCart(boolean inCart) {
        if (this.inCart != inCart) {
            this.inCart = inCart;
            markAsUpdated();
        }
    }
}
