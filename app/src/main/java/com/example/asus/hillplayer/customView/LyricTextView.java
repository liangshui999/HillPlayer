package com.example.asus.hillplayer.customView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.TextView;

import com.example.asus.hillplayer.R;
import com.example.asus.hillplayer.beans.Lyric;
import com.example.asus.hillplayer.constant.MusicState;
import com.example.asus.hillplayer.util.MyLog;
import com.example.asus.hillplayer.util.SizeHelper;

import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by asus-cp on 2017-01-09.
 */

public class LyricTextView extends TextView{

    private String TAG;

    /**
     * 歌词数据
     */
    private List<Lyric> mLyrics;

    /**
     * 当前歌词的颜色
     */
    private int mCurrentLyricColor;

    /**
     * 普通歌词的颜色
     */
    private int mNormalLyricColor;

    /**
     * 当前歌词的大小
     */
    private int mCurrentLyricSize;

    /**
     * 普通歌词的大小
     */
    private int mNormalLyricSize;

    /**
     * 歌词之间的距离
     */
    private int mLyricSpace;

    /**
     * 画笔
     */
    private Paint mPaint;

    /**
     * 当前所在的歌词位置
     */
    private int mCurrentPosition;

    /**
     * 当前进度的准确时间点
     */
    private int mCurrentProgress;

    /**
     * 定时器
     */
    private Timer mTimer;


    /**
     * 刷新视图的标志
     */
    private static final int REFRESH_FLAG = 1;

    private MyHandler handler = new MyHandler(this);

    static class MyHandler extends Handler{

        private SoftReference<LyricTextView> ref;

        MyHandler(LyricTextView lyricTextView){
            this.ref = new SoftReference<>(lyricTextView);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case REFRESH_FLAG:
                    LyricTextView lyricTextView = ref.get();
                    if(lyricTextView != null){
                        lyricTextView.invalidate();
                        lyricTextView.mCurrentPosition++;
                        if(lyricTextView.mCurrentPosition >= lyricTextView.mLyrics.size()){
                            lyricTextView.mCurrentPosition = 0;
                            lyricTextView.handler.removeMessages(REFRESH_FLAG);
                        }else{
                            lyricTextView.handler.sendEmptyMessageDelayed(REFRESH_FLAG,
                                    (long) lyricTextView.mLyrics.get(lyricTextView.mCurrentPosition).getPlayTime());
                        }
                    }
                    break;
            }
        }
    }

    public LyricTextView(Context context) {
        this(context, null);
    }

    public LyricTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LyricTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    /**
     * 初始化
     */
    private void init(Context context, AttributeSet attrs) {
        TAG= getClass().getSimpleName();

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.LyricTextView);
        mCurrentLyricColor = ta.getColor(R.styleable.LyricTextView_current_lyric_color,
                getResources().getColor(R.color.text_icon));

        mNormalLyricColor = ta.getColor(R.styleable.LyricTextView_normal_lyric_color,
                getResources().getColor(R.color.secondary_text));

        mCurrentLyricSize = (int) ta.getDimension(R.styleable.LyricTextView_current_lyric_size,
                SizeHelper.convertDp2Px(18));

        mNormalLyricSize = (int) ta.getDimension(R.styleable.LyricTextView_normal_lyric_size,
                SizeHelper.convertDp2Px(14));

        mLyricSpace = (int) ta.getDimension(R.styleable.LyricTextView_lyric_space_size,
                SizeHelper.convertDp2Px(30));
//        MyLog.d(TAG, "mCurrentLyricColor = "+mCurrentLyricColor);
//        MyLog.d(TAG, "mNormalLyricColor = "+mNormalLyricColor);
//        MyLog.d(TAG, "mCurrentLyricSize = "+mCurrentLyricSize);
//        MyLog.d(TAG, "mNormalLyricSize = "+mNormalLyricSize);
//        MyLog.d(TAG, "mLyricSpace = "+mLyricSpace);

        ta.recycle();

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mLyrics = new ArrayList<>();
        mTimer = new Timer();
    }

    /**
     * 添加歌词数据
     * @param lyrics
     */
    public void setmLyrics(List<Lyric> lyrics, int currentMusicState) {
        mLyrics.clear();
        mLyrics.addAll(lyrics);
        mCurrentPosition = 0;
        //通知重新绘制
        invalidate();
        handler.removeMessages(REFRESH_FLAG);
        if(currentMusicState == MusicState.START){
            handler.sendEmptyMessageDelayed(REFRESH_FLAG, (long) mLyrics.get(0).getPlayTime());
        }

    }

    /**
     * 暂停播放歌词
     */
   public void pause(int currentProgress){
       handler.removeMessages(REFRESH_FLAG);
       mCurrentProgress = currentProgress;
   }

    /**
     * 开始播放歌词
     */
    public void start(){
        setProgress(mCurrentProgress, MusicState.START);
    }

    /**
     * 设置播放的时间进度
     * @param timeProgress
     */
    public void setProgress(int timeProgress, int currentMusicState){
        long palyedTime = 0;
        for(int i = 0;i < mLyrics.size(); i++){
            Lyric lyric = mLyrics.get(i);
            if(timeProgress >= lyric.getStartTime() && timeProgress <= lyric.getEndTime()){
                mCurrentPosition = i;
                palyedTime = (long) (timeProgress - lyric.getStartTime());
                break;
            }
        }
        if(mLyrics.size() > 0){
            if(currentMusicState == MusicState.START){
                handler.removeMessages(REFRESH_FLAG);
                handler.sendEmptyMessageDelayed(REFRESH_FLAG,
                        (long) mLyrics.get(mCurrentPosition).getPlayTime() - palyedTime);
            }else{
                handler.removeMessages(REFRESH_FLAG);
                mCurrentProgress = timeProgress;
            }
            invalidate();//这里一定要记得重绘
        }


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float width = getWidth();
        if(mLyrics.size() > 0){
            //绘制当前的一句歌词
            String currentLyric = mLyrics.get(mCurrentPosition).getContent();
            mPaint.setColor(mCurrentLyricColor);
            mPaint.setTextSize(mCurrentLyricSize);
            canvas.drawText(currentLyric, (width - currentLyric.length() * mCurrentLyricSize)/2, getHeight()/2, mPaint);

            //绘制当前歌词之上的歌词
            for(int i = mCurrentPosition - 1; i >= 0; i--){
                String lyric = mLyrics.get(i).getContent();
                mPaint.setColor(mNormalLyricColor);
                mPaint.setTextSize(mNormalLyricSize);
                canvas.drawText(lyric, (width - lyric.length() * mNormalLyricSize)/2,
                        getHeight()/2 - (mCurrentPosition - i) * mLyricSpace, mPaint);
            }

            //绘制当前歌词之下的歌词
            for(int i = mCurrentPosition + 1; i < mLyrics.size(); i++){
                String lyric = mLyrics.get(i).getContent();
                mPaint.setColor(mNormalLyricColor);
                mPaint.setTextSize(mNormalLyricSize);
                canvas.drawText(lyric, (width - lyric.length() * mNormalLyricSize)/2,
                        getHeight()/2 + (i - mCurrentPosition) * mLyricSpace, mPaint);
            }
        }

    }
}
