create table core_standard
(
    standard_id bigint auto_increment
        primary key,
    category    enum ('CULTURE', 'ETC', 'MAJOR_ADVANCED', 'MAJOR_OPTIONAL') not null,
    total       int                                                         not null,
    year_id     bigint                                                      null,
    constraint FK86m3qbv3u8r3ii5us4jrk92kf
        foreign key (year_id) references common_year (year_id)
);

