package com.bhegstam.shoppinglist.port.rest;

import com.bhegstam.shoppinglist.domain.ShoppingListItem;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.io.IOException;

@Data
public class ShoppingListItemBean {
    private String id;
    private ItemTypeBean itemType;
    private Integer quantity;
    private Boolean inCart;

    public ShoppingListItemBean(ShoppingListItem item) {
        this.id = item.getId().getId().toString();
        this.itemType = ItemTypeBean.fromItemType(item.getItemType());
        this.quantity = item.getQuantity();
        this.inCart = item.isInCart();
    }

    public static ShoppingListItemBean fromJson(String json) {
        try {
            return new ObjectMapper().readValue(json, ShoppingListItemBean.class);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }

    }
}
