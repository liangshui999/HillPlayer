package com.example.asus.hillplayer.beans;

/**
 * Created by asus-cp on 2016-12-22.
 */

public class Music {
    private String name;//歌名

    private String artist;//作者

    private String duration;//时长

    private String year;

    private String mimeType;

    private int size;//大小

    private String data;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Music{" +
                "name='" + name + '\'' +
                ", artist='" + artist + '\'' +
                ", duration='" + duration + '\'' +
                ", year='" + year + '\'' +
                ", mimeType='" + mimeType + '\'' +
                ", size=" + size +
                ", data='" + data + '\'' +
                '}';
    }
}
