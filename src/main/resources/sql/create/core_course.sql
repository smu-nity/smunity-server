create table core_course
(
    course_id      bigint auto_increment
        primary key,
    category       enum ('CULTURE', 'ETC', 'MAJOR_ADVANCED', 'MAJOR_OPTIONAL')                                                                                                                                                                                                                                                                                 not null,
    credit         int                                                                                                                                                                                                                                                                                                                                         not null,
    domain         varchar(255)                                                                                                                                                                                                                                                                                                                                null,
    subject_name   varchar(255)                                                                                                                                                                                                                                                                                                                                not null,
    subject_number varchar(8)                                                                                                                                                                                                                                                                                                                                  not null,
    semester       varchar(255)                                                                                                                                                                                                                                                                                                                                not null,
    sub_domain     enum ('BALANCE_ART', 'BALANCE_BRIDGE', 'BALANCE_ENGINEER', 'BALANCE_HUMANITIES', 'BALANCE_NATURAL', 'BALANCE_NATURAL_ENGINEER', 'BALANCE_SOCIAL', 'BASIC_ACCIDENT', 'BASIC_COMPUTER', 'BASIC_COMPUTER_1', 'BASIC_COMPUTER_2', 'BASIC_ENG_MATH', 'CORE_CONVERGENCE', 'CORE_CREATIVE', 'CORE_DIVERSITY', 'CORE_ETHICAL', 'CORE_PROFESSIONAL') null,
    type           varchar(255)                                                                                                                                                                                                                                                                                                                                not null,
    subject_year   varchar(255)                                                                                                                                                                                                                                                                                                                                not null,
    member_id      bigint                                                                                                                                                                                                                                                                                                                                      null,
    constraint FKpmb6uj4m3s9o701125v933itu
        foreign key (member_id) references common_member (member_id)
);
