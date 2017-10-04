package com.example.popularmovies.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.popularmovies.R;
import com.example.popularmovies.model.Video;

public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.VideoViewHolder>{

    private Video[] mVideos;
    private Context mContext;
    private VideosAdapterOnClickHandler mOnClickHandler;
    public interface VideosAdapterOnClickHandler {
        void onClick(String site, String key);
    }

    public VideosAdapter(Context context, VideosAdapterOnClickHandler onClickHandler) {
        mContext = context;
        mOnClickHandler = onClickHandler;
    }
    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        int layoutIdForListItem = R.layout.movie_video_list_item;

        View view = inflater.inflate(layoutIdForListItem, parent, false);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VideosAdapter.VideoViewHolder holder, int position) {
        String name = mVideos[position].getName();
        holder.mNameTextView.setText(name);
    }

    @Override
    public int getItemCount() {
        if (mVideos == null) return 0;
        return mVideos.length;
    }

    public void setVideos(Video[] videos){
        mVideos = videos;
        notifyDataSetChanged();
    }

    public class VideoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public final TextView mNameTextView;
        public VideoViewHolder(View itemView) {
            super(itemView);
            mNameTextView = (TextView) itemView.findViewById(R.id.tv_video_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mOnClickHandler.onClick(mVideos[adapterPosition].getSite(), mVideos[adapterPosition].getKey());
        }
    }
}
