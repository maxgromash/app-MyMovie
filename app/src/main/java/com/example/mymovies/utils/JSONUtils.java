package com.example.mymovies.utils;

import com.example.mymovies.data.Film;
import com.example.mymovies.data.Review;
import com.example.mymovies.data.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JSONUtils {
    public static final String BASE_POSTER_URL = "https://image.tmdb.org/t/p/";
    public static final String SMALL_POSTER_SIZE = "w185";
    public static final String BIG_POSTER_SIZE = "w780";

    public static final String BASE_YOUTUBE_URL = "https://www.youtube.com/watch?v=";
    private static final String KEY_RESULTS = "results";
    private static final String KEY_REVIEW_CONTENT = "content";
    private static final String KEY_REVIEW_AUTHOR = "author";

    private static final String KEY_VIDEO_NAME = "name";
    private static final String KEY_VIDEO_KEY = "key";

    private static final String KEY_VOTE_COUNT = "vote_count";
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_ORIGINAL_TITLE = "original_title";
    private static final String KEY_OVERVIEW = "overview";
    private static final String KEY_POSTER_PATH = "poster_path";
    private static final String KEY_BACKDROP_PATH = "backdrop_path";
    private static final String KEY_AVERAGE = "vote_average";
    private static final String KEY_RELEASE_DATE = "release_date";

    public static ArrayList<Trailer> getTrailerFromJSON(JSONObject jsonObject){
        ArrayList<Trailer> trailers = new ArrayList<>();
        try {
            JSONArray jsonArray = jsonObject.getJSONArray(KEY_RESULTS);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject review = jsonArray.getJSONObject(i);
                trailers.add(new Trailer(review.getString(KEY_VIDEO_NAME),
                        BASE_YOUTUBE_URL + review.getString(KEY_VIDEO_KEY)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return trailers;
    }

    public static ArrayList<Review> getReviewsFromJSON(JSONObject jsonObject){
        ArrayList<Review> reviews = new ArrayList<>();
        try {
            JSONArray jsonArray = jsonObject.getJSONArray(KEY_RESULTS);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject review = jsonArray.getJSONObject(i);
                reviews.add(new Review(review.getString(KEY_REVIEW_AUTHOR),
                        review.getString(KEY_REVIEW_CONTENT)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return reviews;
    }

    /**
     * Метод для парсинга фильмов из JSON объекта
     * @param jsonObject JSON объект
     * @return массив фильмов
     */
    public static ArrayList<Film> getMoviesFromJSON(JSONObject jsonObject) {
        ArrayList<Film> films = new ArrayList<>();
        try {
            JSONArray jsonArray = jsonObject.getJSONArray(KEY_RESULTS);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject film = jsonArray.getJSONObject(i);
                films.add(new Film(film.getInt(KEY_ID), film.getInt(KEY_VOTE_COUNT), film.getString(KEY_TITLE),
                        film.getString(KEY_ORIGINAL_TITLE), film.getString(KEY_OVERVIEW),
                        BASE_POSTER_URL + SMALL_POSTER_SIZE + film.getString(KEY_POSTER_PATH),
                        film.getString(KEY_BACKDROP_PATH), film.getDouble(KEY_AVERAGE), film.getString(KEY_RELEASE_DATE),
                        BASE_POSTER_URL + BIG_POSTER_SIZE + film.getString(KEY_POSTER_PATH)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return films;
    }
}