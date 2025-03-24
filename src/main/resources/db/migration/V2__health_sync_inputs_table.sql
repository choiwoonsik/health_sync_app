create table health_sync_entry_raws
(
    id        bigint     not null auto_increment,
    sync_id   bigint     not null comment '동기화 ID',
    source_id bigint     not null comment '출처 ID',
    raw_data  mediumtext not null comment '건강 동기화 entries JSON 데이터',
    primary key (id),
    index idx_record_key_health_sync_type (sync_id, source_id)
) engine = innodb
  default charset = utf8mb4
    comment '건강 동기화 입력 데이터';
