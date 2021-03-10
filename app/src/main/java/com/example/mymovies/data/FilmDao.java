package com.example.mymovies.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface FilmDao {

    //General films
    @Query("SELECT * FROM films")
    LiveData<List<Film>> getAllFilms();

    @Query("SELECT * FROM films WHERE id == :filmId" )
    Film getFilmById(int filmId);

    @Query("DELETE FROM films")
    void deleteAllFilms();

    @Insert
    void insertFilm(Film film);

    @Delete
    void deleteFilm(Film film);

    //Favourite films
    @Query("SELECT * FROM favouriteFilms")
    LiveData<List<FavouriteFilms>> getAllFavouriteFilms();

    @Insert
    void insertFavouriteFilm(FavouriteFilms film);

    @Delete
    void deleteFavouriteFilm(FavouriteFilms film);

    @Query("SELECT * FROM favouriteFilms WHERE id == :filmId" )
    FavouriteFilms getFavouriteFilmById(int filmId);
}
