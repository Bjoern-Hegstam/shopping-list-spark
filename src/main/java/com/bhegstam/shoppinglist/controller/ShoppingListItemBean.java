package com.bhegstam.shoppinglist.controller;

import com.bhegstam.itemtype.controller.ItemTypeBean;
import com.bhegstam.shoppinglist.domain.ShoppingListItem;
import lombok.Data;

@Data
public class ShoppingListItemBean {
    private String id;
    private ItemTypeBean itemType;
    private int quantity;
    private boolean inCart;

    public static ShoppingListItemBean fromShoppingListItem(ShoppingListItem item) {
        ShoppingListItemBean bean = new ShoppingListItemBean();
        bean.id = item.getId().toString();
        bean.itemType = ItemTypeBean.fromItemType(item.getItemType());
        bean.quantity = item.getQuantity();
        bean.inCart = item.isInCart();
        return bean;
    }
}
