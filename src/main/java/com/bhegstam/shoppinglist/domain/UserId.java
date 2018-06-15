package com.bhegstam.shoppinglist.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
public class UserId {
    @Getter
    private final int id;

    private UserId(int id) {
        this.id = id;
    }

    public static UserId from(int id) {
        return new UserId(id);
    }

    public static UserId from(String id) {
        return UserId.from(Integer.parseInt(id));
    }

    @Override
    public String toString() {
        return Integer.toString(id);
    }
}
