package com.example.mymovies.data;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.mymovies.utils.JSONUtils;
import com.example.mymovies.utils.NetworkUtils;
import com.example.mymovies.utils.OnDownloadCompleted;
import com.example.mymovies.utils.OnLoadingCompleted;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainViewModel extends AndroidViewModel  {

    private static FilmDatabase database;
    private LiveData<List<Film>> films;
    private LiveData<List<FavouriteFilms>> favouriteFilms;

    public MainViewModel(@NonNull Application application) {
        super(application);
        database = FilmDatabase.getInstance(getApplication());
        films = database.filmDao().getAllFilms();
        favouriteFilms = database.filmDao().getAllFavouriteFilms();
    }

    public void downloadReviews(int id, OnLoadingCompleted callback){
        NetworkUtils.getJSONForReviews(id, jsonObject ->
                callback.onTaskCompleted(JSONUtils.getReviewsFromJSON(jsonObject)));
    }

    public void downloadTrailers(int id, OnLoadingCompleted callback){
        NetworkUtils.getJSONForVideos(id, jsonObject ->
                callback.onTaskCompleted(JSONUtils.getTrailerFromJSON(jsonObject)));
    }

    public void downloadData(int methodOfSort, int page, OnLoadingCompleted callback) {
        NetworkUtils.getJSONFromNetwork(methodOfSort, page, jsonObject -> {
            ArrayList<Film> films = JSONUtils.getMoviesFromJSON(jsonObject);
            if (films != null && !films.isEmpty()) {
                if (page == 1)
                    deleteAllFilms();
                for (Film film : films) {
                    insertFilm(film);
                }
            }
            callback.onTaskCompleted(null);
        });
    }

    public LiveData<List<Film>> getFilms() {
        return films;
    }

    public LiveData<List<FavouriteFilms>> getFavouriteFilms() {
        return favouriteFilms;
    }
    public Film getFilmById(int id) {
        try {
            return new GetFilmAsyncTask().execute(id).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void insertFilm(Film film) {
        new InsertFilmAsyncTask().execute(film);
    }

    public void deleteFilm(Film film) {
        new DeleteFilmAsyncTask().execute(film);
    }

    public void deleteAllFilms() {
        new DeleteAllFilmsAsyncTask().execute();
    }

    public void insertFavouriteFilm(FavouriteFilms film) {
        new InsertFavouriteFilmAsyncTask().execute(film);
    }

    public void deleteFavouriteFilm(FavouriteFilms film) {
        new DeleteFavouriteFilmAsyncTask().execute(film);
    }

    public Film getFavouriteFilmById(int id) {
        try {
            return new GetFavouriteFilmAsyncTask().execute(id).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    //Все операции с базой - асинхронные
    private class GetFilmAsyncTask extends AsyncTask<Integer, Void, Film> {

        @Override
        protected Film doInBackground(Integer... integers) {
            return database.filmDao().getFilmById(integers[0]);
        }
    }

    private class InsertFilmAsyncTask extends AsyncTask<Film, Void, Void> {

        @Override
        protected Void doInBackground(Film... films) {
            if (films != null && films.length > 0)
                database.filmDao().insertFilm(films[0]);
            return null;
        }
    }

    private class DeleteFilmAsyncTask extends AsyncTask<Film, Void, Void> {

        @Override
        protected Void doInBackground(Film... films) {
            if (films != null && films.length > 0)
                database.filmDao().deleteFilm(films[0]);
            return null;
        }
    }

    private class DeleteAllFilmsAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            database.filmDao().deleteAllFilms();
            return null;
        }
    }

    private class InsertFavouriteFilmAsyncTask extends AsyncTask<FavouriteFilms, Void, Void> {

        @Override
        protected Void doInBackground(FavouriteFilms... films) {
            if (films != null && films.length > 0)
                database.filmDao().insertFavouriteFilm(films[0]);
            return null;
        }
    }

    private class DeleteFavouriteFilmAsyncTask extends AsyncTask<FavouriteFilms, Void, Void> {

        @Override
        protected Void doInBackground(FavouriteFilms... films) {
            if (films != null && films.length > 0)
                database.filmDao().deleteFavouriteFilm(films[0]);
            return null;
        }
    }

    private class GetFavouriteFilmAsyncTask extends AsyncTask<Integer, Void, FavouriteFilms> {

        @Override
        protected FavouriteFilms doInBackground(Integer... integers) {
            return database.filmDao().getFavouriteFilmById(integers[0]);
        }
    }

}
