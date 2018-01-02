package com.bhegstam.util;

import com.bhegstam.db.DatabaseConnectionFactory;
import com.google.inject.Inject;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class QueryUtil {

    private final DatabaseConnectionFactory connectionFactory;

    @Inject
    public QueryUtil(DatabaseConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public <T> List<T> selectObjects(Function<DSLContext, List<T>> queryFunction) {
        List<T> objects = new ArrayList<>();

        connectionFactory
                .withConnection(conn -> {
                            List<T> result = queryFunction.apply(DSL.using(conn));
                            objects.addAll(result);
                        }
                );

        return objects;
    }
}
