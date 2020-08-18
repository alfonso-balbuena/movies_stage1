package com.example.movies;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.movies.models.Genre;
import com.example.movies.models.Movie;
import com.example.movies.utils.ImageUtils;
import com.example.movies.utils.NetworkUtils;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.URL;

public class MovieDetail extends BaseActivity {
    private TextView mTitleTv;
    private ImageView mPosterIV;
    private TextView mReleasedTV;
    private TextView mVoteTV;
    private TextView mGenresTV;
    private TextView mOverviewTV;
    private TextView mSecondTitleTV;
    private TextView mErrorTV;
    private LinearLayout mConentMovieLL;
    private final String ID = "Id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        mTitleTv = findViewById(R.id.tv_title_movie);
        mPosterIV = findViewById(R.id.iv_poster);
        mReleasedTV = findViewById(R.id.tv_released_date);
        mVoteTV = findViewById(R.id.tv_vote);
        mGenresTV = findViewById(R.id.tv_genres);
        mOverviewTV = findViewById(R.id.tv_overview);
        mSecondTitleTV = findViewById(R.id.tv_title);
        mErrorTV = findViewById(R.id.tv_movie_detail_error);
        mConentMovieLL = findViewById(R.id.content_movie);
        Intent intent = getIntent();
        if(intent.hasExtra(ID)) {
            Integer id = intent.getIntExtra(ID,0);
            new FetchMovieDetail(this).execute(id);
        }
        setTitle(R.string.movie);
    }

    public static class FetchMovieDetail extends AsyncTask<Integer,Void, Movie> {
        private final WeakReference<MovieDetail> mActivity;

        public FetchMovieDetail(MovieDetail activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        protected Movie doInBackground(Integer... ids) {
            if(ids.length == 0 || !mActivity.get().isOnline())
                return null;
            Integer id = ids[0];
            Gson gson = new Gson();
            URL movieRequested = NetworkUtils.buildUrlSearch(id.toString());
            try {
                String jsonMovie = NetworkUtils.getResponseFromHttpUrl(movieRequested);
                Movie detail = gson.fromJson(jsonMovie, Movie.class);
                return detail;
            } catch (IOException e) {
                e.printStackTrace();
                return  null;
            }
        }

        @Override
        protected void onPostExecute(Movie movie) {
            if(movie == null) {
                mActivity.get().mErrorTV.setVisibility(View.VISIBLE);
                mActivity.get().mConentMovieLL.setVisibility(View.GONE);
                return;
            }
            mActivity.get().mTitleTv.setText(movie.getOriginal_title());
            Picasso.get().load(ImageUtils.getImagePath(movie.getPoster_path())).into(mActivity.get().mPosterIV);
            mActivity.get().mReleasedTV.setText(movie.getRelease_date());
            mActivity.get().mVoteTV.setText(movie.getVote_average().toString());
            mActivity.get().mOverviewTV.setText(movie.getOverview());
            mActivity.get().mSecondTitleTV.setText(movie.getTitle());
            for (Genre g: movie.getGenres()) {
                mActivity.get().mGenresTV.append(g.getName() + ",");
            }
        }
    }
}