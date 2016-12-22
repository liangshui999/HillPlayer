package com.example.asus.hillplayer.activity;

import com.example.asus.hillplayer.presenter.activityPrensenter.PTestActivity;
import com.example.asus.hillplayer.view.activityViewI.IViewTestActivity;

/**
 * Created by asus-cp on 2016-12-22.
 */

public class TestActivity extends BaseActivity<IViewTestActivity, PTestActivity>
implements IViewTestActivity{
    @Override
    public void initView() {

    }

    @Override
    public void initData() {
    }

    @Override
    public PTestActivity createPresenter() {

        return new PTestActivity();
    }

    @Override
    protected void onStart() {
        super.onStart();
        setTitle("呵呵");
    }
}
