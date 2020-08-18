package com.example.movies.utils;

public final class ImageUtils {
    public static final String BASE_URL = "https://image.tmdb.org/t/p/";
    public static final String SIZE = "w500";

    public static String getImagePath(String path) {
        return BASE_URL.concat(SIZE).concat(path);
    }
}
