package com.bhegstam.itemtype;

import com.bhegstam.itemtype.domain.ItemType;
import com.bhegstam.itemtype.domain.ItemTypeId;
import com.bhegstam.itemtype.domain.ItemTypeRepository;
import org.apache.commons.lang.NotImplementedException;

import java.util.HashMap;
import java.util.List;
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

    @Override
    public List<ItemType> findItemTypes(String nameStart, int limit) {
        throw new NotImplementedException("NYI");
    }

    @Override
    public List<ItemType> getItemTypes() {
        throw new NotImplementedException("NYI");
    }

    @Override
    public void deleteItemType(ItemTypeId itemTypeId) {
        throw new NotImplementedException("NYI");
    }
}
