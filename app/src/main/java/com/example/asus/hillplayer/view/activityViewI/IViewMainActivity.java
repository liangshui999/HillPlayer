package com.example.asus.hillplayer.view.activityViewI;

import com.example.asus.hillplayer.beans.Music;
import com.example.asus.hillplayer.view.BaseViewInterface;

import java.util.List;

/**
 * Created by asus-cp on 2016-12-21.
 */

public interface IViewMainActivity extends BaseViewInterface {

    void setDatasToFragments(List<Music> musics);
}
