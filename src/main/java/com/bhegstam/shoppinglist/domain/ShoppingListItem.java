package com.bhegstam.shoppinglist.domain;

import com.bhegstam.util.db.PersistenceStatus;

public class ShoppingListItem {
    private final ShoppingListItemId id;
    private int quantity;
    private boolean inCart;
    private PersistenceStatus persistenceStatus;

    public ShoppingListItem() {
        this(new ShoppingListItemId());
        this.persistenceStatus = PersistenceStatus.INSERT_REQUIRED;
    }

    public ShoppingListItem(ShoppingListItemId id) {
        this.id = id;
        this.quantity = 0;
        this.inCart = false;
        this.persistenceStatus = PersistenceStatus.NOT_CHANGED;
    }

    public ShoppingListItemId getId() {
        return id;
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
