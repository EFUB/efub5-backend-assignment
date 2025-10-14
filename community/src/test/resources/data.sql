-- SET autocommit = 1;

CREATE table if not exists accounts(
    account_id bigint not null auto_increment,
    email varchar(60) not null unique,
    password varchar(255) not null,
    ninkname varchar(16) not null,
    bio varchar(255),
    status varchar(255) not null,
    primary key (account_id)
);