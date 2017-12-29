package com.bhegstam.shoppinglist.controller;

import com.bhegstam.shoppinglist.domain.ShoppingList;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

@Data
public class ShoppingListBean {
    private UUID id;
    private String name;
    private List<ShoppingListItemBean> items;

    public static ShoppingListBean fromShoppingList(ShoppingList shoppingList) {
        ShoppingListBean bean = new ShoppingListBean();
        bean.id = shoppingList.getId().getId();
        bean.name = shoppingList.getName();
        bean.items = shoppingList
                .getItems().stream()
                .map(ShoppingListItemBean::fromShoppingListItem)
                .sorted(comparing(itemBean -> itemBean.getItemType().getName()))
                .collect(toList());
        return bean;
    }

    static ShoppingListBean fromJson(String json) {
        try {
            return new ObjectMapper().readValue(json, ShoppingListBean.class);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
