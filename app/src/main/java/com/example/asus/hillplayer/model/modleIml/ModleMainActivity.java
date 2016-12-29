package com.example.asus.hillplayer.model.modleIml;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;

import com.example.asus.hillplayer.beans.Music;
import com.example.asus.hillplayer.callback.Callback;
import com.example.asus.hillplayer.model.IModel.IModleMainActivity;
import com.example.asus.hillplayer.presenter.activityPrensenter.PMainActivity;
import com.example.asus.hillplayer.util.MyAppliacation;
import com.example.asus.hillplayer.util.MyLog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asus-cp on 2016-12-27.
 */

public class ModleMainActivity implements IModleMainActivity {

    private static String TAG = "ModleMainActivity";

    private ContentResolver mContentResolver;


    public ModleMainActivity() {

        TAG = getClass().getSimpleName();
        mContentResolver = MyAppliacation.getContext().getContentResolver();
    }

    @Override
    public void fetchMusicData(Callback<List<Music>> callback) {
        QueryTask queryTask = new QueryTask(callback);
        queryTask.executeOnExecutor(QueryTask.THREAD_POOL_EXECUTOR,
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);

    }

    private class QueryTask extends AsyncTask<Uri, Integer, List<Music>> {

        private Callback<List<Music>> mCallback;

        QueryTask(Callback<List<Music>> mCallback) {
            this.mCallback = mCallback;
        }

        @Override
        protected List<Music> doInBackground(Uri... params) {

            List<Music> result = new ArrayList<>();
            Cursor cursor = mContentResolver.query(params[0], new String[] { MediaStore.Audio.Media._ID,
                            MediaStore.Audio.Media.DISPLAY_NAME, MediaStore.Audio.Media.TITLE,
                            MediaStore.Audio.Media.DURATION, MediaStore.Audio.Media.ARTIST,
                            MediaStore.Audio.Media.ALBUM, MediaStore.Audio.Media.YEAR,
                            MediaStore.Audio.Media.MIME_TYPE, MediaStore.Audio.Media.SIZE,
                            MediaStore.Audio.Media.DATA}, MediaStore.Audio.Media.MIME_TYPE+"=? or "+
                            MediaStore.Audio.Media.MIME_TYPE+"=?", new String[]{"audio/mpeg","audio/x-ms-wma"},
                    null);
            if(cursor != null){
                while(cursor.moveToNext()){
                    Music music = new Music();
                    music.setName(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME)));
                    music.setDuration(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)));
                    music.setArtist(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)));
                    music.setYear(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.YEAR)));
                    music.setMimeType(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.MIME_TYPE)));
                    music.setSize(cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE)));
                    music.setData(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA)));
                    result.add(music);
                }
                cursor.close();
            }
            MyLog.d(TAG, "result="+result);
            return result;
        }

        @Override
        protected void onPostExecute(List<Music> musics) {
            super.onPostExecute(musics);
            mCallback.onfetchData(musics);
        }
    }
}
