CREATE DEFINER=`root`@`localhost` PROCEDURE `add_movie`(
IN title varchar(100),
IN year int,
IN director varchar(100),
IN banner_url varchar(200),
IN trailer_url varchar(200),
IN first_name varchar(50),
IN last_name varchar(50),
IN dob date,
IN photo_url varchar(200),
IN genre_name varchar(32),
OUT return_message varchar(200) 
)
BEGIN
 DECLARE count INT DEFAULT 0;
    DECLARE movie_id INT DEFAULT 0;
    DECLARE star_id INT DEFAULT 0;
    DECLARE genre_id INT DEFAULT 0;
    
 SELECT movies.id, COUNT(*)
 INTO movie_id , count 
 FROM movies
 WHERE movies.title = title
	AND movies.year = year
	AND movies.director = director
GROUP BY movies.id;
    
 IF count>0 THEN
  SET @return_message = 'The movie already exist. ';
 ELSE 
  INSERT INTO movies (title,year,director,banner_url,trailer_url) 
  VALUES(title,year,director,banner_url,trailer_url);
  SET movie_id = LAST_INSERT_ID();
  SET @return_message = 'A new movie entry has been created.';
 END IF;
        
 SELECT id, COUNT(*)
 INTO star_id , count 
	FROM stars
 WHERE stars.first_name = first_name
   AND stars.last_name = last_name
   GROUP BY id;
        
 IF count=0 THEN
  INSERT INTO stars(first_name,last_name,dob,photo_url)
  VALUES(first_name,last_name,dob,photo_url);
  SET star_id = LAST_INSERT_ID();
  SET @return_message = concat(@return_message, 'A new star entry has been created.');
 END IF;
        
 SELECT id,count(*)
 INTO genre_id,count
 FROM genres
 WHERE genres.name=genre_name
 GROUP BY id;
        
 IF count=0 THEN
  INSERT INTO genres(name)
  VALUES(genre_name);
  SET genre_id = LAST_INSERT_ID();
  SET @return_message = concat(@return_message, 'A new genre entry has been created.');
 END IF;
        
 INSERT INTO genres_in_movies(genre_id,movie_id) VALUES(genre_id,movie_id);
 INSERT INTO stars_in_movies(star_id, movie_id) VALUES(star_id,movie_id);
 SET return_message = @return_message;
END