package com.example.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.example.popularmovies.adapter.MoviesAdapter;
import com.example.popularmovies.data.MovieContract;
import com.example.popularmovies.model.MovieParcelable;
import com.example.popularmovies.utilities.MovieDbJsonUtils;
import com.example.popularmovies.utilities.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.MoviesAdapterOnClickHandler {

    private MoviesAdapter mMoviesAdapter;
    private String[] mMovieIdsArray = null;
    private ProgressBar mLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_movies);
        int numberOfColumns = 2;
        recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
        mMoviesAdapter = new MoviesAdapter(this, this);
        recyclerView.setAdapter(mMoviesAdapter);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

    }

    private void loadMoviePostersData() {
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        String key = getString(R.string.sorting_method);
        String sortingMethod = sharedPref.getString(key, "popular");
        new FetchMoviePostersTask().execute(sortingMethod);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        MenuItem item = menu.findItem(R.id.action_spinner);
        final Spinner spinner = (Spinner) MenuItemCompat.getActionView(item);

        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.sorting_array,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setPrompt(getString(R.string.sorting_prompt));

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        String key = getString(R.string.sorting_method);
        String sortingMethod = sharedPref.getString(key, "popular");
        switch (sortingMethod) {
            case "popular":
                spinner.setSelection(0);
                break;
            case "top_rated":
                spinner.setSelection(1);
                break;
            case "favorites":
                spinner.setSelection(2);
                break;
        }

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mMoviesAdapter.setPostersData(null);
                SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                switch (spinner.getSelectedItem().toString()) {
                    case "Popularity":
                        editor.putString(getString(R.string.sorting_method), "popular");
                        break;
                    case "Top rated":
                        editor.putString(getString(R.string.sorting_method), "top_rated");
                        break;
                    case "Favorites":
                        editor.putString(getString(R.string.sorting_method), "favorites");
                }
                editor.apply();
                loadMoviePostersData();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_refresh) {
            refresh();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void refresh() {
        mMoviesAdapter.setPostersData(null);
        loadMoviePostersData();
    }

    @Override
    public void onClick(String position) {
        MovieParcelable movieParcelable = new MovieParcelable();
        movieParcelable.setId(mMovieIdsArray[Integer.parseInt(position)]);
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT, movieParcelable);
        startActivity(intent);
    }

    private class FetchMoviePostersTask extends AsyncTask<String, Void, String[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
            mLoadingIndicator.getIndeterminateDrawable()
                    .setColorFilter(ResourcesCompat.getColor(getResources(), R.color.progressBarColor, null),
                            PorterDuff.Mode.MULTIPLY);
        }

        @Override
        protected String[] doInBackground(String... params) {
            ConnectivityManager cm =
                    (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                try {
                    if (params[0].equals("favorites")) {
                        ArrayList<String> movieIds = new ArrayList<>();
                        Cursor cursor = getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,null, null, null, null);
                        if (cursor != null && cursor.moveToFirst()) {
                            do {
                                movieIds.add(cursor.getString(cursor.getColumnIndex("movie_id")));
                            } while (cursor.moveToNext());
                            cursor.close();
                        }
                        mMovieIdsArray = movieIds.toArray(new String[0]);
                        return MovieDbJsonUtils.getPostersStringsFromIdsArray(mMovieIdsArray);
                    } else {
                        URL url = NetworkUtils.buildUrlForDiscover(params[0]);
                        String jsonResponse = NetworkUtils.getResponseFromHttpUrl(url);
                        mMovieIdsArray = MovieDbJsonUtils.getMovieIdsFromJson(jsonResponse);
                        return MovieDbJsonUtils
                                .getPostersStringsFromJson(jsonResponse);
                    }

                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                    return null;
                }
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(String[] moviePostersData) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (moviePostersData != null) {
                mMoviesAdapter.setPostersData(moviePostersData);
            } else {
                View view = findViewById(android.R.id.content);
                Snackbar.make(view, getString(R.string.connectivity_error), Snackbar.LENGTH_SHORT)
                        .show();
            }
        }

    }
}