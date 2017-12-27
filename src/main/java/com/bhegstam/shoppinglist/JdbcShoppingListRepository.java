package com.bhegstam.shoppinglist;

import com.bhe.jooq.tables.records.ShoppingListRecord;
import com.bhegstam.db.DatabaseConnectionFactory;
import com.bhegstam.util.DatabaseUtil;
import com.google.inject.Inject;
import org.jooq.Condition;
import org.jooq.impl.DSL;

import java.util.List;

import static com.bhe.jooq.tables.ShoppingList.SHOPPING_LIST;
import static com.bhegstam.webutil.CustomCollectors.onlyElement;

public class JdbcShoppingListRepository implements ShoppingListRepository {
    private final DatabaseConnectionFactory connectionFactory;
    private final DatabaseUtil databaseUtil;

    @Inject
    public JdbcShoppingListRepository(DatabaseConnectionFactory connectionFactory, DatabaseUtil databaseUtil) {
        this.connectionFactory = connectionFactory;
        this.databaseUtil = databaseUtil;
    }


    @Override
    public ShoppingList createShoppingList(String name) {
        ShoppingList shoppingList = new ShoppingList(
                new ShoppingListId(),
                name
        );

        connectionFactory
                .withConnection(conn -> DSL
                        .using(conn)
                        .insertInto(SHOPPING_LIST)
                        .set(SHOPPING_LIST.ID, shoppingList.getId().getId())
                        .set(SHOPPING_LIST.NAME, name)
                        .execute());

        return shoppingList;
    }

    @Override
    public ShoppingList get(ShoppingListId id) {
        return findShoppingListsWhere(SHOPPING_LIST.ID.eq(id.getId()))
                .stream()
                .collect(onlyElement());
    }

    @Override
    public List<ShoppingList> getShoppingLists() {
        return findShoppingListsWhere();
    }

    private List<ShoppingList> findShoppingListsWhere(Condition... conditions) {
        return databaseUtil.findObjectsWhere(SHOPPING_LIST, this::mapRecordToShoppingList, conditions);
    }

    private ShoppingList mapRecordToShoppingList(ShoppingListRecord record) {
        return new ShoppingList(
                new ShoppingListId(record.getId()),
                record.getName()
        );
    }

}
