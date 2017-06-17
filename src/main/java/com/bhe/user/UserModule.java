package com.bhe.user;

import com.google.inject.AbstractModule;

public class UserModule extends AbstractModule{
    @Override
    protected void configure() {
        bind(UserRepository.class).to(UserRepositoryInMem.class).asEagerSingleton();
        bind(UserRegistration.class);
    }
}
