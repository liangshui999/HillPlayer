package com.example.asus.hillplayer.activity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.AppCompatSeekBar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.asus.hillplayer.R;
import com.example.asus.hillplayer.beans.Lyric;
import com.example.asus.hillplayer.beans.Music;
import com.example.asus.hillplayer.callback.OnClickListnerMy;
import com.example.asus.hillplayer.constant.MusicModle;
import com.example.asus.hillplayer.constant.MusicState;
import com.example.asus.hillplayer.constant.MyConstant;
import com.example.asus.hillplayer.customView.LyricTextView;
import com.example.asus.hillplayer.presenter.activityPrensenter.PMusicPlayerActivity;
import com.example.asus.hillplayer.service.MediaPlayerService;
import com.example.asus.hillplayer.util.MyLog;
import com.example.asus.hillplayer.util.TimeHelper;
import com.example.asus.hillplayer.view.activityViewI.IViewMusicPlayerActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by asus-cp on 2017-01-04.
 */

public class MusicPalyerAcitivity extends BaseActivity<IViewMusicPlayerActivity, PMusicPlayerActivity>
implements IViewMusicPlayerActivity, View.OnClickListener{

    private List<Music> mMusics;

    private int mCurrentMusicIndex;

    private Music mCurrentMusic;

    private int mCurrentMusicState;

    private int mCurrentProgress;

    private int mCurrentMusicMode;

    private MediaPlayerService.MediaBinder mMediaBinder;

    private ServiceConnection conn;

    private ProgressTask mProgressTask;

    private LocalBroadcastManager mLocalBroadcastManager;

    private CurrentMusicReciver mCurrentMusicReciver;

    private List<Lyric> mLyrics;

    /**
     * 是否是第一次到达这个界面
     */
    private boolean mIsFirst = true;

    private TextView mPlayTimeTextView;

    private TextView mSumTimeTextView;

    private AppCompatSeekBar mSeekBar;

    private ImageView mModeImageView;

    private ImageView mPreviousImageView;

    private ImageView mNextImageView;

    private ImageView mPlayImageView;

    private LyricTextView mLyricTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_music_player);
        initView();
        updateVaryViewController(mLyricTextView);
        initData();
    }

    @Override
    public void initView() {
        mPlayTimeTextView = (TextView) findViewById(R.id.text_play_time);
        mSumTimeTextView = (TextView) findViewById(R.id.text_sum_time);
        mSeekBar = (AppCompatSeekBar) findViewById(R.id.seek_bar);
        mModeImageView = (ImageView) findViewById(R.id.img_mode);
        mPreviousImageView = (ImageView) findViewById(R.id.img_previous);
        mNextImageView = (ImageView) findViewById(R.id.img_next);
        mPlayImageView = (ImageView) findViewById(R.id.img_play);
        mLyricTextView = (LyricTextView) findViewById(R.id.lyric_text_view);

        mModeImageView.setOnClickListener(this);
        mPreviousImageView.setOnClickListener(this);
        mNextImageView.setOnClickListener(this);
        mPlayImageView.setOnClickListener(this);

    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        mMusics = (List<Music>) intent.getSerializableExtra(MyConstant.MUSICS_KEY);
        mCurrentMusicIndex = intent.getIntExtra(MyConstant.MUSIC_INDEX_KEY, -1);
        mCurrentMusic = mMusics.get(mCurrentMusicIndex);
        mCurrentMusicState = intent.getIntExtra(MyConstant.MUSIC_STATE_KEY, MusicState.PAUSE);
        mCurrentMusicMode = MusicModle.ORDER;
        MyLog.d(TAG, mMusics.toString());

        if(mCurrentMusicState == MusicState.PAUSE){
            mPlayImageView.setImageResource(R.mipmap.pause_big);
        }else{
            mPlayImageView.setImageResource(R.mipmap.play_big);
        }
        setTitle(mCurrentMusic.getName());
        mPlayTimeTextView.setText(TimeHelper.convertMS2StanrdTime(mCurrentProgress));
        mSumTimeTextView.setText(TimeHelper.convertMS2StanrdTime(
                mCurrentMusic.getDuration()));
        mSeekBar.setMax(mCurrentMusic.getDuration());
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //只有用户手动拖拉的时候，才执行mMediaBinder.seekTo(mCurrentProgress);
                //下面有一个异步任务，每隔100ms就在设置seekbar的进度，但是他的fromuser是false
                if(fromUser){
                    if(mMediaBinder != null){
                        mMediaBinder.seekTo(progress);
                    }
                    mLyricTextView.setProgress(progress, mCurrentMusicState);
                }
                mCurrentProgress = progress;
                MyLog.d(TAG, "progress="+progress+"...."+"fromUser="+fromUser);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //绑定服务
        mProgressTask = new ProgressTask();
        conn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                mMediaBinder = (MediaPlayerService.MediaBinder) service;
                mProgressTask.executeOnExecutor(ProgressTask.THREAD_POOL_EXECUTOR);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
        Intent intent1 = new Intent(this, MediaPlayerService.class);
        bindService(intent1, conn, BIND_AUTO_CREATE);

        //注册广播
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
        mCurrentMusicReciver = new CurrentMusicReciver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MyConstant.CURRENT_MUSIC_RECEIVER_ACTION);
        mLocalBroadcastManager.registerReceiver(mCurrentMusicReciver, intentFilter);

        //获取歌词
        mPresenter.readLyric(mCurrentMusic.getName());

        setOnBackClickListner(new OnClickListnerMy() {
            @Override
            public void onClick(View v) {
                backHandle();
            }
        });
    }

    /**
     * 返回键的点击事件处理
     */
    private void backHandle() {
        Intent intent2 = new Intent();
        intent2.putExtra(MyConstant.MUSIC_STATE_KEY, mCurrentMusicState);
        setResult(RESULT_OK, intent2);
        finish();
    }

    @Override
    public void onBackPressed() {
        backHandle();
    }

    @Override
    public void refreshLyricTextView(List<Lyric> lyrics){
        if(lyrics.size() == 0){
            showError(R.string.not_found_lyric, null);
        }else{
            mLyricTextView.setmLyrics(lyrics, mCurrentMusicState);

        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocalBroadcastManager.unregisterReceiver(mCurrentMusicReciver);
        //退出时，一定要把异步任务取消掉,否则每次退出之后再进来都会新开一条线程
        mProgressTask.cancel(true);
        mProgressTask = null;
        unbindService(conn);
        mLyricTextView.pause(mCurrentProgress);
    }

    @Override
    public PMusicPlayerActivity createPresenter() {
        return new PMusicPlayerActivity();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_mode:
                mCurrentMusicMode++;
                mCurrentMusicMode = mCurrentMusicMode % 4;
                if(mCurrentMusicMode == MusicModle.ORDER){
                    mModeImageView.setImageResource(R.mipmap.order);
                    showToast(R.string.order_mode);
                }else if(mCurrentMusicMode == MusicModle.CYCLE){
                    mModeImageView.setImageResource(R.mipmap.recyle);
                    showToast(R.string.cycle_mode);
                }else if(mCurrentMusicMode == MusicModle.RANDOM){
                    mModeImageView.setImageResource(R.mipmap.random);
                    showToast(R.string.random_mode);
                }else if(mCurrentMusicMode == MusicModle.SINGLE){
                    mModeImageView.setImageResource(R.mipmap.single_recyle);
                    showToast(R.string.single_cycle_mode);
                }
                startService();
                break;
            case R.id.img_previous:
                mCurrentMusicState = MusicState.PREVIOUS;
                startService();
                break;
            case R.id.img_next:
                mCurrentMusicState = MusicState.NEXT;
                startService();
                break;
            case R.id.img_play:
                if(mCurrentMusicState == MusicState.PAUSE){
                    mCurrentMusicState = MusicState.START;
                    mPlayImageView.setImageResource(R.mipmap.play_big);
                    mLyricTextView.start();
                }else{
                    mCurrentMusicState = MusicState.PAUSE;
                    mPlayImageView.setImageResource(R.mipmap.pause_big);
                    mLyricTextView.pause(mCurrentProgress);
                }
                startService();
                break;
        }
    }

    /**
     * 开启服务
     */
    public void startService() {
        Intent intent = new Intent(this, MediaPlayerService.class);
        intent.putExtra(MyConstant.MUSIC_STATE_KEY, mCurrentMusicState);
        intent.putExtra(MyConstant.MUSIC_MODEL_KEY, mCurrentMusicMode);
        intent.putExtra(MyConstant.MUSICS_KEY, (Serializable) mMusics);
        intent.putExtra(MyConstant.MUSIC_PATH_KEY, mCurrentMusic.getData());
        startService(intent);
    }


    /**
     * 接收一首歌播放完成之后的广播
     */
    public class CurrentMusicReciver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            mCurrentMusicState = intent.getIntExtra(MyConstant.MUSIC_STATE_KEY, MusicState.START);
            int index = intent.getIntExtra(MyConstant.MUSIC_INDEX_KEY, -1);
            Music music = mMusics.get(index);
            mCurrentMusic = music;
            mCurrentMusicIndex = index;

            setTitle(music.getName());
            mSeekBar.setMax(music.getDuration());//换到下一首的时候必须重新设置max
            mSumTimeTextView.setText(TimeHelper.convertMS2StanrdTime(music.getDuration()));
            mPresenter.readLyric(mCurrentMusic.getName());

            if(mCurrentMusicState == MusicState.START){
                mPlayImageView.setImageResource(R.mipmap.play_big);
            }else if(mCurrentMusicState == MusicState.PAUSE){
                mPlayImageView.setImageResource(R.mipmap.pause_big);
            }
        }
    }


    /**
     * 异步任务，每隔100ms获取一次播放的进度，然后设置进度条的值
     */
    class ProgressTask extends AsyncTask<Void, Integer, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            while(! Thread.currentThread().isInterrupted()){
                try {
                    Thread.sleep(100);
                    publishProgress(mMediaBinder.getProgress());
                    MyLog.d(TAG, Thread.currentThread()+"");
                } catch (InterruptedException e) {  //抛出InterruptedException这个异常之后，也会清除掉中断标志
                    Thread.currentThread().interrupt(); //所以这儿要再次加上一个中断标志
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            MyLog.d(TAG, ""+values[0]);
            super.onProgressUpdate(values);
            mSeekBar.setProgress(values[0]);
            mPlayTimeTextView.setText(TimeHelper.convertMS2StanrdTime(values[0]));

            if(mIsFirst){
                mLyricTextView.setProgress(values[0], mCurrentMusicState);
                mIsFirst = false;
            }

        }
    }
}
