package com.example.asus.hillplayer.model.IModel;

import com.example.asus.hillplayer.beans.Music;
import com.example.asus.hillplayer.callback.Callback;

import java.util.List;

/**
 * Created by asus-cp on 2016-12-27.
 */

public interface IModleMainActivity {

    void fetchMusicData(Callback<List<Music>> callback);
}
