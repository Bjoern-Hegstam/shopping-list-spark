package com.bhegstam.itemtype.db;

import com.bhe.jooq.tables.records.ItemTypeRecord;
import com.bhegstam.db.DatabaseConnectionFactory;
import com.bhegstam.itemtype.domain.ItemType;
import com.bhegstam.itemtype.domain.ItemTypeId;
import com.bhegstam.itemtype.domain.ItemTypeRepository;
import com.bhegstam.util.DatabaseUtil;
import com.google.inject.Inject;
import org.jooq.Condition;
import org.jooq.impl.DSL;

import java.util.List;

import static com.bhe.jooq.tables.ItemType.ITEM_TYPE;
import static com.bhegstam.webutil.CustomCollectors.onlyElement;

public class JdbcItemTypeRepository implements ItemTypeRepository {
    private final DatabaseConnectionFactory connectionFactory;
    private final DatabaseUtil databaseUtil;

    @Inject
    public JdbcItemTypeRepository(DatabaseConnectionFactory connectionFactory, DatabaseUtil databaseUtil) {
        this.connectionFactory = connectionFactory;
        this.databaseUtil = databaseUtil;
    }

    @Override
    public ItemType createItemType(String name) {
        ItemType itemType = new ItemType(
                new ItemTypeId(),
                name
        );

        connectionFactory
                .withConnection(conn -> DSL
                        .using(conn)
                        .insertInto(ITEM_TYPE)
                        .set(ITEM_TYPE.ID, itemType.getId().getId())
                        .set(ITEM_TYPE.NAME, itemType.getName())
                        .execute()
                );

        return itemType;
    }

    @Override
    public ItemType get(ItemTypeId id) {
        return findItemTypesWhere(ITEM_TYPE.ID.eq(id.getId()))
                .stream()
                .collect(onlyElement());
    }

    private List<ItemType> findItemTypesWhere(Condition... conditions) {
        return databaseUtil.findObjectsWhere(ITEM_TYPE, this::mapRecordToItemType, conditions);
    }

    private ItemType mapRecordToItemType(ItemTypeRecord record) {
        return new ItemType(
                new ItemTypeId(record.getId()),
                record.getName()
        );
    }
}
