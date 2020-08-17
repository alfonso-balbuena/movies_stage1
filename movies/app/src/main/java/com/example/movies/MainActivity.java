package com.example.movies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ProgressBar;

import com.example.movies.adapters.SummaryMoviesAdapter;
import com.example.movies.models.Action;
import com.example.movies.models.ResponseMovies;
import com.example.movies.models.SummaryMovie;
import com.example.movies.utils.NetworkUtils;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private RecyclerView rv_movies;
    private SummaryMoviesAdapter moviesAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = findViewById(R.id.pb_loadingmMovies);
        rv_movies = findViewById(R.id.rv_movies);
        rv_movies.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(this,2,LinearLayoutManager.VERTICAL,false);
        rv_movies.setLayoutManager(layoutManager);
        moviesAdapter = new SummaryMoviesAdapter();
        rv_movies.setAdapter(moviesAdapter);
        Picasso.get().setLoggingEnabled(true);
        new FetchMovies().execute(Action.POPULAR);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings,menu);
        return true;
    }

    public class FetchMovies extends AsyncTask<Action,Void, List<SummaryMovie>> {

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<SummaryMovie> doInBackground(Action... actions) {
            if(actions.length == 0)
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
            progressBar.setVisibility(View.GONE);
            Log.d("SERVICE","Cargando peliculas");
            if(movies != null) {
                moviesAdapter.setMovieList(movies);
            }
        }
    }
}