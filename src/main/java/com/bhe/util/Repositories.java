package com.bhe.util;

import com.bhe.user.UserRepository;
import com.bhe.user.UserRepositoryInMem;

public class Repositories {
    private static UserRepository userRepository;

    public static void initDev() {
        userRepository = new UserRepositoryInMem();
    }

    public static UserRepository users() {
        return userRepository;
    }
}
