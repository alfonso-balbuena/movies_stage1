package com.example.movies.utils;

import android.net.Uri;

import com.example.movies.models.Action;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public final class NetworkUtils {

    private final static String BASE_PATH = "https://api.themoviedb.org/3/movie/";
    private final static String POPULAR = "popular";
    private final static String RATED = "top_rated";
    private final static String API_KEY = "";


    public static URL buildURL(Action action) {
        String uri_path = BASE_PATH.concat(getAction(action));
        Uri uri = Uri.parse(uri_path).buildUpon().
                appendQueryParameter("api_key",API_KEY).build();
        return getUrl(uri);
    }

    private static URL getUrl(Uri uri) {
        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static URL buildUrlSearch(String id) {
        String uri_path = BASE_PATH.concat(id);
        Uri uri = Uri.parse(uri_path).buildUpon().
                appendQueryParameter("api_key",API_KEY).build();
        return getUrl(uri);
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");
            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    private static String getAction(Action action) {
        switch (action) {
            case RATED:
                return RATED;
            default:
                return POPULAR;
        }
    }
}
