package com.bhegstam.shoppinglist.domain;

public class UserId extends Identifier {
    public UserId() {
    }

    private UserId(String id) {
        super(id);
    }

    public static UserId from(String id) {
        return new UserId(id);
    }
}
