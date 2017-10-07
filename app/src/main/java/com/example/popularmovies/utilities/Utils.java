package com.example.popularmovies.utilities;

import android.content.Context;
import android.util.Log;

import com.example.popularmovies.BuildConfig;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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

    public static String formatDate(String dateString, Context context) {

        Log.i("date", dateString);
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat format = new SimpleDateFormat(myFormat, Locale.getDefault());
        Date date = null;
        try {
            date = format.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(context);
        return dateFormat.format(date);
    }
}
