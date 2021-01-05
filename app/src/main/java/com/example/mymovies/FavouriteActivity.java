package com.example.mymovies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

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
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.itemFavourite:
                Intent intentToFavourite = new Intent(this, FavouriteActivity.class);
                startActivity(intentToFavourite);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);

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

        adapter.setOnPosterClickListener(new FilmAdapter.OnPosterClickListener() {
            @Override
            public void onPosterClick(int position) {
                Film film = adapter.getFilms().get(position);
                Intent intent = new Intent(FavouriteActivity.this, DetailActivity.class);
                intent.putExtra("id", film.getId());
                startActivity(intent);
            }
        });

        LiveData<List<FavouriteFilms>> favouriteFilms = viewModel.getFavouriteFilms();
        favouriteFilms.observe(this, new Observer<List<FavouriteFilms>>() {
            @Override
            public void onChanged(List<FavouriteFilms> favouriteFilms) {
                List<Film> films = new ArrayList<>();
                if (favouriteFilms != null) {
                    films.addAll(favouriteFilms);
                    adapter.setFilms(films);
                }
            }
        });
    }
}