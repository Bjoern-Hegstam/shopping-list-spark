package com.bhegstam;

import com.bhegstam.shoppinglist.configuration.ApplicationConfiguration;
import com.bhegstam.shoppinglist.configuration.ConfigurationModule;
import com.bhegstam.shoppinglist.persistence.ItemTypeModule;
import com.bhegstam.shoppinglist.persistence.ShoppingListModule;
import com.bhegstam.shoppinglist.persistence.UserModule;
import com.bhegstam.shoppinglist.port.rest.*;
import com.bhegstam.webutil.webapp.ApplicationBase;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import spark.Service;

import java.util.List;

import static java.util.Arrays.asList;

public class Application extends ApplicationBase {

    private final ApplicationConfiguration configuration;

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(
                new ConfigurationModule(),
                new ItemTypeModule(),
                new ShoppingListModule(),
                new UserModule()
        );

        injector
             .getInstance(Application.class)
             .init();
    }

    @Inject
    public Application(
            ApplicationConfiguration configuration,
            IndexController indexController,
            LoginController loginController,
            ItemTypeApiController itemTypeApiController,
            ShoppingListApiController shoppingListApiController,
            UserApiController userApiController
    ) {
        super(
                List.of(
                        indexController,
                        loginController
                ),
                asList(
                        itemTypeApiController,
                        shoppingListApiController,
                        userApiController
                ),
                true,
                true
        );

        this.configuration = configuration;
    }

    @Override
    protected void configureServer(Service http) {
        http.port(configuration.getServer().getPort());

        http.staticFiles.location("/public");
        http.staticFiles.expireTime(600);

        http.after((request, response) -> {
            response.header("Content-Encoding", "gzip");
        });
    }
}
