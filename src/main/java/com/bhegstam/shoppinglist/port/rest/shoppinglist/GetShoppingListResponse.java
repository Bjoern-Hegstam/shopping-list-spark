package com.bhegstam.shoppinglist.port.rest.shoppinglist;

import com.bhegstam.shoppinglist.domain.ItemType;
import com.bhegstam.shoppinglist.domain.ShoppingList;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

class GetShoppingListResponse {
    private final String id;
    private final String name;
    private final List<ItemTypeDto> itemTypes;
    private final List<ShoppingListItemDto> items;

    GetShoppingListResponse(ShoppingList shoppingList) {
        this.id = shoppingList.getId().getId();
        this.name = shoppingList.getName();
        this.itemTypes = shoppingList
                .getItemTypes().stream()
                .sorted(comparing(ItemType::getName))
                .map(ItemTypeDto::new)
                .collect(toList());
        this.items = shoppingList
                .getItems().stream()
                .sorted(comparing(item -> item.getItemType().getName()))
                .map(ShoppingListItemDto::new)
                .collect(toList());
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<ItemTypeDto> getItemTypes() {
        return itemTypes;
    }

    public List<ShoppingListItemDto> getItems() {
        return items;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
