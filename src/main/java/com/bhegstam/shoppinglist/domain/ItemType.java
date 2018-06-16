package com.bhegstam.shoppinglist.domain;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class ItemType {
    private final ItemTypeId id;
    private final String name;

    public ItemType(String name) {
        this(new ItemTypeId(), name);
    }

    public ItemType(ItemTypeId id, String name) {
        this.id = id;
        this.name = name;
    }

    public ItemTypeId getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ItemType itemType = (ItemType) o;

        return id != null ? id.equals(itemType.id) : itemType.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
