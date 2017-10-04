package com.example.popularmovies.utilities;


import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String MOVIE_DB_BASE_URL = "https://api.themoviedb.org/3/movie/";
    private static final String MOVIE_DB_VIDEO_PATH = "/videos";
    private static final String MOVIE_DB_REVIEWS_PATH = "/reviews";
    private static final String API_PARAM = "api_key";
    private static final String YOUTUBE_BASE_URL = "https://www.youtube.com/watch";
    private static final String VIDEO_PARAM = "v";

    public static Uri buildUriForVideoIntent(String site, String key){
        switch (site) {
            case "YouTube":
                return Uri.parse(YOUTUBE_BASE_URL).buildUpon()
                        .appendQueryParameter(VIDEO_PARAM, key)
                        .build();
        }
        return null;
    }

    public static URL buildUrlForDiscover(String sortingMethod) {
        String  baseUrl = MOVIE_DB_BASE_URL.concat(sortingMethod);
        Uri uri = Uri.parse(baseUrl).buildUpon()
                .appendQueryParameter(API_PARAM, Utils.getApiKey())
                .build();

        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static URL buildUrlForMovieDetail(String movieId) {
        Uri uri = Uri.parse(MOVIE_DB_BASE_URL.concat(movieId)).buildUpon()
                .appendQueryParameter(API_PARAM, Utils.getApiKey())
                .build();

        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static URL buildUrlForMovieVideos(String movieId) {
        String baseUri = MOVIE_DB_BASE_URL.concat(movieId).concat(MOVIE_DB_VIDEO_PATH);
        Uri uri = Uri.parse(baseUri).buildUpon()
                .appendQueryParameter(API_PARAM, Utils.getApiKey())
                .build();
        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static URL buildUrlForMovieReviews(String movieId) {
        String baseUri = MOVIE_DB_BASE_URL.concat(movieId).concat(MOVIE_DB_REVIEWS_PATH);
        Uri uri = Uri.parse(baseUri).buildUpon()
                .appendQueryParameter(API_PARAM, Utils.getApiKey())
                .build();
        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

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

}
