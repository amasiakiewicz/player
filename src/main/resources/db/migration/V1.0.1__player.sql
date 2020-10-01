create table player
(
    id                uuid primary key,
    version           int                      not null,
    created_date_time timestamp with time zone not null,
    name              varchar(50)              not null,
    number            int                      not null,
    team_id           uuid                     not null references team (id),
    date_of_birth     date                     not null,
    play_start        date                     not null,
    college           varchar(50)              not null
);
