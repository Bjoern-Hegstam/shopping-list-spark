package com.bhegstam.itemtype.domain;

public interface ItemTypeRepository {
    ItemType createItemType(String name);

    ItemType get(ItemTypeId id);
}
