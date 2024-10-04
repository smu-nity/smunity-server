create table qna_answer
(
    answer_id   bigint auto_increment
        primary key,
    created_at  datetime(6)  null,
    updated_at  datetime(6)  null,
    content     longtext not null,
    member_id   bigint       null,
    question_id bigint       null,
    constraint UK8on2ssmkvwwg6ns24jq5v4kwr
        unique (question_id),
    constraint FKkuw8kdmsc09j21hmnv8ox1kus
        foreign key (question_id) references qna_question (question_id),
    constraint FKnl186rx460t55egnvi8vyb8i4
        foreign key (member_id) references common_member (member_id)
);

