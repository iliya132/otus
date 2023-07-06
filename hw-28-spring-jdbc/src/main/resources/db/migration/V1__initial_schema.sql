create table client
(
    id   bigserial primary key,
    name varchar(50)
);

create table address
(
    id        bigserial primary key,
    street    text,
    client_id bigint references client (id)
);

create table phone
(
    id        bigserial primary key,
    number    text,
    client_id bigint references client (id)
);
