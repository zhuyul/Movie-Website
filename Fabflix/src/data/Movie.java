package data;
import java.util.ArrayList;

import javax.servlet.ServletException;

import dao.ConnectionFactory;

import java.util.*;
import java.io.IOException;
import java.sql.*;


public class Movie {

	private int id;
	private String title;
	private int year;
	private String director;
	private String bannerUrl;
	private String trailerUrl;
	
	private ArrayList<Genre> genreList;
	
	private ArrayList<Star> starList;
	
	public Movie() {
		genreList = new ArrayList<Genre>();
		starList = new ArrayList<Star>();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public String getDirector() {
		return director;
	}

	public void setDirector(String director) {
		this.director = director;
	}

	public String getBannerUrl() {
		return bannerUrl;
	}

	public void setBannerUrl(String bannerUrl) {
		this.bannerUrl = bannerUrl;
	}

	public String getTrailerUrl() {
		return trailerUrl;
	}

	public void setTrailerUrl(String trailerUrl) {
		this.trailerUrl = trailerUrl;
	}

	public ArrayList<Genre> getGenreList() {
		return genreList;
	}
	
	public void addToGenreList(Genre genre) {
		this.genreList.add(genre);
	}

	public ArrayList<Star> getStarList() {
		return starList;
	}

	public void addToStarList(Star star) {
		this.starList.add(star);
	}
	
	@Override
	public String toString() {
		// print Movie info on console
		String result = String.format("id = %d, title = '%s', year = %d, director = '%s', banner_url = '%s', trailer_url = '%s'",
				id, title, year, director, bannerUrl, trailerUrl);
		if (!genreList.isEmpty()) {
			for (Genre genre : genreList) {
				result += "\n----genre_id = " + genre.getId() + ", genre_name = '" + genre.getName() + "'";
			}
		}
		return result;
	}
	
	
}
