package com.bhegstam.shoppinglist.controller;

import com.bhegstam.shoppinglist.domain.ShoppingList;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.io.IOException;

@Data
public class ShoppingListBean {
    private String id;
    private String name;

    public static ShoppingListBean fromShoppingList(ShoppingList shoppingList) {
        ShoppingListBean bean = new ShoppingListBean();
        bean.id = shoppingList.getId().getId().toString();
        bean.name = shoppingList.getName();
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
