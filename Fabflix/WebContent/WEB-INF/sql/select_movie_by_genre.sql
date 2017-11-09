select sql_calc_found_rows * from movies
where movies.id in (
	select movie_id from genres_in_movies
    where genre_id = (
		select genres.id from genres
        where genres.name = 'Action'
    )
) limit 1,10