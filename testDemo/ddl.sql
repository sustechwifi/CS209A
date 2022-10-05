
--DDL for creating tables
--Data should also follow the exact sequence of tables when inserting.
--
--There should be an empty (not null!) record in the container, ship, city and courier
--    table respectively.

--写在前面：DDL设计的基础思路是，尽量减少数据冗余的同时，尽量保证查询的高效
--观察csv文件中数据可以发现，city,company,ship,container,courier等数据有大量重复，故单独建表保存
--建表的思路为，先建立不依赖任何实体的表，然后建立依赖其存在的表，以此类推
--所有表建立自增的id列作为主键以统一查询模式

create table company(
                        name varchar(31) unique not null,
                        id serial primary key
);--company不依赖任何实体

create table container(
                          code varchar(15) unique not null,
                          type varchar(31) not null,
                          id serial primary key
);--container不依赖任何实体

create table city(
                     name varchar(31) unique not null,
                     id serial primary key
);--city不依赖任何实体

create table ship(
                     name varchar(31) unique not null,
                     company_id int not null,
                     id serial primary key,
                     foreign key (company_id) references company(id)
);--ship附属于company，因此外键连接到company(id)

create table courier(
                        phone_number varchar(16),
                        name varchar(20) not null,
                        age int,
                        gender int8,
    --0 for female
    --1 for male
                        company_id int,
                        city_id int,
                        id serial primary key,
                        unique (name,phone_number),
                        foreign key (company_id) references company(id),
                        foreign key (city_id) references city(id)
);--courier附属于company,且有可能有检索其所在城市的需求，因此连接到company(id)和city(id)
--考虑到可能有重名，故采用(name,phone_number)的约束

-- auto-generated definition
create table record
(
    log_time     timestamp         not null,
    item_name    varchar(18)       not null
        unique,
    item_class   varchar(18)       not null,
    item_price   bigint            not null,
    company_id   integer           not null
        references company,
    container_id integer
        references container,
    ship_id      integer
        references ship,
    id           serial
        primary key,
    state        integer default 0 not null
);

alter table record
    owner to postgres;
--record表为所有实体中依赖关系最多的，故在所有实体的最后建立，通过其应该能方便地查询到一条数据的任意信息
--故其需要连接到所有表，其中与container,ship,company为多对一的关系，故不用单独建表
--而与city,courier为多对多的关系，因此单独建表

create table handle(
                       type int not null,
    --type = 1: Retrieval
    --type = 4: Deliver
                       time date,
                       record_id int not null,
                       courier_id int,
                       id serial primary key,
                       foreign key (courier_id) references courier(id),
                       foreign key (record_id) references record(id)
);--连接record和courier，记录record中关于负责人的信息，考虑到可能随着需求的增加
-- 存在且不限于retrieval,deliver的关系，故将处理时间置于此表中，同时设置type字段以标明事件类型

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
                        id serial primary key,
                        foreign key (city_id) references city(id),
                        foreign key (record_id) references record(id)
);--连接record和city，设置了不限于4种的关系类型，根据事件类型的不同使用不同的字段
--尽管由此可能造成数据冗余，但是可以统一record和city之间的联系
--考虑这之间的关系，采用(type,city_id,record_id)的多元约束
create index handle_record_id on handle(record_id);
create index transit_record_id on transit(record_id);