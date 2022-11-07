create sequence hibernate_sequence start 1 increment 1;
create table Address
(
    id int8 not null,
    street varchar(255),
    primary key (id)
);
create table Client
(
    id int8 not null,
    name varchar(255),
    address_id int8,
    primary key (id)
);
create table Phone
(
    id int8 not null,
    number varchar(255),
    client_id int8,
    primary key (id)
);
alter table if exists Client add constraint FOREIGN_KEY_ADDRESS_ID foreign key (address_id) references Address;
alter table if exists Phone add constraint FOREIGN_KEY_CLIENT_ID foreign key (client_id) references Client;

