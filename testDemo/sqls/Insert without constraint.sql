create table company(
    name varchar(30) not null,
    id serial primary key
);

create table container(
    code varchar(15) not null,
    type varchar(30) not null,
    id serial primary key
);

create table city(
    name varchar(20) not null,
    id serial primary key
);

create table ship(
    name varchar(20) not null,
    company_id int not null,
    id serial primary key
);

create table courier(
    phone_number varchar(16),
    name varchar(20) not null,
    birth_year int,
    gender int8,
    --0 for female
    --1 for male
    company_id int,
    city_id int,
    id serial primary key
);

create table record(
    log_time timestamp not null,
    item_name varchar(18) not null,
    item_class varchar(18) not null,
    item_price bigint not null,
    company_id int not null,
    container_id int,
    ship_id int,
    id serial primary key,
    state integer default 0 not null
);

create table handle(
    type int not null,
    --type = 1: Retrieval
    --type = 4: Deliver
    time date,
    record_id int not null,
    courier_id int,
    id serial primary key
);

create table transit(
    type int not null,
    --type = 1: Retrieval
    --type = 2: Export City
    --type = 3: Import City
    --type = 4: Deliver
    --Can expand if there are more circumstances
    time date,
    tax numeric(24,6),
    city_id int,
    record_id int,
    id serial primary key
);