package com.bhegstam.shoppinglist.db;

import com.bhe.jooq.tables.records.ShoppingListRecord;
import com.bhegstam.db.DatabaseConnectionFactory;
import com.bhegstam.shoppinglist.domain.ShoppingList;
import com.bhegstam.shoppinglist.domain.ShoppingListId;
import com.bhegstam.shoppinglist.domain.ShoppingListItem;
import com.bhegstam.shoppinglist.domain.ShoppingListRepository;
import com.bhegstam.util.DatabaseUtil;
import com.bhegstam.util.db.PersistenceStatus;
import com.google.inject.Inject;
import org.jooq.Condition;
import org.jooq.impl.DSL;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.bhe.jooq.tables.ShoppingList.SHOPPING_LIST;
import static com.bhe.jooq.tables.ShoppingListItem.SHOPPING_LIST_ITEM;
import static com.bhegstam.webutil.CustomCollectors.onlyElement;
import static java.util.stream.Collectors.groupingBy;

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

    @Override
    public void update(ShoppingList shoppingList) {
        connectionFactory
                .withConnection(conn -> DSL
                        .using(conn)
                        .transaction(configuration -> {
                            shoppingList
                                    .removedItemIds()
                                    .forEach(itemId -> DSL
                                            .using(configuration)
                                            .deleteFrom(SHOPPING_LIST_ITEM)
                                            .where(SHOPPING_LIST_ITEM.ID.eq(itemId.getId()))
                                            .execute()
                                    );

                            Map<PersistenceStatus, List<ShoppingListItem>> items = shoppingList
                                    .getItems().stream()
                                    .collect(groupingBy(ShoppingListItem::getPersistenceStatus));

                            items.getOrDefault(PersistenceStatus.INSERT_REQUIRED, Collections.emptyList())
                                 .forEach(item -> DSL
                                         .using(configuration)
                                         .insertInto(SHOPPING_LIST_ITEM)
                                         .set(SHOPPING_LIST_ITEM.ID, item.getId().getId())
                                         .set(SHOPPING_LIST_ITEM.ITEM_TYPE_ID, item.getItemTypeId().getId())
                                         .set(SHOPPING_LIST_ITEM.QUANTITY, item.getQuantity())
                                         .set(SHOPPING_LIST_ITEM.IN_CART, item.isInCart())
                                         .execute()
                                 );

                            items.getOrDefault(PersistenceStatus.UPDATED_REQUIRED, Collections.emptyList())
                                 .forEach(item -> DSL
                                         .using(configuration)
                                         .update(SHOPPING_LIST_ITEM)
                                         .set(SHOPPING_LIST_ITEM.QUANTITY, item.getQuantity())
                                         .set(SHOPPING_LIST_ITEM.IN_CART, item.isInCart())
                                         .execute()
                                 );
                        })
                );
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
