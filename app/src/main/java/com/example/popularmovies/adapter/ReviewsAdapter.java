package com.example.popularmovies.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.popularmovies.R;
import com.example.popularmovies.model.Review;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewViewHolder>{

    private Review[] mReviews;
    private Context mContext;

    public ReviewsAdapter(Context context) {
        mContext = context;
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        int layoutIdForListItem = R.layout.movie_review_list_item;

        View view = inflater.inflate(layoutIdForListItem, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
        String author = mReviews[position].getAuthor();
        String content = mReviews[position].getContent();
        holder.mAuthorTextView.setText(author);
        holder.mContentTextView.setText(content);
    }

    @Override
    public int getItemCount() {
        if (mReviews == null) return 0;
        return mReviews.length;
    }

    public void setReviews(Review[] reviews) {
        mReviews = reviews;
        notifyDataSetChanged();
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder {
        public final TextView mAuthorTextView;
        public final TextView mContentTextView;

        public ReviewViewHolder(View itemView) {
            super(itemView);
            mAuthorTextView = (TextView) itemView.findViewById(R.id.tv_review_author);
            mContentTextView = (TextView) itemView.findViewById(R.id.tv_review_content);
        }
    }
}
