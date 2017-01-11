package com.example.asus.hillplayer.presenter.activityPrensenter;

import android.os.AsyncTask;
import android.os.Environment;

import com.example.asus.hillplayer.beans.Lyric;
import com.example.asus.hillplayer.presenter.BasePresenter;
import com.example.asus.hillplayer.util.FileHelper;
import com.example.asus.hillplayer.util.MyLog;
import com.example.asus.hillplayer.view.activityViewI.IViewMusicPlayerActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asus-cp on 2017-01-04.
 */

public class PMusicPlayerActivity extends BasePresenter<IViewMusicPlayerActivity> {

    private static final String TAG = "PMusicPlayerActivity";

    private FileHelper mFileHelper = new FileHelper();

    public void readLyric(String musicName){
        ReadTask readTask = new ReadTask(musicName);
        readTask.executeOnExecutor(ReadTask.THREAD_POOL_EXECUTOR);
    }

    private class ReadTask extends AsyncTask<String, Integer, List<Lyric>>{

        private String mMusicName;

        ReadTask(String mMusicName) {
            this.mMusicName = mMusicName;
    }

        @Override
        protected List<Lyric> doInBackground(String... params) {
            List<Lyric> lyrics = new ArrayList<>();
            String path = Environment.getExternalStorageDirectory().getAbsolutePath()
                    + "/" + "didu.lrc";

            mFileHelper.readLyric(mMusicName, lyrics);
            return lyrics;
        }

        @Override
        protected void onPostExecute(List<Lyric> lyrics) {
            super.onPostExecute(lyrics);
            //刷新歌词数据
            getView().refreshLyricTextView(lyrics);
            MyLog.d(TAG, lyrics.toString());
        }
    }

}
