package com.example.mymovies.data;

import androidx.room.Entity;
import androidx.room.Ignore;

@Entity(tableName = "favouriteFilms")
public class FavouriteFilms extends Film {
    public FavouriteFilms(int uniqueId ,int id, int voteCount, String title, String originalTitle, String overview,
                          String posterPath, String backDropPath, double voteAverage, String releaseDAte, String bigPosterPath) {
        super(uniqueId, id, voteCount, title, originalTitle, overview, posterPath, backDropPath, voteAverage, releaseDAte, bigPosterPath);
    }

    @Ignore
    public FavouriteFilms(Film film){
        super(film.getUniqueId(), film.getId(), film.getVoteCount(), film.getTitle(), film.getOriginalTitle(), film.getOverview(),
                film.getPosterPath(), film.getBackDropPath(), film.getVoteAverage(), film.getReleaseDAte(), film.getBigPosterPath());
    }
}
