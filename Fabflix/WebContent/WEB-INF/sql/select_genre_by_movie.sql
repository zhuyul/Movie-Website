select name from genres
where genres.id in (
	select genre_id from genres_in_movies
    where movie_id = 910
)