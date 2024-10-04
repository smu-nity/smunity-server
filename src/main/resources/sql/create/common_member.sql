create table common_member
(
    member_id     bigint auto_increment
        primary key,
    created_at    datetime(6)                      null,
    updated_at    datetime(6)                      null,
    email         varchar(255)                     not null,
    name          varchar(255)                     not null,
    password      varchar(255)                     not null,
    role          enum ('ROLE_ADMIN', 'ROLE_USER') null,
    username      varchar(255)                     not null,
    department_id bigint                           null,
    year_id       bigint                           null,
    constraint UKnukl4wikhup994cvb6stc4o37
        unique (username),
    constraint FK8hk760o00jjiko78mr1ge6vs9
        foreign key (year_id) references common_year (year_id),
    constraint FKq0jyek3fap9mfymxujtk4rlsx
        foreign key (department_id) references common_department (department_id)
);

