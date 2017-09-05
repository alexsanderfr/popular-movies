package com.example.popularmovies.utilities;

import com.example.popularmovies.BuildConfig;

public class Utils {

    public static String getImageSize(float density, String posterPath) {
        if (density <= 3) {
            return posterPath;
        } else {
            return posterPath.replace("w500", "w780");
        }
    }

    static String getApiKey() {
        return BuildConfig.MOVIE_DB_API_KEY;
    }
}
