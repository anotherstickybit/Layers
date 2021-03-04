create table users (
    id bigserial primary key,
    login text not null unique,
    password text not null,
    name text not null,
    secret text not null,
    roles text[] not null default '{}',
    removed boolean not null default false,
    created timestamp not null default current_timestamp
);