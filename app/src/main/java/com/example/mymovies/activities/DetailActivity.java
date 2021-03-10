package com.example.mymovies.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mymovies.R;
import com.example.mymovies.adapters.ReviewAdapter;
import com.example.mymovies.adapters.TrailerAdapter;
import com.example.mymovies.data.FavouriteFilms;
import com.example.mymovies.data.Film;
import com.example.mymovies.data.MainViewModel;
import com.example.mymovies.data.Review;
import com.example.mymovies.data.Trailer;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {

    private ImageView imageViewPoster;
    private TextView textViewTitle;
    private TextView textViewDate;
    private TextView textDescription;
    private TextView textViewRating;
    private TextView textViewOriginalTitle;
    private Film film;
    private FavouriteFilms favouriteFilm;
    private MainViewModel viewModel;
    private ImageView star;

    private RecyclerView recyclerViewTrailers;
    private RecyclerView recyclerViewReviews;
    private ReviewAdapter reviewAdapter;
    private TrailerAdapter trailerAdapter;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.itemMain:
                Intent intentToMain = new Intent(this, MainActivity.class);
                intentToMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentToMain);
                break;
            case R.id.itemFavourite:
                finish();

                Intent intentToFavourite = new Intent(this, FavouriteActivity.class);
                intentToFavourite.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentToFavourite);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        star = findViewById(R.id.imageViewAddToFavourite);
        textViewOriginalTitle = findViewById(R.id.textViewOriginalTitle);
        textViewRating = findViewById(R.id.textViewRating);
        textDescription = findViewById(R.id.textViewDescription);
        textViewDate = findViewById(R.id.textViewReleaseDate);
        textViewTitle = findViewById(R.id.textViewTitle);
        imageViewPoster = findViewById(R.id.imageViewBigPoster);
        recyclerViewTrailers = findViewById(R.id.recyclerViewTrailers);
        recyclerViewReviews = findViewById(R.id.recyclerViewReviews);

        int id = getIntent().getIntExtra("id", 0);

        viewModel = new ViewModelProvider(this, new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                if (modelClass.isAssignableFrom(MainViewModel.class))
                    return (T) new MainViewModel(getApplication());
                else {
                    throw new IllegalArgumentException();
                }
            }

        }).get(MainViewModel.class);
        if (getIntent().getIntExtra("flag", 0) == 1)
            film = viewModel.getFavouriteFilmById(id);
        else
            film = viewModel.getFilmById(id);
        if (film == null)
        {
            finish();
            return;
        }
        favouriteFilm = new FavouriteFilms(film);

        Picasso.get().load(film.getBigPosterPath()).into(imageViewPoster);
        textViewTitle.setText(film.getTitle());
        textViewDate.setText(film.getReleaseDAte());
        textDescription.setText(film.getOverview());
        textViewRating.setText(Double.toString(film.getVoteAverage()));
        textViewOriginalTitle.setText(film.getOriginalTitle());

        if (viewModel.getFavouriteFilmById(film.getId()) == null) {
            Picasso.get().load(R.drawable.nostar).into(star);
        } else {
            Picasso.get().load(R.drawable.star).into(star);
        }

        trailerAdapter = new TrailerAdapter();
        trailerAdapter.setOnTrailerClickListener(url -> {
            Intent intentToTrailer = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intentToTrailer);
        });

        reviewAdapter = new ReviewAdapter();
        recyclerViewReviews.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewTrailers.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewReviews.setAdapter(reviewAdapter);
        recyclerViewTrailers.setAdapter(trailerAdapter);

        viewModel.downloadTrailers(film.getId(), object ->
                trailerAdapter.setTrailers((ArrayList<Trailer>)object));

        viewModel.downloadReviews(film.getId(), object ->
                reviewAdapter.setReviews((ArrayList<Review>)object));

    }

    public void onClickImageStar(View view) {

        if (viewModel.getFavouriteFilmById(film.getId()) == null) {
            Picasso.get().load(R.drawable.star).into(star);
            viewModel.insertFavouriteFilm(favouriteFilm);
            Toast.makeText(this, R.string.add_to_favourite, Toast.LENGTH_SHORT).show();
        } else {
            Picasso.get().load(R.drawable.nostar).into(star);
            viewModel.deleteFavouriteFilm(favouriteFilm);
            Toast.makeText(this, R.string.remove_from_favourite, Toast.LENGTH_SHORT).show();
        }

    }
}