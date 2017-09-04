package com.example.popularmovies.utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;


public class MovieDbJsonUtils {
    private static final String TAG = MovieDbJsonUtils.class.getSimpleName();

    public static final String OWM_MESSAGE_TITLE = "original_title";
    public static final String OWM_MESSAGE_PLOT = "overview";
    public static final String OWM_MESSAGE_DATE = "release_date";
    public static final String OWM_MESSAGE_RATING = "vote_average";
    public static final String OWM_POSTER_PATH = "poster_path";
    public static final String OWM_MESSAGE_CODE = "cod";
    public static final String OWM_RESULTS = "results";
    public static final String OWM_ID = "id";

    public static String[] getPostersStringsFromJson(String moviesJsonStr)
            throws JSONException {

        final String OWM_MESSAGE_CODE = "cod";
        final String OWM_RESULTS = "results";
        final String OWM_POSTER_PATH = "poster_path";
        final String BASE_POSTER_URL = "https://image.tmdb.org/t/p/";
        final String POSTER_SIZE = "w500/";

        ArrayList<String> postersData = new ArrayList<>();
        JSONObject moviesJson = new JSONObject(moviesJsonStr);

        if (moviesJson.has(OWM_MESSAGE_CODE)) {
            int errorCode = moviesJson.getInt(OWM_MESSAGE_CODE);

            switch (errorCode) {
                case HttpURLConnection.HTTP_OK:
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    return null;
                default:
                    return null;
            }
        }

        JSONArray movieResults = moviesJson.getJSONArray(OWM_RESULTS);

        for (int i = 0; i < movieResults.length(); i++) {
            JSONObject movie = movieResults.getJSONObject(i);
            String posterPath = movie.getString(OWM_POSTER_PATH);
            String posterUrl = BASE_POSTER_URL + POSTER_SIZE + posterPath;
            postersData.add(posterUrl);
        }
        return postersData.toArray(new String[0]);
    }

    public static String[] getMovieIdsFromJson(String moviesJsonStr) throws JSONException {
        final String OWM_MESSAGE_CODE = "cod";
        final String OWM_RESULTS = "results";
        final String OWM_ID = "id";

        ArrayList<String> movieIds = new ArrayList<>();
        JSONObject moviesJson = new JSONObject(moviesJsonStr);

        if (moviesJson.has(OWM_MESSAGE_CODE)) {
            int errorCode = moviesJson.getInt(OWM_MESSAGE_CODE);

            switch (errorCode) {
                case HttpURLConnection.HTTP_OK:
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    return null;
                default:
                    return null;
            }
        }

        JSONArray movieResults = moviesJson.getJSONArray(OWM_RESULTS);

        for (int i = 0; i < movieResults.length(); i++) {
            JSONObject movie = movieResults.getJSONObject(i);
            String id = movie.getString(OWM_ID);
            movieIds.add(id);
        }
        return movieIds.toArray(new String[0]);
    }

    public static HashMap<String, String> getMovieDetailFromJson(String moviesJsonStr)
            throws JSONException {
        final String OWM_MESSAGE_CODE = "cod";
        final String OWM_MESSAGE_TITLE = "original_title";
        final String OWM_MESSAGE_PLOT = "overview";
        final String OWM_MESSAGE_DATE = "release_date";
        final String OWM_MESSAGE_RATING = "vote_average";
        final String OWM_POSTER_PATH = "poster_path";
        final String BASE_POSTER_URL = "https://image.tmdb.org/t/p/";
        final String POSTER_SIZE = "w500/";

        JSONObject moviesJson = new JSONObject(moviesJsonStr);

        if (moviesJson.has(OWM_MESSAGE_CODE)) {
            int errorCode = moviesJson.getInt(OWM_MESSAGE_CODE);

            switch (errorCode) {
                case HttpURLConnection.HTTP_OK:
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    return null;
                default:
                    return null;
            }
        }

        HashMap<String, String> hashMapResult = new HashMap<>();
        hashMapResult.put(OWM_MESSAGE_TITLE, moviesJson.getString(OWM_MESSAGE_TITLE));
        hashMapResult.put(OWM_MESSAGE_PLOT, moviesJson.getString(OWM_MESSAGE_PLOT));
        hashMapResult.put(OWM_MESSAGE_DATE, moviesJson.getString(OWM_MESSAGE_DATE));
        hashMapResult.put(OWM_MESSAGE_RATING, moviesJson.getString(OWM_MESSAGE_RATING));
        String posterUrl = BASE_POSTER_URL + POSTER_SIZE + moviesJson.getString(OWM_POSTER_PATH);
        hashMapResult.put(OWM_POSTER_PATH, posterUrl);
        return hashMapResult;
    }
}
