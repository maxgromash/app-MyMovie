package com.example.mymovies.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.mymovies.R;
import com.example.mymovies.adapters.FilmAdapter;
import com.example.mymovies.data.FavouriteFilms;
import com.example.mymovies.data.Film;
import com.example.mymovies.data.MainViewModel;

import java.util.ArrayList;
import java.util.List;

public class FavouriteActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FilmAdapter adapter;
    private MainViewModel viewModel;
    private TextView textView;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.itemMain) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);

        textView = findViewById(R.id.textViewNoFavourite);

        //Инициализируем viewModel
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
        recyclerView = findViewById(R.id.recyclerViewFavouriteFilms);
        adapter = new FilmAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        adapter.setOnPosterClickListener(position -> {
            Film film = adapter.getFilms().get(position);
            Intent intent = new Intent(FavouriteActivity.this, DetailActivity.class);
            intent.putExtra("id", film.getId());
            intent.putExtra("flag", 1);
            startActivity(intent);
        });

        LiveData<List<FavouriteFilms>> favouriteFilms = viewModel.getFavouriteFilms();
        favouriteFilms.observe(this, favouriteFilms1 -> {
            if (favouriteFilms1 != null) {
                List<Film> films = new ArrayList<>(favouriteFilms1);
                if (films.size() == 0)
                    textView.setVisibility(View.VISIBLE);
                else
                    textView.setVisibility(View.INVISIBLE);
                adapter.setFilms(films);
            }
        });
    }
}