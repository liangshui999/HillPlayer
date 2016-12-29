package com.example.asus.hillplayer.beans;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by asus-cp on 2016-12-22.
 */

public class Music implements Parcelable{
    private String name;//歌名

    private String artist;//作者

    private String duration;//时长

    private String year;

    private String mimeType;

    private int size;//大小

    private String data;

    public Music(){}

    protected Music(Parcel in) {
        name = in.readString();
        artist = in.readString();
        duration = in.readString();
        year = in.readString();
        mimeType = in.readString();
        size = in.readInt();
        data = in.readString();
    }

    public static final Creator<Music> CREATOR = new Creator<Music>() {
        @Override
        public Music createFromParcel(Parcel in) {
            return new Music(in);
        }

        @Override
        public Music[] newArray(int size) {
            return new Music[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(artist);
        dest.writeString(duration);
        dest.writeString(year);
        dest.writeString(mimeType);
        dest.writeInt(size);
        dest.writeString(data);
    }
}
