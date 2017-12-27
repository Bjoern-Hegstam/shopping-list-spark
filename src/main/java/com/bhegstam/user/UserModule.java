package com.bhegstam.user;

import com.bhegstam.user.db.UserRepositoryImpl;
import com.bhegstam.user.domain.UserRegistration;
import com.bhegstam.user.domain.UserRepository;
import com.google.inject.AbstractModule;

public class UserModule extends AbstractModule{
    @Override
    protected void configure() {
        bind(UserRepository.class).to(UserRepositoryImpl.class);
        bind(UserRegistration.class);
    }
}
