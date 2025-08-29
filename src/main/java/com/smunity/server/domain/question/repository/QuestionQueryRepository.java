package com.smunity.server.domain.question.repository;

import com.smunity.server.domain.question.entity.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface QuestionQueryRepository {

    Page<Question> findPage(Pageable pageable);
}
