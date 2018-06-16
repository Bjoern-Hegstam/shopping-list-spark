package com.bhegstam.shoppinglist.persistence;

import com.bhegstam.shoppinglist.domain.ItemType;
import com.bhegstam.shoppinglist.domain.ItemTypeId;
import com.bhegstam.shoppinglist.domain.ItemTypeRepository;
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
    public void createItemType(ItemType itemType) {
        itemTypes.put(itemType.getId(), itemType);
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
