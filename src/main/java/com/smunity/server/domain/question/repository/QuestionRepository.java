package com.smunity.server.domain.question.repository;

import com.smunity.server.domain.question.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long>, QuestionQueryRepository {

    long countByAnswerIsNull();
}
