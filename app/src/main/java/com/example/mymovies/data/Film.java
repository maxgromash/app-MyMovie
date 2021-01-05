package com.example.mymovies.data;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "films")
public class Film {
    @PrimaryKey(autoGenerate = true)
    private int uniqueId;
    private int id;
    private int voteCount;
    private String title;
    private String originalTitle;
    private String overview;
    private String bigPosterPath;
    private String posterPath;
    private String backDropPath;
    private double voteAverage;
    private String releaseDAte;

    public String getBigPosterPath() {
        return bigPosterPath;
    }

    public void setBigPosterPath(String bigPosterPath) {
        this.bigPosterPath = bigPosterPath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getBackDropPath() {
        return backDropPath;
    }

    public void setBackDropPath(String backDropPath) {
        this.backDropPath = backDropPath;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getReleaseDAte() {
        return releaseDAte;
    }

    public void setReleaseDAte(String releaseDAte) {
        this.releaseDAte = releaseDAte;
    }

    public int getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(int uniqueId) {
        this.uniqueId = uniqueId;
    }

    public Film(int uniqueId, int id, int voteCount, String title, String originalTitle, String overview, String posterPath,
                String backDropPath, double voteAverage, String releaseDAte, String bigPosterPath) {
        this.uniqueId = uniqueId;
        this.id = id;
        this.voteCount = voteCount;
        this.title = title;
        this.originalTitle = originalTitle;
        this.overview = overview;
        this.posterPath = posterPath;
        this.backDropPath = backDropPath;
        this.voteAverage = voteAverage;
        this.releaseDAte = releaseDAte;
        this.bigPosterPath = bigPosterPath;
    }

    @Ignore
    public Film(int id, int voteCount, String title, String originalTitle, String overview, String posterPath,
                String backDropPath, double voteAverage, String releaseDAte, String bigPosterPath) {
        this.id = id;
        this.voteCount = voteCount;
        this.title = title;
        this.originalTitle = originalTitle;
        this.overview = overview;
        this.posterPath = posterPath;
        this.backDropPath = backDropPath;
        this.voteAverage = voteAverage;
        this.releaseDAte = releaseDAte;
        this.bigPosterPath = bigPosterPath;
    }
}
