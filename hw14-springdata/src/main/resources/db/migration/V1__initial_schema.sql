drop table if exists client, address, nickname, phone;

create table client
(
    id           bigserial   not null primary key,
    name         varchar(50) not null
);

create table address
(
    client_id   bigint      not null references client (id),
    street      varchar(50) not null
);

create table nickname
(
    id          bigserial   not null primary key,
    nick        varchar(50) not null,
    client_id   bigint references client (id)
);

create table phone
(
    id          bigserial   not null primary key,
    number      varchar(50) not null,
    client_id   bigint references client (id)
);






