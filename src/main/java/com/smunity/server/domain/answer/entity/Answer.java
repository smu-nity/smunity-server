package com.smunity.server.domain.answer.entity;

import com.smunity.server.domain.question.entity.Question;
import com.smunity.server.global.common.entity.BaseEntity;
import com.smunity.server.global.common.entity.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@Table(name = "qna_answer")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Answer extends BaseEntity {

    @Id
    @Column(name = "answer_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private Question question;

    public void setMember(Member member) {
        this.member = member;
        member.getAnswers().add(this);
    }

    public void setQuestion(Question question) {
        this.question = question;
        question.setAnswer(this);
    }

    public void setData(Member member, Question question) {
        setMember(member);
        setQuestion(question);
    }

    public void update(Member member, String content) {
        this.member = member;
        this.content = content;
    }
}
