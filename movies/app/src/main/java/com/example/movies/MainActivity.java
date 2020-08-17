package com.example.movies;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;

import com.example.movies.models.Action;
import com.example.movies.models.ResponseMovies;
import com.example.movies.models.SummaryMovie;
import com.example.movies.utils.NetworkUtils;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
    }
}