package com.smunity.server.global.common.util;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.ComparableExpressionBase;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.PathBuilder;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

public class QueryDslUtil {

    public static <T> OrderSpecifier<?>[] toOrderSpecifiers(EntityPathBase<T> qClass, Sort sort) {
        PathBuilder<T> pathBuilder = new PathBuilder<>(qClass.getType(), qClass.getMetadata());
        return sort.stream()
                .map(order -> toOrderSpecifier(order, pathBuilder))
                .toArray(OrderSpecifier[]::new);
    }

    private static <T> OrderSpecifier<?> toOrderSpecifier(Order order, PathBuilder<T> pathBuilder) {
        ComparableExpressionBase<?> path = pathBuilder.getComparable(order.getProperty(), Comparable.class);
        return order.isAscending() ? path.asc() : path.desc();
    }
}
