package com.example.asus.hillplayer.activity;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.asus.hillplayer.R;
import com.example.asus.hillplayer.adapter.ListSlidAdapter;
import com.example.asus.hillplayer.adapter.MusicFragmentAdapter;
import com.example.asus.hillplayer.beans.Music;
import com.example.asus.hillplayer.constant.MyConstant;
import com.example.asus.hillplayer.fragment.ArtistListFragment;
import com.example.asus.hillplayer.fragment.LocalMusicListFragment;
import com.example.asus.hillplayer.presenter.activityPrensenter.PMainActivity;
import com.example.asus.hillplayer.receiver.INetObserver;
import com.example.asus.hillplayer.receiver.NetStateReceiver;
import com.example.asus.hillplayer.util.VaryViewController;
import com.example.asus.hillplayer.view.activityViewI.IViewMainActivity;

import java.io.Serializable;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity<IViewMainActivity,PMainActivity> implements
IViewMainActivity{

    private Toolbar mToolBar;

    private DrawerLayout mDrawerLayout;

    private ListView mSlidListView;

    private LinearLayout mTargetView;

    private TabLayout mTabLayout;

    private ViewPager mViewPager;

    private NetStateReceiver mNetStateReceiver;

    private VaryViewController mVaryViewController;

    private FragmentManager mFragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setmNetObserver(new INetObserver() {
            @Override
            public void onNetConnected() {
                Toast.makeText(MainActivity.this,"网络连接",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNetDisConnected() {
                Toast.makeText(MainActivity.this,"网络断开连接",Toast.LENGTH_SHORT).show();
            }
        });
        setContentView(R.layout.activity_main);
        mTargetView = (LinearLayout) findViewById(R.id.ll_container);
        refreshVaryViewController(mTargetView);
        initView();
        initData();


        List<String> menus=new ArrayList<>();
        menus.add(getResources().getString(R.string.music_player));
        menus.add(getResources().getString(R.string.radio));
        menus.add(getResources().getString(R.string.video_player));
        ListSlidAdapter adapter=new ListSlidAdapter(this,menus);
        mSlidListView.setAdapter(adapter);

//        showLoading("正在加载...");
//        myHandler.sendEmptyMessageDelayed(ERROR,3000);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void initView() {
        mToolBar = (Toolbar) findViewById(R.id.toolbar);
        mDrawerLayout= (DrawerLayout) findViewById(R.id.drawlayout_slid);
        mSlidListView= (ListView) findViewById(R.id.list_slid_menu);
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        mViewPager = (ViewPager) findViewById(R.id.viewpager_main_activity);
    }

    @Override
    public void initData() {
        //必须写在最前面
        mToolBar.setTitle(R.string.local_music);
        mToolBar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(mToolBar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ActionBarDrawerToggle actionBarDrawerToggle=new ActionBarDrawerToggle(this,mDrawerLayout,
                mToolBar,R.string.open_drawer,R.string.close_drawer){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        actionBarDrawerToggle.syncState();
        mDrawerLayout.addDrawerListener(actionBarDrawerToggle);

        mFragmentManager = getSupportFragmentManager();
        mPresenter.fetchData();
//        LocalMusicListFragment fragment = new LocalMusicListFragment();
//        FragmentTransaction transaction = mFragmentManager.beginTransaction();
//        transaction.add(R.id.ll_container, fragment);
//        transaction.commit();


    }

    @Override
    public PMainActivity createPresenter() {
        return new PMainActivity();
    }


    @Override
    public void setDatasToFragments(List<Music> musics) {
        List<Fragment> fragments = new ArrayList<>(2);
        LocalMusicListFragment localMusicListFragment = new LocalMusicListFragment();
        ArtistListFragment artistListFragment = new ArtistListFragment();

        Bundle bundle = new Bundle();
        bundle.putSerializable(MyConstant.TO_MUSIC_ARTIST_LIST_FRAGMENT_KEY, (Serializable) musics);
        localMusicListFragment.setArguments(bundle);
        artistListFragment.setArguments(bundle);


        List<String> titles = new ArrayList<>(2);
        titles.add(getString(R.string.music_list));
        titles.add(getString(R.string.artist));

        fragments.add(localMusicListFragment);
        fragments.add(artistListFragment);
        MusicFragmentAdapter adapter = new MusicFragmentAdapter(mFragmentManager, fragments, titles);
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);



    }

    @Override
    public void onBackPressed() {
        if(mDrawerLayout.isDrawerOpen(Gravity.LEFT)){
            mDrawerLayout.closeDrawer(Gravity.LEFT);
        }else{
            finish();
        }
    }
}







/*public static final int LOAD=1;

    public static final int ERROR=2;

    public static final int TARGET=3;

    private MyHandler myHandler=new MyHandler(this);

    static class MyHandler extends Handler{

        private SoftReference<MainActivity> softReference;
        private ReferenceQueue<Activity> referenceQueue;

        public MyHandler(MainActivity mainActivity){
            referenceQueue=new ReferenceQueue<>();
            softReference=new SoftReference<>(mainActivity,referenceQueue);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            final MainActivity mainActivity=softReference.get();
            if(mainActivity != null){
                switch (msg.what){
                    case LOAD:
                        break;
                    case ERROR:
                        mainActivity.showError("加载出错了...", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(mainActivity,"错了",Toast.LENGTH_SHORT).show();
                                mainActivity.hideLoading();
                            }
                        });
                        break;
                    case TARGET:
                        mainActivity.hideLoading();
                        break;
                }
            }
        }
    }*/
