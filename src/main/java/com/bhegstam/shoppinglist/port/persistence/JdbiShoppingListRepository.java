package com.bhegstam.shoppinglist.port.persistence;

import com.bhegstam.shoppinglist.domain.*;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.jdbi.v3.sqlobject.transaction.Transaction;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@RegisterRowMapper(ShoppingListMapper.class)
@RegisterRowMapper(ItemTypeMapper.class)
@RegisterRowMapper(ShoppingListItemMapper.class)
public interface JdbiShoppingListRepository extends ShoppingListRepository {
    @Transaction
    default void persist(ShoppingList shoppingList) {
        Optional<ShoppingList> listInDatabase = findWithItems(shoppingList.getId());
        if (listInDatabase.isPresent() && !listInDatabase.get().getUpdatedAt().equals(shoppingList.getUpdateAtOnFetch())) {
            throw new OptimisticLockingException(String.format(
                    "Cannot persist shopping list [%s], list in database was last updated [%s]",
                    shoppingList,
                    listInDatabase.get().getUpdatedAt()
            ));
        }

        if (shoppingList.insertRequired()) {
            createShoppingList(
                    shoppingList.getId(),
                    shoppingList.getName(),
                    Timestamp.from(shoppingList.getCreatedAt()),
                    Timestamp.from(shoppingList.getUpdatedAt())
            );
            shoppingList.markAsPersisted();
        } else if (shoppingList.updateRequired()) {
            updateShoppingList(
                    shoppingList.getId(),
                    shoppingList.getName(),
                    Timestamp.from(shoppingList.getUpdatedAt())
            );
            shoppingList.markAsPersisted();
        }

        // Add/update item types
        shoppingList
                .getItemTypes()
                .forEach(itemType -> {
                    if (itemType.insertRequired()) {
                        createItemType(
                                shoppingList.getId(),
                                itemType.getId(),
                                itemType.getName(),
                                Timestamp.from(itemType.getCreatedAt()),
                                Timestamp.from(itemType.getUpdatedAt())
                        );
                        itemType.markAsPersisted();
                    }
                });

        // Synchronize items
        shoppingList
                .getItems()
                .forEach(item -> {
                    if (item.insertRequired()) {
                        createItem(
                                item.getId(),
                                shoppingList.getId(),
                                item.getItemType().getId(),
                                item.getQuantity(),
                                item.isInCart(),
                                Timestamp.from(item.getCreatedAt()),
                                Timestamp.from(item.getUpdatedAt())
                        );
                        item.markAsPersisted();
                    } else if (item.updateRequired()) {
                        ShoppingListItem itemInDatabase = listInDatabase
                                .orElseThrow(() -> new ShoppingListNotFoundException(shoppingList.getId()))
                                .get(item.getId());
                        if (!itemInDatabase.getUpdatedAt().equals(item.getUpdateAtOnFetch())) {
                            throw new OptimisticLockingException(String.format(
                                    "Cannot persist item [%s], iten in database was last updated [%s]",
                                    item,
                                    itemInDatabase.getUpdatedAt()
                            ));
                        }

                        updateItem(
                                shoppingList.getId(),
                                item.getId(),
                                item.getQuantity(),
                                item.isInCart(),
                                Timestamp.from(item.getUpdatedAt())
                        );
                        item.markAsPersisted();
                    }
                });

        shoppingList
                .removedItemIds()
                .forEach(itemId -> deleteItem(shoppingList.getId(), itemId));
        shoppingList.clearRemovedItems();

        // Delete item types
        shoppingList
                .deletedItemTypeIds()
                .forEach(itemTypeId -> deleteItemType(shoppingList.getId(), itemTypeId));
        shoppingList.clearDeletedItemTypes();
    }

    @SqlUpdate("insert into shopping_list(id, name, created_at, updated_at) values (:listId.id, :name, :createdAt, :updatedAt)")
    void createShoppingList(
            @BindBean("listId") ShoppingListId listId,
            @Bind("name") String name,
            @Bind("createdAt") Timestamp createdAt,
            @Bind("updatedAt") Timestamp updatedAt
    );

    @SqlUpdate("update shopping_list set name = :name, updated_at = :updatedAt where id = :listId.id")
    void updateShoppingList(
            @BindBean("listId") ShoppingListId listId,
            @Bind("name") String name,
            @Bind("updatedAt") Timestamp updatedAt
    );

    @SqlUpdate("insert into item_type(shopping_list_id, id, name, created_at, updated_at) values (:listId.id, :itemTypeId.id, :name, :createdAt, :updatedAt)")
    void createItemType(
            @BindBean("listId") ShoppingListId listId,
            @BindBean("itemTypeId") ItemTypeId itemTypeId,
            @Bind("name") String name,
            @Bind("createdAt") Timestamp createdAt,
            @Bind("updatedAt") Timestamp updatedAt
    );

    @SqlUpdate("update item_type set name = :name, updated_at = :updatedAt where shopping_list_id = :listId.id and id = :itemTypeId.id")
    void updateItemType(
            @BindBean("listId") ShoppingListId listId,
            @BindBean("itemTypeId") ItemTypeId itemTypeId,
            @Bind("name") String name,
            @Bind("updatedAt") Timestamp updatedAt
    );

    @SqlUpdate("delete from item_type where shopping_list_id = :listId.id and id = :itemTypeId.id")
    void deleteItemType(
            @BindBean("listId") ShoppingListId listId,
            @BindBean("itemTypeId") ItemTypeId itemTypeId
    );

    @SqlUpdate("insert into shopping_list_item(id, shopping_list_id, item_type_id, quantity, in_cart, created_at, updated_at) values (:itemId.id, :listId.id, :itemTypeId.id, :quantity, :inCart, :createdAt, :updatedAt)")
    void createItem(
            @BindBean("itemId") ShoppingListItemId itemId,
            @BindBean("listId") ShoppingListId listId,
            @BindBean("itemTypeId") ItemTypeId itemTypeId,
            @Bind("quantity") int quantity,
            @Bind("inCart") boolean inCart,
            @Bind("createdAt") Timestamp createdAt,
            @Bind("updatedAt") Timestamp updatedAt
    );

    @SqlUpdate("update shopping_list_item set quantity = :quantity, in_cart = :inCart, updated_at = :updatedAt where id = :itemId.id and shopping_list_id = :listId.id")
    void updateItem(
            @BindBean("listId") ShoppingListId listId,
            @BindBean("itemId") ShoppingListItemId itemId,
            @Bind("quantity") int quantity,
            @Bind("inCart") boolean inCart,
            @Bind("updatedAt") Timestamp updatedAt
    );

    @SqlUpdate("delete from shopping_list_item where id = :itemId.id and shopping_list_id = :listId.id")
    void deleteItem(
            @BindBean("listId") ShoppingListId listId,
            @BindBean("itemId") ShoppingListItemId itemId
    );

    @Transaction
    default ShoppingList get(ShoppingListId listId) {
        return findWithItems(listId)
                .orElseThrow(() -> new ShoppingListNotFoundException(listId));
    }

    default Optional<ShoppingList> findWithItems(ShoppingListId listId) {
        ShoppingList shoppingList = getShoppingListEntity(listId);
        if (shoppingList == null) {
            return Optional.empty();
        }

        shoppingList.loadFromDb(getItemTypes(listId), getItems(listId));
        return Optional.of(shoppingList);
    }

    @SqlQuery("select * from shopping_list where id = :listId.id")
    ShoppingList getShoppingListEntity(@BindBean("listId") ShoppingListId listId);

    @SqlQuery("select * from item_type where shopping_list_id = :listId.id")
    List<ItemType> getItemTypes(@BindBean("listId") ShoppingListId listId);

    @SqlQuery("select " +
            "i.id i_id, " +
            "i.quantity i_quantity, " +
            "i.in_cart i_in_cart, " +
            "i.created_at i_created_at, " +
            "i.updated_at i_updated_at, " +
            "it.id it_id, " +
            "it.name it_name, " +
            "it.created_at it_created_at, " +
            "it.updated_at it_updated_at " +
            "from shopping_list_item i " +
            "join item_type it on it.id = i.item_type_id " +
            "where i.shopping_list_id = :listId.id " +
            "order by it.name")
    List<ShoppingListItem> getItems(@BindBean("listId") ShoppingListId listId);

    @Transaction
    default List<ShoppingList> getShoppingLists() {
        List<ShoppingList> lists = getShoppingLists_internal();
        lists.forEach(list -> list.loadFromDb(getItemTypes(list.getId()), getItems(list.getId())));
        return lists;
    }

    @SqlQuery("select * from shopping_list order by name")
    List<ShoppingList> getShoppingLists_internal();

    @Transaction
    default void delete(ShoppingListId listId) {
        Optional<ShoppingList> optionalShoppingList = findWithItems(listId);
        if (optionalShoppingList.isEmpty()) {
            return;
        }
        ShoppingList shoppingList = optionalShoppingList.get();

        if (shoppingList.getItems().isEmpty() && shoppingList.getItemTypes().isEmpty()) {
            deleteShoppingList(listId);
        } else {
            throw new ShoppingListDeleteNotAllowedException(listId);
        }
    }

    @SqlUpdate("delete from shopping_list where id = :listId.id")
    void deleteShoppingList(@BindBean("listId") ShoppingListId listId);
}
