package com.example.asus.hillplayer.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import com.example.asus.hillplayer.beans.Music;
import com.example.asus.hillplayer.constant.MusicModle;
import com.example.asus.hillplayer.constant.MusicState;
import com.example.asus.hillplayer.constant.MyConstant;
import com.example.asus.hillplayer.util.MyLog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by asus-cp on 2016-12-30.
 */

public class MediaPlayerService extends Service implements MediaPlayer.OnPreparedListener,
        MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnSeekCompleteListener{

    private static String TAG;

    private MediaPlayer mMediaPlayer;

    private List<Music> mMusics;

    private String mSingleMusicPath;//单曲播放的时候，该单曲的路径

    private String mLastPath = "";//上一首歌的路径

    private String mCurrentPath;//当前音乐的路径

    private int mCurrentModle;

    private int mCurrentMusicIndex;//当前音乐在音乐列表里面的索引

    private int mCurrentState;

    private List<Integer> mRandomIndexList;//随机播放的索引列表

    private Random mRandom;

    private LocalBroadcastManager mLocalBroadcastManager;


    @Override
    public void onCreate() {
        super.onCreate();
        TAG = getClass().getSimpleName();
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setOnErrorListener(this);
        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setOnCompletionListener(this);
        mMediaPlayer.setOnSeekCompleteListener(this);
        mRandomIndexList = new ArrayList<>();
        mRandom = new Random();
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent != null){
            mCurrentState = intent.getIntExtra(MyConstant.MUSIC_STATE_KEY, MusicState.IDLE);
            mCurrentPath = intent.getStringExtra(MyConstant.MUSIC_PATH_KEY);
            mCurrentModle = intent.getIntExtra(MyConstant.MUSIC_MODEL_KEY, MusicModle.ORDER);
            mMusics = (List<Music>) intent.getSerializableExtra(MyConstant.MUSICS_KEY);
            if(mMusics != null){
                mCurrentMusicIndex = getIndexByPath(mCurrentPath, mMusics);
            }

            MyLog.d(TAG, "mCurrentState="+ mCurrentState +"...."+"mCurrentPath="+mCurrentPath+"....."+"mCurrentMusicIndex="+mCurrentMusicIndex);

            switch(mCurrentState){
                case MusicState.START://这个内部分为2种情况
                    startPlayMusic();
                    break;
                case MusicState.PAUSE:
                    mMediaPlayer.pause();
                    break;
            }
        }

        return START_STICKY;
    }


    /**
     * 开始播放音乐
     */
    private void startPlayMusic() {
        if(mLastPath != null && mLastPath.equals(mCurrentPath)
                && !"".equals(mCurrentPath)){
            mMediaPlayer.start();
        }else{
            mMediaPlayer.reset();
            try {
                mMediaPlayer.setDataSource(mCurrentPath);
                mMediaPlayer.prepareAsync();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 发送当前音乐的广播
     */
    private void sendCurrentMusicInfo() {
        Intent intent = new Intent(MyConstant.CURRENT_MUSIC_RECEIVER_ACTION);
        intent.putExtra(MyConstant.MUSIC_INDEX_KEY, mCurrentMusicIndex);
        mLocalBroadcastManager.sendBroadcast(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMediaPlayer.release();
        mMediaPlayer = null;
    }


    /**
     * 根据路径获取索引的值
     * @param path
     * @param musics
     * @return
     */
    public int getIndexByPath(String path, List<Music> musics){
        if(path == null || musics == null){
            return -1;
        }
        for(int i = 0; i < musics.size(); i++){
            if(path.equals(musics.get(i).getData())){
                return i;
            }
        }
        return -1;
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MediaBinder();
    }


    @Override
    public void onPrepared(MediaPlayer mp) {
        sendCurrentMusicInfo();
        mp.start();
        mLastPath = mCurrentPath;

    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        MyLog.d(TAG, "what="+what+"...."+"extra="+extra);
        mp.reset();
        return true;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        switch (mCurrentModle){
            case MusicModle.ORDER:
                if(mCurrentMusicIndex != -1){
                    mCurrentMusicIndex++;
                    if(mCurrentMusicIndex < mMusics.size()){
                        mCurrentPath = mMusics.get(mCurrentMusicIndex).getData();
                        startPlayMusic();
                    }else{
                        mMediaPlayer.pause();
                    }
                }
                break;
            case MusicModle.CYCLE:
                if(mCurrentMusicIndex != -1){
                    mCurrentMusicIndex++;
                    if(mCurrentMusicIndex == mMusics.size()){
                        mCurrentMusicIndex = 0;
                    }
                    mCurrentPath = mMusics.get(mCurrentMusicIndex).getData();
                    startPlayMusic();
                }
                break;
            case MusicModle.RANDOM:
                mCurrentMusicIndex = mRandom.nextInt(mMusics.size());
                mCurrentPath = mMusics.get(mCurrentMusicIndex).getData();
                startPlayMusic();
                sendCurrentMusicInfo();
                break;
            case MusicModle.SINGLE:
                startPlayMusic();
                break;
        }
    }


    @Override
    public void onSeekComplete(MediaPlayer mp) {
        /*switch(mCurrentState){
            case MusicState.START://这个内部分为2种情况
                mMediaPlayer.start();
                break;
            case MusicState.PAUSE:
                mMediaPlayer.pause();
                break;
        }*/
    }

    public class MediaBinder extends Binder{

        public void seekTo(int progress){
            mMediaPlayer.seekTo(progress);
        }

        public int getProgress(){
            return mMediaPlayer.getCurrentPosition();
        }

        public int getSumProgress(){
            return mMediaPlayer.getDuration();
        }
    }


}
