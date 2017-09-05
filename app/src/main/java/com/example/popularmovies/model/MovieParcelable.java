package com.example.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

public class MovieParcelable implements Parcelable {
    private String id;
    private String title;
    private String posterPath;
    private String plot;
    private String rating;
    private String date;

    private MovieParcelable(Parcel in) {
        id = in.readString();
        title = in.readString();
        posterPath = in.readString();
        plot = in.readString();
        rating = in.readString();
        date = in.readString();
    }

    public MovieParcelable() {
    }


    public static final Creator<MovieParcelable> CREATOR = new Creator<MovieParcelable>() {
        @Override
        public MovieParcelable createFromParcel(Parcel in) {
            return new MovieParcelable(in);
        }

        @Override
        public MovieParcelable[] newArray(int size) {
            return new MovieParcelable[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(posterPath);
        dest.writeString(plot);
        dest.writeString(rating);
        dest.writeString(date);
    }

    public String getId(){
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle(){
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
