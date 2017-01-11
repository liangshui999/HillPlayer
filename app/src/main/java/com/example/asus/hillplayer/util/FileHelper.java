package com.example.asus.hillplayer.util;

import android.content.Context;
import android.os.Environment;

import com.example.asus.hillplayer.beans.Lyric;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by asus-cp on 2017-01-10.
 */

public class FileHelper {

    private static final String TAG = "FileHelper";

    private static final String DIRECTORY_NAME = "hill";

    public FileHelper() {

    }

    public void readLyric(String musicName, List<Lyric> lyrics){
        String path = null;
        File directory = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator + DIRECTORY_NAME);
        if(! directory.exists() || !directory.isDirectory()){
            return;
        }
        File[] files = directory.listFiles();
        for(File file : files){
            if(file.getName().equals(changePostFix(musicName))){
                path = file.getAbsolutePath();
                break;
            }
        }
        if(path != null){
            realReadLyric(path, lyrics);
        }
    }

    /**
     * 读取歌词的内容
     * @param path
     * @param lyrics
     */
    private void realReadLyric(String path, List<Lyric> lyrics){
        if(path == null || lyrics == null){
            return;
        }
        File file = new File(path);
        if(! file.exists()){
            return;
        }
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            BufferedReader br = new BufferedReader(new InputStreamReader(fileInputStream, "GBK"));
            String line = null;
            while((line = br.readLine()) != null){
                String lineContent = getTime(line);
                if(lineContent != null){
                    int semicolonIndex = lineContent.indexOf(":");
                    float minute = Float.parseFloat(lineContent.substring(1, semicolonIndex));
                    float sedond = Float.parseFloat(lineContent.substring(semicolonIndex+1, lineContent.length()-1));
                    float startTime = minute * 60 * 1000 + sedond * 1000;
                    Lyric lyric = new Lyric();
                    lyric.setStartTime(startTime);

                    int inBracketsIndex = line.indexOf("]");
                    String content = line.substring(inBracketsIndex + 1);
                    lyric.setContent(content);
                    lyrics.add(lyric);
                }
            }
            setEndTimeAndPlayTime(lyrics);
        } catch (Exception e) {
            e.printStackTrace();
            MyLog.d(TAG,"异常信息："+e);
        }
    }

    /**
     * 获取到歌词里面的时间
     * @param s
     * @return
     */
    private String getTime(String s){
        String result = null;
        String regex = "\\[\\d+:\\d+\\.\\d+\\]";
        Pattern p = Pattern.compile(regex);
        Matcher matcher = p.matcher(s);
        if(matcher.find()){
            result = matcher.group();
        }

        return result;
    }


    /**
     * 设置每一句歌词的播放时间和结束时间
     * @param lyrics
     */
    private void setEndTimeAndPlayTime(List<Lyric> lyrics){
        for(int i = 0; i < lyrics.size() - 1; i++){
            Lyric current = lyrics.get(i);
            Lyric next = lyrics.get(i + 1);
            current.setEndTime(next.getStartTime());
            current.setPlayTime(current.getEndTime() - current.getStartTime());
        }
        //最后一句是重复的，需要移除掉
        lyrics.remove(lyrics.size() - 1);
    }


    /**
     * 更改文件名的后缀
     * @param s
     * @return
     */
    private String changePostFix(String s){
        String[] names = s.split("\\.");
        return names[0] + ".lrc";
    }
}
