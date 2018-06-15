package com.bhegstam.shoppinglist.persistence;

import com.bhegstam.shoppinglist.domain.UserRegistration;
import com.bhegstam.shoppinglist.domain.UserRepository;
import com.google.inject.AbstractModule;

public class UserModule extends AbstractModule{
    @Override
    protected void configure() {
        bind(UserRepository.class).to(JdbcUserRepository.class);
        bind(UserRegistration.class);
    }
}
