package com.example.movies;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.movies.adapters.SummaryMoviesAdapter;
import com.example.movies.models.Action;
import com.example.movies.models.ResponseMovies;
import com.example.movies.models.SummaryMovie;
import com.example.movies.utils.NetworkUtils;
import com.google.gson.Gson;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    private ProgressBar progressBar;
    private SummaryMoviesAdapter moviesAdapter;
    private TextView errorTV;
    private Action current;
    private final String ID = "Id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = findViewById(R.id.pb_loadingmMovies);
        RecyclerView rv_movies = findViewById(R.id.rv_movies);
        errorTV = findViewById(R.id.tv_movies_error);
        rv_movies.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(this,2,LinearLayoutManager.VERTICAL,false);
        rv_movies.setLayoutManager(layoutManager);
        moviesAdapter = new SummaryMoviesAdapter(new SummaryMoviesAdapter.SummaryMoviesClickHandler() {
            @Override
            public void onClick(int id) {
                Intent detailIntent = new Intent(MainActivity.this, MovieDetail.class);
                detailIntent.putExtra(ID,id);
                startActivity(detailIntent);
            }
        });
        rv_movies.setAdapter(moviesAdapter);
        new FetchMovies(this).execute(Action.POPULAR);
        current = Action.POPULAR;
        setTitle(R.string.popular);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings,menu);
        return true;
    }

    private void clickOption(Action new_action) {
        if(current != new_action) {
            new FetchMovies(this).execute(new_action);
            current = new_action;
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sort_popular:
                setTitle(R.string.popular);
                clickOption(Action.POPULAR);
                return true;
            case R.id.action_sort_rated:
                setTitle(R.string.rated);
                clickOption(Action.RATED);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static class FetchMovies extends AsyncTask<Action,Void, List<SummaryMovie>> {

        private final WeakReference<MainActivity> mActivity;

        public FetchMovies(MainActivity mainActivity) {
            mActivity = new WeakReference<>(mainActivity);
        }

        @Override
        protected void onPreExecute() {
            mActivity.get().progressBar.setVisibility(View.VISIBLE);
            mActivity.get().errorTV.setVisibility(View.GONE);
        }

        @Override
        protected List<SummaryMovie> doInBackground(Action... actions) {
            if(actions.length == 0 || !mActivity.get().isOnline())
                return  null;

            URL moviesRequested = NetworkUtils.buildURL(actions[0]);
            Gson gson = new Gson();
            try {
                String jsonMovies = NetworkUtils.getResponseFromHttpUrl(moviesRequested);
                ResponseMovies responseMovies = gson.fromJson(jsonMovies, ResponseMovies.class);
                return responseMovies.getResults();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<SummaryMovie> movies) {
            mActivity.get().progressBar.setVisibility(View.GONE);
            if(movies != null) {
                mActivity.get().moviesAdapter.setMovieList(movies);
            } else {
                mActivity.get().moviesAdapter.setMovieList(new ArrayList<SummaryMovie>());
                mActivity.get().errorTV.setVisibility(View.VISIBLE);
            }
        }
    }
}