create table manager
(
    id   bigserial primary key,
    label text,
    param_1 text
);
create table client
(
    id   bigserial not null primary key,
    name varchar(50)
);
