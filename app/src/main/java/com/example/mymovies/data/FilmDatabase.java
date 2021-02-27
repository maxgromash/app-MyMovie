package com.example.mymovies.data;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities =  {Film.class, FavouriteFilms.class}, version = 1, exportSchema = false)
public abstract class FilmDatabase extends RoomDatabase {
    private static  FilmDatabase database;
    private static final String DATABASE_NAME = "mymovie.db";
    private static final Object LOCK = new Object();

    public static FilmDatabase getInstance(Context context) {
        synchronized (LOCK) {
            if (database == null) {
                database = Room.databaseBuilder(context, FilmDatabase.class, DATABASE_NAME).build();
            }
        }
        return database;
    }

    //Доступ к операциям с базой
    public abstract FilmDao filmDao();
}
