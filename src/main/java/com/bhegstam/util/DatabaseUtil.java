package com.bhegstam.util;

import com.bhegstam.db.DatabaseConnectionFactory;
import com.google.inject.Inject;
import org.jooq.Condition;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.impl.DSL;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class DatabaseUtil {

    private final DatabaseConnectionFactory connectionFactory;

    @Inject
    public DatabaseUtil(DatabaseConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public  <RecordType extends Record, ObjectType> List<ObjectType> findObjectsWhere(
            Table<RecordType> table,
            Function<RecordType, ObjectType> objectMapper,
            Condition... conditions
    ) {
        List<ObjectType> objects = new ArrayList<>();

        connectionFactory
                .withConnection(conn -> DSL
                        .using(conn)
                        .selectFrom(table)
                        .where(conditions)
                        .fetch()
                        .stream()
                        .map(objectMapper)
                        .forEach(objects::add)
                );

        return objects;
    }
}
