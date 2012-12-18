# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table album (
  id                        bigint auto_increment not null,
  name                      varchar(255),
  constraint pk_album primary key (id))
;

create table linked_account (
  id                        bigint auto_increment not null,
  user_id                   bigint,
  provider_user_id          varchar(255),
  provider_key              varchar(255),
  constraint pk_linked_account primary key (id))
;

create table photo (
  id                        bigint auto_increment not null,
  name                      varchar(255),
  path_normal               varchar(255),
  album_id_id               bigint,
  constraint pk_photo primary key (id))
;

create table security_role (
  id                        bigint auto_increment not null,
  role_name                 varchar(255),
  constraint pk_security_role primary key (id))
;

create table token_action (
  id                        bigint auto_increment not null,
  token                     varchar(255),
  target_user_id            bigint,
  type                      varchar(2),
  created                   datetime,
  expires                   datetime,
  constraint ck_token_action_type check (type in ('EV','PR')),
  constraint uq_token_action_token unique (token),
  constraint pk_token_action primary key (id))
;

create table users (
  id                        bigint auto_increment not null,
  email                     varchar(255),
  name                      varchar(255),
  last_login                datetime,
  active                    tinyint(1) default 0,
  email_validated           tinyint(1) default 0,
  constraint pk_users primary key (id))
;

create table user_permission (
  id                        bigint auto_increment not null,
  value                     varchar(255),
  constraint pk_user_permission primary key (id))
;


create table album_security_role (
  album_id                       bigint not null,
  security_role_id               bigint not null,
  constraint pk_album_security_role primary key (album_id, security_role_id))
;

create table album_photo (
  album_id                       bigint not null,
  photo_id                       bigint not null,
  constraint pk_album_photo primary key (album_id, photo_id))
;

create table users_security_role (
  users_id                       bigint not null,
  security_role_id               bigint not null,
  constraint pk_users_security_role primary key (users_id, security_role_id))
;

create table users_user_permission (
  users_id                       bigint not null,
  user_permission_id             bigint not null,
  constraint pk_users_user_permission primary key (users_id, user_permission_id))
;
alter table linked_account add constraint fk_linked_account_user_1 foreign key (user_id) references users (id) on delete restrict on update restrict;
create index ix_linked_account_user_1 on linked_account (user_id);
alter table photo add constraint fk_photo_albumId_2 foreign key (album_id_id) references album (id) on delete restrict on update restrict;
create index ix_photo_albumId_2 on photo (album_id_id);
alter table token_action add constraint fk_token_action_targetUser_3 foreign key (target_user_id) references users (id) on delete restrict on update restrict;
create index ix_token_action_targetUser_3 on token_action (target_user_id);



alter table album_security_role add constraint fk_album_security_role_album_01 foreign key (album_id) references album (id) on delete restrict on update restrict;

alter table album_security_role add constraint fk_album_security_role_securi_02 foreign key (security_role_id) references security_role (id) on delete restrict on update restrict;

alter table album_photo add constraint fk_album_photo_album_01 foreign key (album_id) references album (id) on delete restrict on update restrict;

alter table album_photo add constraint fk_album_photo_photo_02 foreign key (photo_id) references photo (id) on delete restrict on update restrict;

alter table users_security_role add constraint fk_users_security_role_users_01 foreign key (users_id) references users (id) on delete restrict on update restrict;

alter table users_security_role add constraint fk_users_security_role_securi_02 foreign key (security_role_id) references security_role (id) on delete restrict on update restrict;

alter table users_user_permission add constraint fk_users_user_permission_user_01 foreign key (users_id) references users (id) on delete restrict on update restrict;

alter table users_user_permission add constraint fk_users_user_permission_user_02 foreign key (user_permission_id) references user_permission (id) on delete restrict on update restrict;

# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table album;

drop table album_security_role;

drop table album_photo;

drop table linked_account;

drop table photo;

drop table security_role;

drop table token_action;

drop table users;

drop table users_security_role;

drop table users_user_permission;

drop table user_permission;

SET FOREIGN_KEY_CHECKS=1;

