create table health_syncs
(
    id               bigint       not null auto_increment,
    record_key       varchar(255) not null comment '레코드 키',
    health_sync_type varchar(30)  not null comment '건강 유형 [steps]',
    date_created     datetime(6)  not null comment '생성일시',
    date_updated     datetime(6)  not null comment '수정일시',
    primary key (id),
    unique key uk_record_key_health_sync_type (record_key, health_sync_type)
) engine = innodb
  default charset = utf8mb4
    comment '건강 동기화 유형 정보';

create table `health_sync_sources`
(
    id            bigint      not null auto_increment,
    sync_id       bigint      not null comment '건강 동기화 유형 PK',
    device_name   varchar(63) not null comment '출처 디바이스 이름',
    device_vender varchar(63) not null comment '출처 디바이스 제조사',
    source_mode   int         not null comment '출처 모드 번호',
    source_name   varchar(63) not null comment '출처명',
    source_type   varchar(63) not null comment '출처 유형',
    date_created  datetime(6) not null comment '생성일시',
    date_updated  datetime(6) not null comment '수정일시',
    primary key (id),
    index idx_sync_id (sync_id)
) engine = innodb
  default charset = utf8mb4
    comment '건강 동기화 데이터 출처 정보';

create table `health_sync_entries`
(
    id             bigint      not null auto_increment,
    sync_id        bigint      not null comment '건강 동기화 유형 PK',
    source_id      bigint      not null comment '건강 동기화 출처 PK',
    period_from    datetime(6) null comment '측정 시작 시간',
    period_to      datetime(6) null comment '측정 종료 시간',
    distance_value double      null comment '이동 거리 값',
    distance_unit  varchar(10) null comment '이동 거리 단위 (km)',
    calories_value double      null comment '소모 칼로리 값',
    calories_unit  varchar(10) null comment '칼로리 단위 (kcal)',
    activity_value double      null comment '활동 값',
    activity_type  varchar(10) null comment '활동 유형 (steps)',
    date_created   datetime(6) not null comment '생성일시',
    date_updated   datetime(6) not null comment '수정일시',
    primary key (id),
    index idx_sync_id (sync_id),
    index idx_source_id (source_id),
    index idx_sync_id_source_id (sync_id, source_id)
) engine = innodb
  default charset = utf8mb4
    comment '건강 동기화 데이터 정보';
