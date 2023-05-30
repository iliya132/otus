

create table if not exists address (
    id bigint primary key,
    street text not null,
    client_id bigint unique, --guarantee one to one
    foreign key (client_id) references client(id)
);

create table if not exists phone (
    id bigint primary key,
    number varchar(18) unique,
    client_id bigint,
    foreign key (client_id) references client(id)
);

create sequence address_seq start with 1 increment by 1 owned by address.id;
create sequence phone_seq start with 1 increment by 1 owned by phone.id;