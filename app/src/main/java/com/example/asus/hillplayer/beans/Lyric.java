package com.example.asus.hillplayer.beans;

/**
 * 歌词
 * Created by asus-cp on 2017-01-10.
 */

public class Lyric {

    private float startTime;

    private float endTime;

    /**
     * 歌词的播放时间
     */
    private float playTime;

    /**
     * 歌词的内容
     */
    private String content;


    public float getStartTime() {
        return startTime;
    }

    public void setStartTime(float startTime) {
        this.startTime = startTime;
    }

    public float getEndTime() {
        return endTime;
    }

    public void setEndTime(float endTime) {
        this.endTime = endTime;
    }

    public float getPlayTime() {
        return playTime;
    }

    public void setPlayTime(float playTime) {
        this.playTime = playTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Lyric{" +
                "startTime=" + startTime +
                ", endTime=" + endTime +
                ", playTime=" + playTime +
                ", content='" + content + '\'' +
                '}';
    }
}
