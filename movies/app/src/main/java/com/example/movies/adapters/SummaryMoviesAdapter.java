package com.example.movies.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movies.R;
import com.example.movies.models.SummaryMovie;
import com.example.movies.utils.ImageUtils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SummaryMoviesAdapter extends RecyclerView.Adapter<SummaryMoviesAdapter.SummaryMoviesAdapterViewHolder> {

    List<SummaryMovie> movieList;

    public void setMovieList(List<SummaryMovie> movieList) {
        this.movieList = movieList;
        notifyDataSetChanged();
    }

    public void appendMovieList(List<SummaryMovie> movies) {
        if(movieList == null) {
            movieList = movies;
        } else {
            movieList.addAll(movies);
        }
    }

    @NonNull
    @Override
    public SummaryMoviesAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutItem = R.layout.movie_rv;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutItem,parent,false);
        return new SummaryMoviesAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SummaryMoviesAdapterViewHolder holder, int position) {
        String poster_path = movieList.get(position).getPoster_path();
        Picasso.get().load(ImageUtils.getImagePath(poster_path)).into(holder.posterIV, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(Exception e) {
                Log.e("ERROR",e.getMessage());
            }
        });
    }

    @Override
    public int getItemCount() {
        return (movieList == null) ? 0 : movieList.size();
    }

    public  class SummaryMoviesAdapterViewHolder extends RecyclerView.ViewHolder {
        public final ImageView posterIV;


        public SummaryMoviesAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            posterIV = itemView.findViewById(R.id.iv_movie_poster);
        }
    }
}
