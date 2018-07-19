DROP DATABASE periodical;

CREATE DATABASE periodical;
USE periodical;

create table categories
(
  id int not null auto_increment
    primary key,
  name varchar(50) not null,
  constraint id_UNIQUE
  unique (id),
  constraint nameCategory_UNIQUE
  unique (name)
)
;

create table editions
(
  id int not null auto_increment
    primary key,
  name varchar(100) not null,
  price int not null,
  categories_id int not null,
  constraint id_UNIQUE
  unique (id),
  constraint fk_editions_categories1
  foreign key (categories_id) references categories (id)
    on delete cascade
)
;

create index fk_editions_categories1_idx
  on editions (categories_id)
;

create table orders
(
  id int not null auto_increment
    primary key,
  bill int not null,
  users_id int not null,
  statuses_id int not null,
  constraint id_UNIQUE
  unique (id)
)
;

create index fk_orders_statuses1_idx
  on orders (statuses_id)
;

create index fk_orders_users1_idx
  on orders (users_id)
;

create table orders_editions
(
  orders_id int not null,
  editions_id int not null,
  constraint fk_orders_editions_orders1
  foreign key (orders_id) references orders (id),
  constraint fk_orders_editions_editions1
  foreign key (editions_id) references editions (id)
    on delete cascade
)
;

create index fk_orders_editions_editions1_idx
  on orders_editions (editions_id)
;

create index fk_orders_editions_orders1_idx
  on orders_editions (orders_id)
;

create table roles
(
  id int not null auto_increment
    primary key,
  name varchar(15) not null,
  constraint nameRole_UNIQUE
  unique (name)
)
;

create table statuses
(
  id int not null auto_increment
    primary key,
  name varchar(30) not null,
  constraint nameStatus_UNIQUE
  unique (name)
)
;

alter table orders
  add constraint fk_orders_statuses1
foreign key (statuses_id) references statuses (id)
;

create table user_lock
(
  id int not null auto_increment
    primary key,
  name varchar(15) not null,
  constraint id_UNIQUE
  unique (id)
)
;

create table users
(
  id int not null auto_increment
    primary key,
  login varchar(20) not null,
  password varchar(128) not null,
  first_name varchar(20) not null,
  last_name varchar(20) not null,
  email varchar(100) not null,
  roles_id int not null,
  bill int not null,
  user_lock_id int not null,
  constraint id_UNIQUE
  unique (id),
  constraint login_UNIQUE
  unique (login),
  constraint fk_users_roles1
  foreign key (roles_id) references roles (id)
    on delete cascade,
  constraint fk_users_user_lock1
  foreign key (user_lock_id) references user_lock (id)
    on delete cascade
)
;

create index fk_users_roles1_idx
  on users (roles_id)
;

create index fk_users_user_lock1_idx
  on users (user_lock_id)
;

alter table orders
  add constraint fk_orders_users1
foreign key (users_id) references users (id)
;

