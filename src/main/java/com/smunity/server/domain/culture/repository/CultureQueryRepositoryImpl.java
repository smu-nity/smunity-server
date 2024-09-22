package com.smunity.server.domain.culture.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.smunity.server.domain.culture.entity.Culture;
import com.smunity.server.global.common.entity.enums.SubDomain;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.smunity.server.domain.culture.entity.QCulture.culture;


@Repository
@RequiredArgsConstructor
public class CultureQueryRepositoryImpl implements CultureQueryRepository {

    private final JPAQueryFactory query;

    @Override
    public List<Culture> findBySubDomain(SubDomain subDomain) {
        return query.selectFrom(culture)
                .where(subDomainEq(subDomain))
                .fetch();
    }

    private BooleanExpression subDomainEq(SubDomain subDomain) {
        return subDomain != null ? culture.subDomain.eq(subDomain) : null;
    }
}
