package com.bhe.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BinaryOperator;
import java.util.stream.Collector;

public class CustomCollectors {
    public static <T> Collector<T, ?, Optional<T>> onlyOptionalElement() {
        return Collector.of(
                ArrayList::new,
                List::add,
                (BinaryOperator<List<T>>) (e1, e2) -> {
                    throw new IllegalArgumentException("More than one element found");
                },
                list -> {
                    if (list.size() > 1) {
                        throw new IllegalArgumentException("More than one element found");
                    }

                    return list.stream().findFirst();
                }
        );
    }

    public static <T> Collector<T, ?, T> onlyElement() {
        return Collector.of(
                ArrayList::new,
                List::add,
                (BinaryOperator<List<T>>) (e1, e2) -> {
                    throw new IllegalArgumentException("More than one element found");
                },
                list -> {
                    if (list.size() > 1) {
                        throw new IllegalArgumentException("More than one element found");
                    }

                    return list.stream().findFirst().orElseThrow(() -> new IllegalArgumentException("No element found"));
                }
        );
    }
}
