package com.example.popularmovies;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.popularmovies.adapter.ReviewsAdapter;
import com.example.popularmovies.adapter.VideosAdapter;
import com.example.popularmovies.data.MovieContract;
import com.example.popularmovies.model.MovieParcelable;
import com.example.popularmovies.model.Review;
import com.example.popularmovies.model.Video;
import com.example.popularmovies.utilities.MovieDbJsonUtils;
import com.example.popularmovies.utilities.NetworkUtils;
import com.example.popularmovies.utilities.Utils;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

public class DetailActivity extends AppCompatActivity implements VideosAdapter.VideosAdapterOnClickHandler {


    private MovieParcelable movieParcelable;
    private ProgressBar mLoadingIndicator;
    private VideosAdapter mVideosAdapter;
    private ReviewsAdapter mReviewsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        if (intent != null) {
            if (intent.hasExtra(Intent.EXTRA_TEXT)) {
                movieParcelable = intent.getParcelableExtra(Intent.EXTRA_TEXT);
                loadMovieDetailData();

            }
        }

        RecyclerView videosRecyclerView = (RecyclerView) findViewById(R.id.rv_movie_videos);
        RecyclerView.LayoutManager videosLayoutManager = new LinearLayoutManager(this);
        videosRecyclerView.setLayoutManager(videosLayoutManager);
        videosRecyclerView.setNestedScrollingEnabled(false);
        mVideosAdapter = new VideosAdapter(this, this);
        videosRecyclerView.setAdapter(mVideosAdapter);

        RecyclerView reviewsRecyclerView = (RecyclerView) findViewById(R.id.rv_movie_reviews);
        RecyclerView.LayoutManager reviewsLayoutManager = new LinearLayoutManager(this);
        reviewsRecyclerView.setLayoutManager(reviewsLayoutManager);
        reviewsRecyclerView.setNestedScrollingEnabled(false);
        mReviewsAdapter = new ReviewsAdapter(this);
        reviewsRecyclerView.setAdapter(mReviewsAdapter);

        existsOnDb();
    }

    private void existsOnDb() {
        final String movieId = movieParcelable.getId();
        final Button favoriteButton = (Button) findViewById(R.id.bt_favorite);
        final Button unfavoriteButton = (Button) findViewById(R.id.bt_unfavorite);
        AsyncTask<String, Void, Cursor> asyncTask = new AsyncTask<String, Void, Cursor>() {
            @Override
            protected Cursor doInBackground(String... params) {
                try {
                    return getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                            null,
                            "movie_id=?",
                            new String[]{params[0]},
                            null);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(Cursor result) {
                if ((result != null) && result.getCount() > 0) {
                    favoriteButton.setVisibility(View.GONE);
                    unfavoriteButton.setVisibility(View.VISIBLE);
                } else {
                    unfavoriteButton.setVisibility(View.GONE);
                    favoriteButton.setVisibility(View.VISIBLE);
                }
            }
        };
        asyncTask.execute(movieId);
    }

    private void loadMovieDetailData() {
        String movieId = movieParcelable.getId();
        new FetchMovieDetailTask().execute(movieId);
        new FetchMovieVideosTask().execute(movieId);
        new FetchMovieReviewsTask().execute(movieId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                refresh();
                return true;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
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

    @Override
    public void onClick(String site, String key) {
        Uri link = NetworkUtils.buildUriForVideoIntent(site, key);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(link);
        startActivity(intent);
    }

    public void onClickFavorite(View view) {
        String movieId = movieParcelable.getId();

        Button favoriteButton = (Button) view;
        Button unfavoriteButton = (Button) findViewById(R.id.bt_unfavorite);
        Uri uri = MovieContract.MovieEntry.CONTENT_URI;
        ContentValues contentValues = new ContentValues();
        contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, movieId);
        getContentResolver().insert(uri, contentValues);
        unfavoriteButton.setVisibility(View.VISIBLE);
        favoriteButton.setVisibility(View.GONE);
    }

    public void onClickUnfavorite(View view) {
        String movieId = movieParcelable.getId();
        Button favoriteButton = (Button) findViewById(R.id.bt_favorite);
        Button unfavoriteButton = (Button) view;
        Uri uri = MovieContract.MovieEntry.CONTENT_URI;
        getContentResolver().delete(uri, "movie_id=?", new String[]{movieId});
        unfavoriteButton.setVisibility(View.GONE);
        favoriteButton.setVisibility(View.VISIBLE);
    }


    private class FetchMovieDetailTask extends AsyncTask<String, Object, MovieParcelable> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
            mLoadingIndicator.getIndeterminateDrawable()
                    .setColorFilter(ResourcesCompat.getColor(getResources(), R.color.progressBarColor, null),
                            PorterDuff.Mode.MULTIPLY);
        }

        @Override
        protected MovieParcelable doInBackground(String... params) {
            ConnectivityManager cm =
                    (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnectedOrConnecting()) {
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
        protected void onPostExecute(MovieParcelable result) {
            mLoadingIndicator.setVisibility(View.GONE);
            if (result != null) {
                setTitle(result.getTitle());
                ImageView posterImageView = (ImageView) findViewById(R.id.iv_movie_poster_detail);
                Picasso.with(DetailActivity.this).load(Utils.getImageSize(getResources()
                        .getDisplayMetrics().density, result.getPosterPath()))
                        .placeholder(R.drawable.ic_placeholder_image).error(R.drawable.ic_error)
                        .into(posterImageView);
                TextView overviewTextView = (TextView) findViewById(R.id.tv_overview);
                overviewTextView.setText(result.getPlot());
                TextView yearTextView = (TextView) findViewById(R.id.tv_year);
                yearTextView.setText(Utils.formatDate(result.getDate(), getApplicationContext()));
                TextView runtimeTextView = (TextView) findViewById(R.id.tv_runtime);
                String runtime = result.getRuntime() + " min";
                runtimeTextView.setText(runtime);
                TextView ratingTextView = (TextView) findViewById(R.id.tv_rating);
                String rating = result.getRating() + "/10";
                ratingTextView.setText(rating);
            } else {
                View view = findViewById(android.R.id.content);
                Snackbar.make(view, getString(R.string.connectivity_error), Snackbar.LENGTH_SHORT)
                        .show();
            }
        }

    }

    private class FetchMovieVideosTask extends AsyncTask<String, Object, Video[]> {
        @Override
        protected Video[] doInBackground(String... params) {
            ConnectivityManager cm =
                    (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                try {
                    URL url = NetworkUtils.buildUrlForMovieVideos(params[0]);
                    String jsonResponse = NetworkUtils.getResponseFromHttpUrl(url);
                    return MovieDbJsonUtils.getMovieVideosFromJson(jsonResponse);

                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                    return null;
                }
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(Video[] result) {
            if (result != null) {
                if (result.length == 0) {
                    TextView videoTextView = (TextView) findViewById(R.id.tv_videos);
                    videoTextView.setVisibility(View.GONE);
                }
                mVideosAdapter.setVideos(result);
            } else {
                View view = findViewById(android.R.id.content);
                Snackbar.make(view, getString(R.string.connectivity_error), Snackbar.LENGTH_SHORT)
                        .show();
            }
        }
    }

    private class FetchMovieReviewsTask extends AsyncTask<String, Object, Review[]> {
        @Override
        protected Review[] doInBackground(String... params) {
            ConnectivityManager cm =
                    (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                try {
                    URL url = NetworkUtils.buildUrlForMovieReviews(params[0]);
                    String jsonResponse = NetworkUtils.getResponseFromHttpUrl(url);
                    return MovieDbJsonUtils.getMovieReviewsFromJson(jsonResponse);

                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                    return null;
                }
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(Review[] result) {
            if (result != null) {
                if (result.length == 0) {
                    TextView reviewTextView = (TextView) findViewById(R.id.tv_reviews);
                    reviewTextView.setVisibility(View.GONE);
                }
                mReviewsAdapter.setReviews(result);
            } else {
                View view = findViewById(android.R.id.content);
                Snackbar.make(view, getString(R.string.connectivity_error), Snackbar.LENGTH_SHORT)
                        .show();
            }
        }
    }
}
