package com.example.popularmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class MovieContract {

    public static final String AUTHORITY = "com.example.popularmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_FAVORITES = "favorites";

    public static final class MovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_FAVORITES).build();

        public static final String TABLE_NAME = "favorites";

        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_POSTER_ID = "poster_id";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_RATING = "rating";
    }
}
