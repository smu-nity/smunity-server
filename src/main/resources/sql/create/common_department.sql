create table common_department
(
    department_id bigint auto_increment
        primary key,
    college       varchar(255)                                                                                                                                                                                                                                                                                                                                not null,
    name          varchar(255)                                                                                                                                                                                                                                                                                                                                not null,
    type          enum ('BALANCE_ART', 'BALANCE_BRIDGE', 'BALANCE_ENGINEER', 'BALANCE_HUMANITIES', 'BALANCE_NATURAL', 'BALANCE_NATURAL_ENGINEER', 'BALANCE_SOCIAL', 'BASIC_ACCIDENT', 'BASIC_COMPUTER', 'BASIC_COMPUTER_1', 'BASIC_COMPUTER_2', 'BASIC_ENG_MATH', 'CORE_CONVERGENCE', 'CORE_CREATIVE', 'CORE_DIVERSITY', 'CORE_ETHICAL', 'CORE_PROFESSIONAL') not null,
    is_editable   bit                                                                                                                                                                                                                                                                                                                                         not null
);

