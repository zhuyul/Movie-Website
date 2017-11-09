CREATE TABLE movies(
 id   int    not null auto_increment,
 title  varchar(100) not null,
 year  int    not null,
 director  varchar(100) not null,
 banner_url  varchar(200),
 trailer_url varchar(200),
 primary key (id)
);
CREATE TABLE stars(
 id   int    not null auto_increment,
 first_name varchar(50)  not null,
 last_name varchar(50)  not null,
 dob   date,
 photo_url varchar(200),
 primary key (id)
);
CREATE TABLE stars_in_movies(
 star_id  int    not null,
 movie_id int    not null,
 foreign key (star_id) references stars(id),
 foreign key (movie_id) references movies(id)
);
CREATE TABLE genres(
 id   int    not null auto_increment,
 name  varchar(32)  not null,
 primary key (id)
);
CREATE TABLE genres_in_movies(
 genre_id int    not null,
 movie_id int    not null,
 foreign key (genre_id) references genres(id),
 foreign key (movie_id) references movies(id)
);
CREATE TABLE creditcards(
 id   varchar(20)  not null,
 first_name varchar(50)  not null,
 last_name varchar(50)  not null,
 expiration date   not null,
 primary key (id)
);
CREATE TABLE customers(
 id   int    not null auto_increment,
 first_name varchar(50)  not null,
 last_name varchar(50)  not null,
 cc_id  varchar(20)  not null,
 address  varchar(200) not null,
 email  varchar(50)  not null,
 password varchar(20)  not null,
 primary key (id),
 foreign key (cc_id) references creditcards(id)
);
CREATE TABLE sales(
 id   int    not null auto_increment,
 customer_id int    not null,
 movie_id int    not null,
 sale_date date   not null,
 primary key (id),
 foreign key (customer_id) references customers(id),
 foreign key (movie_id) references movies(id)
);