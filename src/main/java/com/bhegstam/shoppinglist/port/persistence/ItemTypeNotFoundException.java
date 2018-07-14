package com.bhegstam.shoppinglist.port.persistence;

import com.bhegstam.shoppinglist.domain.ItemTypeId;

public class ItemTypeNotFoundException extends RuntimeException {
    public ItemTypeNotFoundException(ItemTypeId id) {
    }
}
