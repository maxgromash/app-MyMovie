package com.example.mymovies.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymovies.R;
import com.example.mymovies.data.Film;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class FilmAdapter extends RecyclerView.Adapter<FilmAdapter.FilmViewHolder> {

    private List<Film> films;
    public OnPosterClickListener onPosterClickListener;
    private OnReachEndListener onReachEndListener;

    public FilmAdapter() {
        films = new ArrayList<>();
    }

    public List<Film> getFilms() {
        return films;
    }

    public void setFilms(List<Film> films) {
        this.films = films;
        notifyDataSetChanged();
    }

    public void addFilms(ArrayList<Film> films) {
        this.films.addAll(films);
    }

    public void setOnPosterClickListener(OnPosterClickListener onPosterClickListener) {
        this.onPosterClickListener = onPosterClickListener;
    }

    public void setOnReachEndListener(OnReachEndListener onReachEndListener) {
        this.onReachEndListener = onReachEndListener;
    }

    public interface OnPosterClickListener {
        void onPosterClick(int position);
    }

    public interface OnReachEndListener {
        void onReachEnd();
    }

    //Создаём
    @NonNull
    @Override
    public FilmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.film_item, parent, false);
        return new FilmViewHolder(view);
    }

    //Заполняем
    @Override
    public void onBindViewHolder(@NonNull FilmViewHolder holder, int position) {
        if (films.size()>=20 && position == films.size() -5 && onReachEndListener != null) {
            onReachEndListener.onReachEnd();
        }
        Film film = films.get(position);
        //Используем Picasso для авто-кэширования
        Picasso.get().load(film.getPosterPath()).into(holder.imageViewSmallPoster);
    }

    @Override
    public int getItemCount() {
        return films.size();
    }

    /**
     * Холдер для адаптера
     * Предоставляет доступ ко всем View-компонентам в кажддой строке списка
     */
    class FilmViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageViewSmallPoster;

        public FilmViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewSmallPoster = itemView.findViewById(R.id.imageViewSmallPoster);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onPosterClickListener != null)
                        onPosterClickListener.onPosterClick(getAdapterPosition());
                }
            });
        }
    }
}
