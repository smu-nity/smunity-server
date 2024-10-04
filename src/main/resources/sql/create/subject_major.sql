create table subject_major
(
    major_id      bigint auto_increment
        primary key,
    category      enum ('CULTURE', 'ETC', 'MAJOR_ADVANCED', 'MAJOR_OPTIONAL') not null,
    credit        int                                                         not null,
    grade         enum ('ALL', 'FIRST', 'FOURTH', 'SECOND', 'THIRD')          not null,
    name          varchar(255)                                                not null,
    number        varchar(255)                                                not null,
    semester      enum ('FIRST', 'SECOND')                                    not null,
    type          varchar(255)                                                not null,
    department_id bigint                                                      not null,
    constraint FKgh0dk1i0xbosxqa5k3qdqp4mg
        foreign key (department_id) references common_department (department_id)
);

