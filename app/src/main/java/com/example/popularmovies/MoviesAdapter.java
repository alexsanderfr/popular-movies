package com.example.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.popularmovies.utilities.Utils;
import com.squareup.picasso.Picasso;


public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {
    private final Context mContext;
    private String[] mPostersData;
    private final MoviesAdapterOnClickHandler mClickHandler;

    public interface MoviesAdapterOnClickHandler {
        void onClick(String id);
    }

    public MoviesAdapter(Context context, MoviesAdapterOnClickHandler onClickHandler) {
        mContext = context;
        mClickHandler = onClickHandler;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        int layoutIdForListItem = R.layout.movie_poster_list_item;

        View view = inflater.inflate(layoutIdForListItem, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        String image = Utils.getImageSize(mContext.getResources()
                .getDisplayMetrics().density, mPostersData[position]);
        Picasso.with(mContext).load(image).into(holder.mPosterImageView);
    }

    @Override
    public int getItemCount() {
        if (null == mPostersData) return 0;
        return mPostersData.length;
    }

    public void setPostersData(String[] postersData){
        mPostersData = postersData;
        notifyDataSetChanged();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{
        public final ImageView mPosterImageView;

        public MovieViewHolder(View itemView) {
            super(itemView);
            mPosterImageView = (ImageView) itemView.findViewById(R.id.iv_movie_poster);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mClickHandler.onClick(String.valueOf(adapterPosition));
        }
    }
}
