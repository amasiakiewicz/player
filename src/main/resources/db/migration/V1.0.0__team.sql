create table team
(
    id                uuid primary key,
    version           int                      not null,
    created_date_time timestamp with time zone not null
);
