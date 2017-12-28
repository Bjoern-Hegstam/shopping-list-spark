package com.bhegstam.shoppinglist.domain;

import com.bhegstam.itemtype.domain.ItemType;
import com.bhegstam.util.db.PersistenceStatus;

public class ShoppingListItem {
    private final ShoppingListItemId id;
    private final ItemType itemType;
    private int quantity;
    private boolean inCart;
    private PersistenceStatus persistenceStatus;

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
        this.id = id;
        this.itemType = itemType;
        this.quantity = quantity;
        this.inCart = inCart;
        this.persistenceStatus = persistenceStatus;
    }

    public ShoppingListItemId getId() {
        return id;
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
            itemUpdated();
        }
    }

    private void itemUpdated() {
        if (persistenceStatus != PersistenceStatus.INSERT_REQUIRED) {
            persistenceStatus = PersistenceStatus.UPDATED_REQUIRED;
        }
    }

    public boolean isInCart() {
        return inCart;
    }

    public void setInCart(boolean inCart) {
        if (this.inCart != inCart) {
            this.inCart = inCart;
            itemUpdated();
        }
    }

    public PersistenceStatus getPersistenceStatus() {
        return persistenceStatus;
    }

    public void setPersistenceStatus(PersistenceStatus persistenceStatus) {
        this.persistenceStatus = persistenceStatus;
    }
}
