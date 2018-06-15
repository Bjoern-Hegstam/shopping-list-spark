package com.bhegstam;

import com.bhegstam.shoppinglist.application.ItemTypeApplication;
import com.bhegstam.shoppinglist.application.ShoppingListApplication;
import com.bhegstam.shoppinglist.application.UserApplication;
import com.bhegstam.shoppinglist.configuration.ApplicationConfiguration;
import com.bhegstam.shoppinglist.configuration.property.Database;
import com.bhegstam.shoppinglist.configuration.property.Server;
import com.bhegstam.shoppinglist.persistence.DatabaseMigrator;
import com.bhegstam.shoppinglist.persistence.JdbiItemTypeRepository;
import com.bhegstam.shoppinglist.persistence.JdbiShoppingListRepository;
import com.bhegstam.shoppinglist.persistence.JdbiUserRepository;
import com.bhegstam.shoppinglist.port.rest.*;
import com.bhegstam.webutil.webapp.ApplicationBase;
import spark.Service;

import java.util.List;

import static com.bhegstam.shoppinglist.configuration.EnvironmentVariable.*;
import static java.util.Arrays.asList;

public class Application extends ApplicationBase {
    private static final int DEFAULT_PORT = 4567;

    private final ApplicationConfiguration conf;

    public static void main(String[] args) {
        ApplicationConfiguration conf = new ApplicationConfiguration(
                new Server(System.getenv().containsKey(PORT) ? Integer.parseInt(System.getenv(PORT)) : DEFAULT_PORT),
                new Database(
                        System.getenv(JDBC_DATABASE_URL),
                        System.getenv(JDBC_DATABASE_USERNAME),
                        System.getenv(JDBC_DATABASE_PASSWORD)
                )
        );

        new Application(conf).init();
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
