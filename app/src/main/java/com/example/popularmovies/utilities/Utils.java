package com.example.popularmovies.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.popularmovies.BuildConfig;

public class Utils {

    public static String getImageSize(float density, String posterPath) {
        if (density <= 3) {
            return posterPath;
        } else {
            return posterPath.replace("w500", "w780");
        }
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    static String getApiKey() {
        return BuildConfig.MOVIE_DB_API_KEY;
    }
}
