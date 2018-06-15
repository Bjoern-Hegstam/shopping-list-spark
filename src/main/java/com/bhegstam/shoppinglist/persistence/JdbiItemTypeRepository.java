package com.bhegstam.shoppinglist.persistence;

import com.bhegstam.shoppinglist.domain.ItemType;
import com.bhegstam.shoppinglist.domain.ItemTypeId;
import com.bhegstam.shoppinglist.domain.ItemTypeRepository;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

public interface JdbiItemTypeRepository extends ItemTypeRepository {
    @SqlUpdate("insert into itemType(id, name) values (:itemTypeid, :itemTypename)")
    void createItemType(@BindBean("itemType") ItemType itemType);

    @Override
    ItemType createItemType(String name);

    @Override
    ItemType get(ItemTypeId id);

    @Override
    List<ItemType> findItemTypes(String nameStart, int limit);

    @Override
    List<ItemType> getItemTypes();

    @Override
    void deleteItemType(ItemTypeId itemTypeId);
}
