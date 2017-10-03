package com.example.popularmovies.model;


public class Video {
    private String name;
    private String key;
    private String site;

    public Video(String name, String key, String site) {
        this.name = name;
        this.key = key;
        this.site = site;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

}
