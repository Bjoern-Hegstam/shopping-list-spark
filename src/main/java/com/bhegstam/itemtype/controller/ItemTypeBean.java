package com.bhegstam.itemtype.controller;

import com.bhegstam.itemtype.domain.ItemType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.io.IOException;

@Data
public class ItemTypeBean {
    private String id;
    private String name;

    public static ItemTypeBean fromItemType(ItemType itemType) {
        ItemTypeBean bean = new ItemTypeBean();
        bean.setId(itemType.getId().getId().toString());
        bean.setName(itemType.getName());
        return bean;
    }

    static ItemTypeBean fromJson(String json) {
        try {
            return new ObjectMapper().readValue(json, ItemTypeBean.class);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
