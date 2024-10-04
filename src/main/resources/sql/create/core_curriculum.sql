create table core_curriculum
(
    curriculum_id bigint auto_increment
        primary key,
    domain        enum ('BALANCE', 'BASIC', 'CORE')                                                                                                                                                                                                                                                                                                           not null,
    sub_domain    enum ('BALANCE_ART', 'BALANCE_BRIDGE', 'BALANCE_ENGINEER', 'BALANCE_HUMANITIES', 'BALANCE_NATURAL', 'BALANCE_NATURAL_ENGINEER', 'BALANCE_SOCIAL', 'BASIC_ACCIDENT', 'BASIC_COMPUTER', 'BASIC_COMPUTER_1', 'BASIC_COMPUTER_2', 'BASIC_ENG_MATH', 'CORE_CONVERGENCE', 'CORE_CREATIVE', 'CORE_DIVERSITY', 'CORE_ETHICAL', 'CORE_PROFESSIONAL') not null,
    year_id       bigint                                                                                                                                                                                                                                                                                                                                      null,
    constraint FK6efpcybxp00n1ny29xpqm6p1d
        foreign key (year_id) references common_year (year_id)
);

