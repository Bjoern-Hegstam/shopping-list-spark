package com.bhegstam.shoppinglist.application;

import com.bhegstam.shoppinglist.domain.ItemType;
import com.bhegstam.shoppinglist.domain.ItemTypeId;
import com.bhegstam.shoppinglist.domain.ItemTypeRepository;

import java.util.List;

public class ItemTypeApplication {
    private final ItemTypeRepository itemTypeRepository;

    public ItemTypeApplication(ItemTypeRepository itemTypeRepository) {
        this.itemTypeRepository = itemTypeRepository;
    }

    public ItemType createItemType(String name) {
        ItemType itemType = new ItemType(name);
        itemTypeRepository.createItemType(itemType);
        return itemType;
    }

    public List<ItemType> findItemTypes(String nameStart, int limit) {
        return itemTypeRepository.findItemTypes(nameStart, limit);
    }

    public void deleteItemType(ItemTypeId itemTypeId) {
        itemTypeRepository.deleteItemType(itemTypeId);
    }
}
