package com.example.asus.hillplayer.view.activityViewI;

import com.example.asus.hillplayer.beans.Lyric;
import com.example.asus.hillplayer.customView.LyricTextView;
import com.example.asus.hillplayer.view.BaseViewInterface;

import java.util.List;

/**
 * Created by asus-cp on 2017-01-04.
 */

public interface IViewMusicPlayerActivity extends BaseViewInterface {

    /**
     * 刷新歌词数据
     * @param lyrics
     */
    void refreshLyricTextView(List<Lyric> lyrics);
}
