select * from stars
where stars.id in (
	select stars_in_movies.star_id from stars_in_movies
    where stars_in_movies.movie_id = 910
)