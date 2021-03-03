package com.bhegstam.shoppinglist.domain;

import java.util.List;

public interface ItemTypeRepository {
    void create(ItemType itemType);

    ItemType get(ItemTypeId id);

    List<ItemType> findItemTypes(String nameStart, int limit);

    List<ItemType> getItemTypes();

    void deleteItemType(ItemTypeId itemTypeId);
}
