package com.bhegstam.itemtype;

import com.bhegstam.itemtype.domain.ItemType;
import com.bhegstam.itemtype.domain.ItemTypeId;
import com.bhegstam.itemtype.domain.ItemTypeRepository;

import java.util.HashMap;
import java.util.Map;

public class InMemoryItemTypeRepository implements ItemTypeRepository {
    private final Map<ItemTypeId, ItemType> itemTypes;

    public InMemoryItemTypeRepository() {
        itemTypes = new HashMap<>();
    }

    @Override
    public ItemType createItemType(String name) {
        ItemType itemType = new ItemType(
                new ItemTypeId(),
                name
        );
        itemTypes.put(itemType.getId(), itemType);
        return itemType;
    }

    @Override
    public ItemType get(ItemTypeId id) {
        return itemTypes.get(id);
    }
}
