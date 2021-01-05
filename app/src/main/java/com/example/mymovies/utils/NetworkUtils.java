package com.example.mymovies.utils;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class NetworkUtils {
    private static final String BASE_URL = "https://api.themoviedb.org/3/discover/movie";
    private static final String BASE_URL_VIDEOS = "https://api.themoviedb.org/3/movie/%s/videos";
    private static final String BASE_URL_REVIEWS = "https://api.themoviedb.org/3/movie/%s/reviews";

    private static final String PARAMS_API_KEY = "api_key";
    private static final String PARAMS_LANGUAGE = "language";
    private static final String PARAMS_SORT_BY = "sort_by";
    private static final String PARAMS_PAGE = "page";
    private static final String PARAMS_MIN_VOTE_COUNT = "vote_count.gte";

    private static final String API_KEY = "0746ed6eba982635274cd7aea94292a0";
    private static final String LANGUAGE_VALUE = "ru-RU";
    private static final String SORT_BY_POPULARITY = "popularity.desc";
    private static final String SORT_BY_VOTE = "vote_average.desc";
    private static final String MIN_VOTE_COUNT = "1000";

    public static final int POPULARITY = 0;
    public static final int VOTE = 1;


    private OnStartLoadingListener onStartLoadingListener;

    public void setOnStartLoadingListener(OnStartLoadingListener onStartLoadingListener) {
        this.onStartLoadingListener = onStartLoadingListener;
    }

    public interface OnStartLoadingListener{
        void onStartLoading();
    }

    public static URL buildURLtoReviews(int id){
        Uri uri = Uri.parse(String.format(BASE_URL_REVIEWS, id)).buildUpon()
                .appendQueryParameter(PARAMS_API_KEY, API_KEY).build();
                //.appendQueryParameter(PARAMS_LANGUAGE, LANGUAGE_VALUE)
        try {
            return new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JSONObject getJSONForReviews(int id) {
        URL url = buildURLtoReviews(id);
        JSONObject result = null;
        try {
            result = new JSONLoadTask().execute(url).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static URL buildURLtoVideos(int id){
        Uri uri = Uri.parse(String.format(BASE_URL_VIDEOS, id)).buildUpon()
                .appendQueryParameter(PARAMS_API_KEY, API_KEY)
                .appendQueryParameter(PARAMS_LANGUAGE, LANGUAGE_VALUE).build();
        try {
            return new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JSONObject getJSONForVideos(int id) {
        URL url = buildURLtoVideos(id);
        JSONObject result = null;
        try {
            result = new JSONLoadTask().execute(url).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return result;
    }




    /**
     * Публичный метод для получения JSON данных
     *
     * @param sortBy Метод сортировки
     * @param page   Номер страницы
     * @return JSON объект с данными
     */
    public static JSONObject getJSONFromNetwork(int sortBy, int page) {
        JSONObject result = null;
        URL url = buildURL(sortBy, page);
        try {
            result = new JSONLoadTask().execute(url).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Метод для создание URL
     *
     * @param sortBy метод соратировки
     * @param page   номер страницы
     * @return Созданный URL для JSON запроса
     */
    private static URL buildURL(int sortBy, int page) {
        URL result = null;
        String methodOfSort;
        if (sortBy == POPULARITY)
            methodOfSort = SORT_BY_POPULARITY;
        else
            methodOfSort = SORT_BY_VOTE;
        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(PARAMS_API_KEY, API_KEY)
                .appendQueryParameter(PARAMS_LANGUAGE, LANGUAGE_VALUE)
                .appendQueryParameter(PARAMS_SORT_BY, methodOfSort)
                .appendQueryParameter(PARAMS_PAGE, Integer.toString(page))
                .appendQueryParameter(PARAMS_MIN_VOTE_COUNT, MIN_VOTE_COUNT) .build();
        try {
            result = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static class JSONLoadTask extends AsyncTask<URL, Void, JSONObject> {
        /**
         * Переопределенный асинхронный метод загрузки данный в формате JSON по ссылке
         *
         * @param urls ссылка для загрузки
         * @return JSON объект с данными
         */
        @Override
        protected JSONObject doInBackground(URL... urls) {
            JSONObject result = null;
            HttpURLConnection httpURLConnection = null;
            try {
                httpURLConnection = (HttpURLConnection) urls[0].openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(reader);
                StringBuilder builder = new StringBuilder();
                String line = bufferedReader.readLine();
                while (line != null) {
                    builder.append(line);
                    line = bufferedReader.readLine();
                }
                result = new JSONObject(builder.toString());
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            } finally {
                if (httpURLConnection != null)
                    httpURLConnection.disconnect();
            }
            return result;
        }
    }
}
