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
    private final static String API_PARAM = "api_key";

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
        Uri uri = Uri.parse(MOVIE_DB_BASE_URL + movieId).buildUpon()
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
