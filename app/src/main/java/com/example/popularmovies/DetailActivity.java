package com.example.popularmovies;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.popularmovies.utilities.MovieDbJsonUtils;
import com.example.popularmovies.utilities.NetworkUtils;
import com.example.popularmovies.utilities.Utils;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

public class DetailActivity extends AppCompatActivity {

    private String mMovieId;
    private ProgressBar mLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        if (intent != null) {
            if (intent.hasExtra(Intent.EXTRA_TEXT)) {
                mMovieId = intent.getStringExtra(Intent.EXTRA_TEXT);
                loadMovieDetailData();

            }
        }
    }

    private void loadMovieDetailData() {
        new FetchMovieDetailTask().execute(mMovieId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu, menu);
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
        setTitle("");
        ImageView posterImageView = (ImageView) findViewById(R.id.iv_movie_poster_detail);
        posterImageView.setImageResource(android.R.color.transparent);
        TextView overviewTextView = (TextView) findViewById(R.id.tv_overview);
        overviewTextView.setText("");
        TextView yearTextView = (TextView) findViewById(R.id.tv_year);
        yearTextView.setText("");
        TextView ratingTextView = (TextView) findViewById(R.id.tv_rating);
        ratingTextView.setText("");
        loadMovieDetailData();

    }

    private class FetchMovieDetailTask extends AsyncTask<String, Object, HashMap<String, String>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
            mLoadingIndicator.getIndeterminateDrawable()
                    .setColorFilter(ResourcesCompat.getColor(getResources(), R.color.progressBarColor, null),
                            PorterDuff.Mode.MULTIPLY);
        }

        @Override
        protected HashMap<String, String> doInBackground(String... params) {
            if (Utils.isOnline(DetailActivity.this)) {
                try {
                    URL url = NetworkUtils.buildUrlForMovieDetail(params[0]);
                    String jsonResponse = NetworkUtils.getResponseFromHttpUrl(url);
                    return MovieDbJsonUtils.getMovieDetailFromJson(jsonResponse);

                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                    return null;
                }
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(HashMap<String, String> result) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (result != null) {
                setTitle(result.get(MovieDbJsonUtils.OWM_MESSAGE_TITLE));
                ImageView posterImageView = (ImageView) findViewById(R.id.iv_movie_poster_detail);
                Picasso.with(DetailActivity.this).load(Utils.getImageSize(getResources()
                        .getDisplayMetrics().density, result
                        .get(MovieDbJsonUtils.OWM_POSTER_PATH))).into(posterImageView);
                TextView overviewTextView = (TextView) findViewById(R.id.tv_overview);
                overviewTextView.setText(result.get(MovieDbJsonUtils.OWM_MESSAGE_PLOT));
                TextView yearTextView = (TextView) findViewById(R.id.tv_year);
                yearTextView.setText(result.get(MovieDbJsonUtils.OWM_MESSAGE_DATE));
                TextView ratingTextView = (TextView) findViewById(R.id.tv_rating);
                ratingTextView.setText(result.get(MovieDbJsonUtils.OWM_MESSAGE_RATING));
            } else {
                View view = findViewById(android.R.id.content);
                Snackbar.make(view, getString(R.string.connectivity_error), Snackbar.LENGTH_SHORT)
                        .show();
            }
        }

    }
}
