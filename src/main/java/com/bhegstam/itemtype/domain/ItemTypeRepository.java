package com.bhegstam.itemtype.domain;

import java.util.List;

public interface ItemTypeRepository {
    ItemType createItemType(String name);

    ItemType get(ItemTypeId id);

    List<ItemType> findItemTypes(String nameStart, int limit);

    List<ItemType> getItemTypes();

    void deleteItemType(ItemTypeId itemTypeId);
}
