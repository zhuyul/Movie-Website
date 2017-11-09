CREATE TABLE movies_ft(
 id   int    not null auto_increment,
 title  varchar(100) not null,
 year  int    not null,
 director  varchar(100) not null,
 banner_url  varchar(200),
 trailer_url varchar(200),
 primary key (id),
 FULLTEXT (title)
) ENGINE=MyISAM;