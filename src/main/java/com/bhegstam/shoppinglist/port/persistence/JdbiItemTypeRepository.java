package com.bhegstam.shoppinglist.port.persistence;

import com.bhegstam.shoppinglist.domain.ItemType;
import com.bhegstam.shoppinglist.domain.ItemTypeId;
import com.bhegstam.shoppinglist.domain.ItemTypeRepository;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

@RegisterRowMapper(ItemTypeMapper.class)
public interface JdbiItemTypeRepository extends ItemTypeRepository {
    @SqlUpdate("insert into item_type(id, name) values (:itemType.id.id, :itemType.name)")
    void add(@BindBean("itemType") ItemType itemType);

    default ItemType get(ItemTypeId id) {
        ItemType itemType = getItemType(id);
        if (itemType == null) {
            throw new ItemTypeNotFoundException(id);
        }

        return itemType;
    }

    @SqlQuery("select * from item_type where id = :itemTypeId.id")
    ItemType getItemType(@BindBean("itemTypeId") ItemTypeId id);

    @SqlQuery("select * from item_type where name like ':nameStart%' order by name limit :limit")
    List<ItemType> findItemTypes(@Bind("nameStart") String nameStart, @Bind("limit") int limit);

    @SqlQuery("select * from item_type order by name")
    List<ItemType> getItemTypes();

    @SqlUpdate("delete from item_type where id = :itemTypeId.id")
    void deleteItemType(@BindBean("itemTypeId") ItemTypeId itemTypeId);
}
