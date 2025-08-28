package com.smunity.server.domain.question.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.smunity.server.domain.question.entity.Question;
import com.smunity.server.global.common.util.QueryDslUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.smunity.server.domain.question.entity.QQuestion.question;

@Repository
@RequiredArgsConstructor
public class QuestionQueryRepositoryImpl implements QuestionQueryRepository {

    private final JPAQueryFactory query;

    @Override
    public Page<Question> findPage(Pageable pageable) {
        List<Question> content = fetchContent(pageable);
        JPAQuery<Long> countQuery = buildCountQuery();
        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private List<Question> fetchContent(Pageable pageable) {
        return query.selectFrom(question)
                .leftJoin(question.answer).fetchJoin()
                .orderBy(QueryDslUtil.toOrderSpecifiers(question, pageable.getSort()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    private JPAQuery<Long> buildCountQuery() {
        return query.select(question.count())
                .from(question);
    }
}
