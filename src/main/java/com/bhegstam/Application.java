package com.bhegstam;

import com.bhegstam.shoppinglist.application.ItemTypeApplication;
import com.bhegstam.shoppinglist.application.ShoppingListApplication;
import com.bhegstam.shoppinglist.application.UserApplication;
import com.bhegstam.shoppinglist.configuration.ApplicationConfiguration;
import com.bhegstam.shoppinglist.persistence.DatabaseMigrator;
import com.bhegstam.shoppinglist.persistence.JdbiItemTypeRepository;
import com.bhegstam.shoppinglist.persistence.JdbiShoppingListRepository;
import com.bhegstam.shoppinglist.persistence.JdbiUserRepository;
import com.bhegstam.shoppinglist.port.rest.*;
import com.bhegstam.webutil.webapp.ApplicationBase;
import spark.Service;

import java.util.List;

import static java.util.Arrays.asList;

public class Application extends ApplicationBase {

    private final ApplicationConfiguration conf;

    public static void main(String[] args) {
        new Application(new ApplicationConfiguration()).init();
    }

    public Application(ApplicationConfiguration conf) {
        super(
                List.of(
                        new IndexController(),
                        new LoginController(
                                new UserApplication(conf.getJdbi().onDemand(JdbiUserRepository.class))
                        )
                ),
                asList(
                        new ItemTypeApiController(
                                new ItemTypeApplication(conf.getJdbi().onDemand(JdbiItemTypeRepository.class))
                        ),
                        new ShoppingListApiController(
                                new ShoppingListApplication(
                                        conf.getJdbi().onDemand(JdbiShoppingListRepository.class),
                                        conf.getJdbi().onDemand(JdbiItemTypeRepository.class)
                                )
                        ),
                        new UserApiController(new UserApplication(conf.getJdbi().onDemand(JdbiUserRepository.class)))
                ),
                true,
                true
        );

        this.conf = conf;

        new DatabaseMigrator(conf.getDatabase()).migrateDatabase();
    }


    @Override
    protected void configureServer(Service http) {
        http.port(conf.getServer().getPort());

        http.staticFiles.location("/public");
        http.staticFiles.expireTime(600);

        http.after((request, response) -> response.header("Content-Encoding", "gzip"));
    }
}
