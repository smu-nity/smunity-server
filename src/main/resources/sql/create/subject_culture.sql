create table subject_culture
(
    culture_id bigint auto_increment
        primary key,
    credit     int                                                                                                                                                                                                                                                                                                                                         not null,
    name       varchar(255)                                                                                                                                                                                                                                                                                                                                not null,
    number     varchar(255)                                                                                                                                                                                                                                                                                                                                not null,
    sub_domain enum ('BALANCE_ART', 'BALANCE_BRIDGE', 'BALANCE_ENGINEER', 'BALANCE_HUMANITIES', 'BALANCE_NATURAL', 'BALANCE_NATURAL_ENGINEER', 'BALANCE_SOCIAL', 'BASIC_ACCIDENT', 'BASIC_COMPUTER', 'BASIC_COMPUTER_1', 'BASIC_COMPUTER_2', 'BASIC_ENG_MATH', 'CORE_CONVERGENCE', 'CORE_CREATIVE', 'CORE_DIVERSITY', 'CORE_ETHICAL', 'CORE_PROFESSIONAL') not null
);

