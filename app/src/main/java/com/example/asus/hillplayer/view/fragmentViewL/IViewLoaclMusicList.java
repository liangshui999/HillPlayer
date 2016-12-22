package com.example.asus.hillplayer.view.fragmentViewL;

import com.example.asus.hillplayer.beans.Music;
import com.example.asus.hillplayer.view.BaseViewInterface;

import java.util.List;

/**
 * Created by asus-cp on 2016-12-22.
 */

public interface IViewLoaclMusicList extends BaseViewInterface {

    /**
     * 第一次获取数据之后刷新UI
     * @param musics
     */
    void initUI(List<Music> musics);

    /**
     * 每次数据更新的时候刷新UI
     * @param musics
     */
    void refreshUI(List<Music> musics);
}
