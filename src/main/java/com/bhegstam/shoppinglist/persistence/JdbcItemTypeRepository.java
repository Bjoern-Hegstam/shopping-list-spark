package com.bhegstam.shoppinglist.persistence;

import com.bhe.jooq.tables.records.ItemTypeRecord;
import com.bhegstam.shoppinglist.domain.ItemType;
import com.bhegstam.shoppinglist.domain.ItemTypeId;
import com.bhegstam.shoppinglist.domain.ItemTypeRepository;
import com.google.inject.Inject;
import org.jooq.impl.DSL;

import java.util.ArrayList;
import java.util.List;

import static com.bhe.jooq.tables.ItemType.ITEM_TYPE;
import static com.bhegstam.webutil.CustomCollectors.onlyElement;

public class JdbcItemTypeRepository implements ItemTypeRepository {
    private final DatabaseConnectionFactory connectionFactory;
    private final QueryUtil queryUtil;

    @Inject
    public JdbcItemTypeRepository(DatabaseConnectionFactory connectionFactory, QueryUtil queryUtil) {
        this.connectionFactory = connectionFactory;
        this.queryUtil = queryUtil;
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
        return queryUtil.selectObjects(
                dsl -> dsl
                        .selectFrom(ITEM_TYPE)
                        .where(ITEM_TYPE.ID.eq(id.getId()))
                        .fetch(this::mapRecordToItemType)
        ).stream().collect(onlyElement());
    }

    @Override
    public List<ItemType> findItemTypes(String nameStart, int limit) {
        queryUtil.selectObjects(
                sql -> sql
                        .selectFrom(ITEM_TYPE)
                        .where(ITEM_TYPE.NAME.startsWith(nameStart))
                        .orderBy(ITEM_TYPE.NAME)
                        .limit(limit)
                        .fetch(this::mapRecordToItemType)
        );


        List<ItemType> result = new ArrayList<>();

        connectionFactory
                .withConnection(conn -> {
                    List<ItemType> itemTypes = DSL
                            .using(conn)
                            .selectFrom(ITEM_TYPE)
                            .where(ITEM_TYPE.NAME.startsWith(nameStart))
                            .orderBy(ITEM_TYPE.NAME)
                            .limit(limit)
                            .fetch(this::mapRecordToItemType);

                    result.addAll(itemTypes);
                });

        return result;
    }

    @Override
    public List<ItemType> getItemTypes() {
        return queryUtil.selectObjects(
                dsl -> dsl
                        .selectFrom(ITEM_TYPE)
                        .orderBy(ITEM_TYPE.NAME)
                        .fetch(this::mapRecordToItemType)
        );
    }

    @Override
    public void deleteItemType(ItemTypeId itemTypeId) {
        connectionFactory
                .withConnection(conn -> DSL
                        .using(conn)
                        .deleteFrom(ITEM_TYPE)
                        .where(ITEM_TYPE.ID.eq(itemTypeId.getId()))
                        .execute()
                );
    }

    private ItemType mapRecordToItemType(ItemTypeRecord record) {
        return new ItemType(
                new ItemTypeId(record.getId()),
                record.getName()
        );
    }
}
