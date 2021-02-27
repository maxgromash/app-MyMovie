package com.example.mymovies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mymovies.adapters.FilmAdapter;
import com.example.mymovies.data.Film;
import com.example.mymovies.data.MainViewModel;
import com.example.mymovies.utils.JSONUtils;
import com.example.mymovies.utils.NetworkUtils;

import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch switchSort;
    private RecyclerView recyclerViewFilms;
    private FilmAdapter adapter;
    private TextView textViewPopularity;
    private TextView textViewVotes;
    private MainViewModel viewModel;
    private ProgressBar progressBar;

    private int methodOfSort;
    private static int page = 1;
    private static boolean isLoading = false;

    //Создание меню
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.itemFavourite) {
            Intent intentToFavourite = new Intent(this, FavouriteActivity.class);
            startActivity(intentToFavourite);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        textViewPopularity = findViewById(R.id.textViewPopularity);
        textViewVotes = findViewById(R.id.textViewTopRated);
        switchSort = findViewById(R.id.switchSort);
        progressBar = findViewById(R.id.progressBarLoading);

        //Создаём адаптер для RecyclerView
        adapter = new FilmAdapter();
        recyclerViewFilms = findViewById(R.id.recyclerViewFilms);
        recyclerViewFilms.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerViewFilms.setAdapter(adapter);


        //Инициализация ViewModel
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

        //получение фильмов через ViewModel и доавление LiveData к адаптеру
        LiveData<List<Film>> filmsFromLiveData = viewModel.getFilms();
        filmsFromLiveData.observe(this, films -> adapter.setFilms(films));

        //Настройка слушателя смены сортировки
        switchSort.setChecked(true);
        switchSort.setOnCheckedChangeListener((compoundButton, b) -> {
            page = 1;
            setMethodOfSort(b);
        });
        switchSort.setChecked(false);

        //Настройка слушателя нажатия на фильм
        adapter.setOnPosterClickListener(position -> {
            Film film = Objects.requireNonNull(viewModel.getFilms().getValue()).get(position);
            Intent intent = new Intent(MainActivity.this, DetailActivity.class);
            intent.putExtra("id", film.getId());
            startActivity(intent);
        });

        //Настройка слушателя конца фильмов
        adapter.setOnReachEndListener(() -> {
            if (!isLoading) {
                downloadData(methodOfSort);
                recyclerViewFilms.post(() -> adapter.notifyDataSetChanged());
            }
        });


    }


    /**
     * Обработка смены сортировки на рейтинг
     *
     * @param view вид
     */
    public void onClickSetTopRated(View view) {
        setMethodOfSort(false);
        switchSort.setChecked(false);
    }

    /**
     * Обработка смены сортировки на популярность
     *
     * @param view Нащ вью
     */
    public void onClickSetTPopularity(View view) {
        setMethodOfSort(true);
        switchSort.setChecked(true);
    }

    /**
     * Получение данных через API и добавление их в базу
     *
     * @param methodOfSort Метод сортировки
     */
    private void downloadData(int methodOfSort) {
        isLoading = true;
        progressBar.setVisibility(View.VISIBLE);
        viewModel.downloadData(methodOfSort, page);
        page++;
        isLoading = false;
        progressBar.setVisibility(View.INVISIBLE);
    }

    /**
     * Метод смены режима сортировки
     *
     * @param topRated вид сортировки
     */
    private void setMethodOfSort(boolean topRated) {
        if (topRated) {
            methodOfSort = NetworkUtils.VOTE;
            textViewPopularity.setTextColor(getResources().getColor(R.color.colorAccent));
            textViewVotes.setTextColor(getResources().getColor(R.color.white));
        } else {
            textViewPopularity.setTextColor(getResources().getColor(R.color.white));
            textViewVotes.setTextColor(getResources().getColor(R.color.colorAccent));
            methodOfSort = NetworkUtils.POPULARITY;
        }
        downloadData(methodOfSort);
    }
}