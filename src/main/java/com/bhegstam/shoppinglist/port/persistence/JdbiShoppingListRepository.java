package com.bhegstam.shoppinglist.port.persistence;

import com.bhegstam.shoppinglist.domain.*;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.jdbi.v3.sqlobject.transaction.Transaction;

import java.util.List;

@RegisterRowMapper(ShoppingListMapper.class)
@RegisterRowMapper(ShoppingListItemMapper.class)
public interface JdbiShoppingListRepository extends ShoppingListRepository {
    @Transaction
    default void persist(ShoppingList shoppingList) {
        if (shoppingList.getPersistenceStatus() == PersistenceStatus.INSERT_REQUIRED) {
            createShoppingList(shoppingList.getId(), shoppingList.getName());
            shoppingList.setPersistenceStatus(PersistenceStatus.PERSISTED);
        } else if (shoppingList.getPersistenceStatus() == PersistenceStatus.UPDATED_REQUIRED) {
            updateShoppingList(shoppingList.getId(), shoppingList.getName());
            shoppingList.setPersistenceStatus(PersistenceStatus.PERSISTED);
        }

        shoppingList
                .getItems()
                .forEach(item -> {
                    if (item.getPersistenceStatus() == PersistenceStatus.INSERT_REQUIRED) {
                        createItem(item.getId(), shoppingList.getId(), item.getItemType().getId(), item.getQuantity(), item.isInCart());
                        item.setPersistenceStatus(PersistenceStatus.PERSISTED);
                    } else if (item.getPersistenceStatus() == PersistenceStatus.UPDATED_REQUIRED) {
                        updateItem(shoppingList.getId(), item.getId(), item.getQuantity(), item.isInCart());
                        item.setPersistenceStatus(PersistenceStatus.PERSISTED);
                    }
                });

        shoppingList
                .removedItemIds()
                .forEach(itemId -> deleteItem(shoppingList.getId(), itemId));
    }

    @SqlUpdate("update shopping_list set name = :name where id = :listId.id")
    void updateShoppingList(@BindBean("listId") ShoppingListId listId, @Bind("name") String name);

    @SqlUpdate("insert into shopping_list(id, name) values (:listId.id, :name)")
    void createShoppingList(@BindBean("listId") ShoppingListId listId, @Bind("name") String name);

    @SqlUpdate("insert into shopping_list_item(id, shopping_list_id, item_type_id, quantity, in_cart) values (:itemId.id, :listId.id, :itemTypeId.id, :quantity, :inCart)")
    void createItem(@BindBean("itemId") ShoppingListItemId itemId, @BindBean("listId") ShoppingListId listId, @BindBean("itemTypeId") ItemTypeId itemTypeId, @Bind("quantity") int quantity, @Bind("inCart") boolean inCart);

    @SqlUpdate("update shopping_list_item set quantity = :quantity, in_cart = :inCart where id = :itemId.id and shopping_list_id = :listId.id")
    void updateItem(@BindBean("listId") ShoppingListId listId, @BindBean("itemId") ShoppingListItemId itemId, @Bind("quantity") int quantity, @Bind("inCart") boolean inCart);

    @SqlUpdate("delete from shopping_list_item where id = :itemId.id and shopping_list_id = :listId.id")
    void deleteItem(@BindBean("listId") ShoppingListId listId, @BindBean("itemId") ShoppingListItemId itemId);

    @Transaction
    default ShoppingList get(ShoppingListId listId) {
        ShoppingList shoppingList = find(listId);
        if (shoppingList != null) {
            return shoppingList;
        }
        throw new ShoppingListNotFoundException(listId);
    }

    default ShoppingList find(ShoppingListId listId) {
        ShoppingList shoppingList = getShoppingList(listId);
        if (shoppingList == null) {
            return null;
        }

        shoppingList.setItems(getItems(listId));
        return shoppingList;
    }

    @SqlQuery("select * from shopping_list where id = :listId.id")
    ShoppingList getShoppingList(@BindBean("listId") ShoppingListId listId);

    @SqlQuery("select i.id i_id, i.quantity i_quantity, i.in_cart i_in_cart, it.id it_id, it.name it_name from shopping_list_item i join item_type it on it.id = i.item_type_id where i.shopping_list_id = :listId.id order by it.name")
    List<ShoppingListItem> getItems(@BindBean("listId") ShoppingListId listId);

    @Transaction
    default List<ShoppingList> getShoppingLists() {
        List<ShoppingList> lists = getShoppingLists_internal();
        lists.forEach(list -> list.setItems(getItems(list.getId())));
        return lists;
    }

    @SqlQuery("select * from shopping_list order by name")
    List<ShoppingList> getShoppingLists_internal();

    @Transaction
    default void delete(ShoppingListId listId) {
        ShoppingList shoppingList = find(listId);
        if (shoppingList == null) {
            return;
        }

        if (shoppingList.getItems().isEmpty()) {
            deleteShoppingList(listId);
        } else {
            throw new ShoppingListDeleteNotAllowedException(listId);
        }
    }

    @SqlUpdate("delete from shopping_list where id = :listId.id")
    void deleteShoppingList(@BindBean("listId") ShoppingListId listId);
}
