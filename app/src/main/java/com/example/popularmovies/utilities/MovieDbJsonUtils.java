package com.example.popularmovies.utilities;

import com.example.popularmovies.model.MovieParcelable;
import com.example.popularmovies.model.Video;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;


public class MovieDbJsonUtils {
    private static final String TAG = MovieDbJsonUtils.class.getSimpleName();

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

    public static MovieParcelable getMovieDetailFromJson(String moviesJsonStr)
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

        MovieParcelable movieParcelable = new MovieParcelable();
        movieParcelable.setTitle(moviesJson.getString(OWM_MESSAGE_TITLE));
        movieParcelable.setDate(moviesJson.getString(OWM_MESSAGE_DATE));
        movieParcelable.setPlot(moviesJson.getString(OWM_MESSAGE_PLOT));
        movieParcelable.setRating(moviesJson.getString(OWM_MESSAGE_RATING));
        String posterUrl = BASE_POSTER_URL + POSTER_SIZE + moviesJson.getString(OWM_POSTER_PATH);
        movieParcelable.setPosterPath(posterUrl);

        return movieParcelable;
    }

    public static Video[] getMovieVideosFromJson (String moviesJsonStr) throws JSONException {
        final String OWM_MESSAGE_CODE = "cod";
        final String OWM_RESULTS = "results";
        final String OWM_KEY = "key";
        final String OWM_SITE = "site";
        final String OWM_NAME = "name";

        ArrayList<Video> videos = new ArrayList<>();
        JSONObject videosJson = new JSONObject(moviesJsonStr);

        if (videosJson.has(OWM_MESSAGE_CODE)) {
            int errorCode = videosJson.getInt(OWM_MESSAGE_CODE);

            switch (errorCode) {
                case HttpURLConnection.HTTP_OK:
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    return null;
                default:
                    return null;
            }
        }

        JSONArray videoResults = videosJson.getJSONArray(OWM_RESULTS);

        for (int i = 0; i < videoResults.length(); i++) {
            JSONObject videoJson = videoResults.getJSONObject(i);
            String key = videoJson.getString(OWM_KEY);
            String site = videoJson.getString(OWM_SITE);
            String name = videoJson.getString(OWM_NAME);
            Video video = new Video(name, key, site);
            videos.add(video);
        }

        Video[] videoArray = new Video[videos.size()];
        for (int i =0; i<videos.size(); i++) {
            videoArray[i] = videos.get(i);
        }

        return videoArray;
    }
}
