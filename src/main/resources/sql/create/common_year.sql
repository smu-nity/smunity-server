create table common_year
(
    year_id     bigint auto_increment
        primary key,
    culture     int          null,
    culture_cnt int          null,
    major_i     int          null,
    major_s     int          null,
    year_name   varchar(255) not null,
    total       int          null,
    year_value  int          null
);

