create table qna_question
(
    question_id bigint auto_increment
        primary key,
    created_at  datetime(6)  null,
    updated_at  datetime(6)  null,
    anonymous   bit          not null,
    content     longtext not null,
    title       varchar(255) not null,
    member_id   bigint       null,
    constraint FK1luni7huq5dib4ssoibwofk7b
        foreign key (member_id) references common_member (member_id)
);

