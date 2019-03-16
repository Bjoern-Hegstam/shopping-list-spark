package com.bhegstam.shoppinglist.domain;

import com.bhegstam.shoppinglist.port.persistence.PersistenceStatus;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class ShoppingListItem extends Entity<ShoppingListItemId> {
    private final ItemType itemType;
    private int quantity;
    private boolean inCart;

    public ShoppingListItem(ItemType itemType) {
        this(
                new ShoppingListItemId(),
                itemType,
                0,
                false,
                PersistenceStatus.INSERT_REQUIRED
        );
    }

    public ShoppingListItem(
            ShoppingListItemId id,
            ItemType itemType,
            int quantity,
            boolean inCart,
            PersistenceStatus persistenceStatus
    ) {
        super(
                id,
                LocalDateTime.now(ZoneOffset.UTC),
                LocalDateTime.now(ZoneOffset.UTC),
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
