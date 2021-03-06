drop table if exists group_permission CASCADE;
drop table if exists users CASCADE;
drop table if exists group_level CASCADE;
drop table if exists permissions CASCADE;
drop table if exists reservation CASCADE;
drop table if exists review_image CASCADE;
drop table if exists review CASCADE;
drop table if exists room_image CASCADE;
drop table if exists room CASCADE;

create table group_level
(
    id   bigint not null,
    name varchar(255) not null,
    primary key (id)
);

create table permissions
(
    id   bigint not null,
    name varchar(255) not null,
    primary key (id)
);

create table group_permission
(
    id            bigint not null,
    group_id      bigint not null,
    permission_id bigint not null,
    primary key (id),
    CONSTRAINT unq_group_id_permission_id UNIQUE (group_id, permission_id),
    CONSTRAINT fk_group_id_for_group_permission FOREIGN KEY (group_id) REFERENCES group_level (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT fk_permission_id_for_group_permission FOREIGN KEY (permission_id) REFERENCES permissions (id) ON DELETE RESTRICT ON UPDATE RESTRICT
);

create table users
(
    id            bigint NOT NULL AUTO_INCREMENT,
    created_at    timestamp,
    updated_at    timestamp,
    email         varchar(255),
    name          varchar(255),
    phone         varchar(255),
    profile_image varchar(255),
    provider      varchar(255),
    provider_id   varchar(255),
    group_id      bigint not null,
    primary key (id),
    CONSTRAINT unq_username UNIQUE (name),
    CONSTRAINT unq_provider_and_id UNIQUE (provider, provider_id),
    CONSTRAINT fk_group_id_for_user FOREIGN KEY (group_id) REFERENCES group_level (id) ON DELETE RESTRICT ON UPDATE RESTRICT
);

create table room
(
    id            bigint NOT NULL AUTO_INCREMENT,
    created_at    timestamp,
    updated_at    timestamp,
    address1      varchar(255),
    address2      varchar(255),
    charge        integer,
    description   varchar(255),
    is_deleted    boolean,
    name          varchar(255),
    reviewCount   bigint,
    reviewRating  double,
    bathroomCount integer,
    bedCount      integer,
    maxGuest      integer,
    roomCount     integer,
    room_type     varchar(255),
    user_id       bigint,
    primary key (id)
);

create table room_image
(
    id         bigint NOT NULL AUTO_INCREMENT,
    created_at timestamp,
    updated_at timestamp,
    path       varchar(255),
    room_id    bigint not null,
    primary key (id),
    CONSTRAINT fk_room_id_for_room_image FOREIGN KEY (room_id) REFERENCES room (id) ON DELETE RESTRICT ON UPDATE RESTRICT
);

create table reservation
(
    id                 varchar(255) not null,
    created_at         timestamp,
    updated_at         timestamp,
    end_date           date,
    reservation_status varchar(255),
    room_id            bigint       not null,
    start_date         date,
    term               integer,
    total_price        integer,
    user_id            bigint       not null,
    primary key (id)
);

create table review
(
    id             bigint NOT NULL AUTO_INCREMENT,
    created_at     timestamp,
    updated_at     timestamp,
    comment        varchar(255),
    rating         integer,
    reservation_id varchar(255),
    visible        boolean,
    primary key (id)
);

create table review_image
(
    id         bigint NOT NULL AUTO_INCREMENT,
    created_at timestamp,
    updated_at timestamp,
    path       varchar(255),
    review_id  bigint not null,
    primary key (id),
    CONSTRAINT fk_review_id_for_review_image FOREIGN KEY (review_id) REFERENCES review (id) ON DELETE RESTRICT ON UPDATE RESTRICT
);