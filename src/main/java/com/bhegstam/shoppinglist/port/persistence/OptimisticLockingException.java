package com.bhegstam.shoppinglist.port.persistence;

public class OptimisticLockingException extends RuntimeException {
    public OptimisticLockingException(String message) {
        super(message);
    }
}
