package com.smunity.server.domain.question.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.smunity.server.domain.question.entity.Question;
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
        List<Question> content = query
                .selectFrom(question)
                .leftJoin(question.answer).fetchJoin()
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = query
                .select(question.count())
                .from(question);
        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }
}
