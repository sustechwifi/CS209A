drop table commit;

drop table issue;

drop table release;

create table commit
(
    id      serial
        constraint commit_pk
            primary key,
    content text        not null,
    repo    varchar(63) not null,
    owner   varchar(63)
);

alter table commit
    owner to postgres;

create unique index commit_id_uindex
    on commit (id);

create table issue
(
    id      serial
        constraint issue_pk
            primary key,
    content text        not null,
    repo    varchar(63) not null,
    owner   varchar(63)
);

alter table issue
    owner to postgres;

create unique index issue_id_uindex
    on issue (id);


create table release
(
    id      serial
        constraint release_pk
            primary key,
    content text        not null,
    repo    varchar(63) not null,
    owner   varchar(63) not null
);

alter table release
    owner to postgres;

create unique index release_id_uindex
    on release (id);


