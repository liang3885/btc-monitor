create database btc_monitor;

use btc_monitor;


create table tx_out
(
    id                  bigint auto_increment
        primary key,
    block_number        bigint          null,
    tx_id              varchar(128)    null,
    out_index           int             null,
    receiver            varchar(64)     null,
    `value`              varchar(64)     null,
    spent            tinyint(1)      default false,
    created_at datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
);

create table progress
(
    id                  bigint auto_increment
        primary key,
    `key`   varchar(128)    null,
    `value`             bigint    null
)