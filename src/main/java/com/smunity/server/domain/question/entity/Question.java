package com.smunity.server.domain.question.entity;

import com.smunity.server.domain.answer.entity.Answer;
import com.smunity.server.global.common.entity.BaseEntity;
import com.smunity.server.global.common.entity.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@Table(name = "qna_question")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Question extends BaseEntity {

    @Id
    @Column(name = "question_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private boolean anonymous;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private Member author;

    @OneToOne(mappedBy = "question", fetch = FetchType.LAZY)
    private Answer answer;

    public void setAnswer(Answer answer) {
        this.answer = answer;
    }
}
