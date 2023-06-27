create table client
(
    id bigserial primary key,
    name varchar(50),
    created_at timestamp with time zone default now()
);
