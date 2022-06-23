DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS group_permission CASCADE;
DROP TABLE IF EXISTS groups CASCADE;
DROP TABLE IF EXISTS permissions CASCADE;

CREATE TABLE permissions
(
    id   bigint      NOT NULL,
    name varchar(20) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE groups
(
    id   bigint      NOT NULL,
    name varchar(20) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE group_permission
(
    id            bigint NOT NULL,
    group_id      bigint NOT NULL,
    permission_id bigint NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT unq_group_id_permission_id UNIQUE (group_id, permission_id),
    CONSTRAINT fk_group_id_for_group_permission FOREIGN KEY (group_id) REFERENCES groups (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT fk_permission_id_for_group_permission FOREIGN KEY (permission_id) REFERENCES permissions (id) ON DELETE RESTRICT ON UPDATE RESTRICT
);

CREATE TABLE users
(
    id            bigint      NOT NULL AUTO_INCREMENT,
    name          varchar(20) NOT NULL,
    profile_image varchar(255) DEFAULT NULL,
    email         varchar(255) DEFAULT NULL,
    phone         varchar(255) DEFAULT NULL,
    provider      varchar(20) NOT NULL,
    provider_id   varchar(80) NOT NULL,
    group_id      bigint      NOT NULL,
    created_at    timestamp,
    updated_at    timestamp,
    PRIMARY KEY (id),
    CONSTRAINT unq_username UNIQUE (name),
    CONSTRAINT unq_provider_and_id UNIQUE (provider, provider_id),
    CONSTRAINT fk_group_id_for_user FOREIGN KEY (group_id) REFERENCES groups (id) ON DELETE RESTRICT ON UPDATE RESTRICT
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
    term               integer,
    reservation_status varchar(255),
    room_id            bigint       not null,
    start_date         date,
    total_price        integer,
    user_id            bigint       not null,
    primary key (id)
);