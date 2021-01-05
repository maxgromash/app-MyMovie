package com.example.mymovies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
    private  static int page = 1;
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
        switch (id) {
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


        //Настройка слушателя смены сортировки
        switchSort.setChecked(true);
        switchSort.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                page = 1;
                setMethodOfSort(b);
            }
        });
        switchSort.setChecked(false);

        //Настройка слушателя нажатия на фильм
        adapter.setOnPosterClickListener(new FilmAdapter.OnPosterClickListener() {
            @Override
            public void onPosterClick(int position) {
                Film film = viewModel.getFilms().getValue().get(position);
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra("id", film.getId());
                startActivity(intent);
            }
        });

        //Настройка слушателя конца фильмов
        adapter.setOnReachEndListener(new FilmAdapter.OnReachEndListener() {
            @Override
            public void onReachEnd() {
                if (!isLoading){
                    downloadData(methodOfSort);
                    recyclerViewFilms.post(new Runnable()
                    {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        });

        //получение фильмов через ViewModel и доавление LiveData к адаптеру
        LiveData<List<Film>> filmsFromLiveData = viewModel.getFilms();
        filmsFromLiveData.observe(this, new Observer<List<Film>>() {
            @Override
            public void onChanged(List<Film> films) {
               adapter.setFilms(films);
            }
        });
    }


    /**
     * Обработка смены сортировки на рейтинг
     *
     * @param view вид
     */
    public void onCkickSetTopRated(View view) {
        setMethodOfSort(false);
        switchSort.setChecked(false);
    }

    /**
     * Обработка смены сортировки на популярность
     *
     * @param view
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

        ArrayList<Film> films = JSONUtils.getMoviesFromJSON(NetworkUtils.getJSONFromNetwork(methodOfSort, page));
        if (films != null && !films.isEmpty()) {
            if (page == 1)
                viewModel.deleteAllFilms();
            for (Film film : films) {
                viewModel.insertFilm(film);
            }
            adapter.addFilms(films);
            page++;
        }
        isLoading = false;
        progressBar.setVisibility(View.INVISIBLE);
    }

    /**
     * Мето дсмены режима сортировки
     *
     * @param toprated вид сортировки
     */
    private void setMethodOfSort(boolean toprated) {
        if (toprated) {
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