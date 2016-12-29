package com.example.asus.hillplayer.presenter.activityPrensenter;

import com.example.asus.hillplayer.R;
import com.example.asus.hillplayer.activity.MainActivity;
import com.example.asus.hillplayer.beans.Music;
import com.example.asus.hillplayer.callback.Callback;
import com.example.asus.hillplayer.model.IModel.IModleMainActivity;
import com.example.asus.hillplayer.model.modleIml.ModleMainActivity;
import com.example.asus.hillplayer.presenter.BasePresenter;
import com.example.asus.hillplayer.view.activityViewI.IViewMainActivity;

import java.util.List;

/**
 * Created by asus-cp on 2016-12-21.
 */

public class PMainActivity extends BasePresenter<IViewMainActivity>{

    private IModleMainActivity mModle = new ModleMainActivity();

    public void fetchData(){
        final IViewMainActivity mainActivity = getView();
        mainActivity.showLoading(R.string.loading);
        mModle.fetchMusicData(new Callback<List<Music>>() {
            @Override
            public void onfetchData(List<Music> result) {
                mainActivity.hideLoading();
                mainActivity.setDatasToFragments(result);
            }
        });
    }
}
