package com.example.asus.hillplayer.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.hillplayer.R;
import com.example.asus.hillplayer.adapter.ListSlidAdapter;
import com.example.asus.hillplayer.adapter.MusicFragmentAdapter;
import com.example.asus.hillplayer.beans.Music;
import com.example.asus.hillplayer.constant.MusicState;
import com.example.asus.hillplayer.constant.MyConstant;
import com.example.asus.hillplayer.fragment.ArtistListFragment;
import com.example.asus.hillplayer.fragment.LocalMusicListFragment;
import com.example.asus.hillplayer.presenter.activityPrensenter.PMainActivity;
import com.example.asus.hillplayer.receiver.INetObserver;
import com.example.asus.hillplayer.receiver.NetStateReceiver;
import com.example.asus.hillplayer.service.MediaPlayerService;
import com.example.asus.hillplayer.util.VaryViewController;
import com.example.asus.hillplayer.view.activityViewI.IViewMainActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity<IViewMainActivity,PMainActivity> implements
IViewMainActivity, View.OnClickListener{

    private Toolbar mToolBar;

    private DrawerLayout mDrawerLayout;

    private ListView mSlidListView;

    private LinearLayout mTargetView;

    private TabLayout mTabLayout;

    private ViewPager mViewPager;

    private TextView mMusicNameTextView;

    private TextView mArtistNameTextView;

    private ImageView mPlayImageView;

    private ImageView mNextImageView;

    private RelativeLayout mControlPannelLayout;

    private NetStateReceiver mNetStateReceiver;

    private VaryViewController mVaryViewController;

    private FragmentManager mFragmentManager;

    private List<Music> mMusics;

    private LocalBroadcastManager mLocalBroadcastManager;

    private CurrentMusicReciver mCurrentMusicReciver;

    private int mCurrentMusicState = MusicState.PAUSE;

    private int mCurrentMusicIndex;

    private static final int TO_MUSIC_PALYER_ACTIVITY = 1;


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
        updateVaryViewController(mTargetView);
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
        mLocalBroadcastManager.unregisterReceiver(mCurrentMusicReciver);
    }

    @Override
    public void initView() {
        mToolBar = (Toolbar) findViewById(R.id.toolbar);
        mDrawerLayout= (DrawerLayout) findViewById(R.id.drawlayout_slid);
        mSlidListView= (ListView) findViewById(R.id.list_slid_menu);
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        mViewPager = (ViewPager) findViewById(R.id.viewpager_main_activity);
        mMusicNameTextView = (TextView) findViewById(R.id.text_music_name);
        mArtistNameTextView = (TextView) findViewById(R.id.text_artist_name);
        mPlayImageView = (ImageView) findViewById(R.id.img_play);
        mNextImageView = (ImageView) findViewById(R.id.img_next);
        mControlPannelLayout = (RelativeLayout) findViewById(R.id.re_layout_control_pannel);

        mPlayImageView.setOnClickListener(this);
        mNextImageView.setOnClickListener(this);
        mControlPannelLayout.setOnClickListener(this);
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

        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
        mCurrentMusicReciver = new CurrentMusicReciver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MyConstant.CURRENT_MUSIC_RECEIVER_ACTION);
        mLocalBroadcastManager.registerReceiver(mCurrentMusicReciver, intentFilter);


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
        bundle.putSerializable(MyConstant.MUSICS_KEY, (Serializable) musics);
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
        mMusics = musics;

    }

    @Override
    public void onBackPressed() {
        if(mDrawerLayout.isDrawerOpen(Gravity.LEFT)){
            mDrawerLayout.closeDrawer(Gravity.LEFT);
        }else{
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_play:
                if(mCurrentMusicState == MusicState.PAUSE){
                    mCurrentMusicState = MusicState.START;
                    startMusicService(MusicState.START);
                    mPlayImageView.setImageResource(R.mipmap.play_blue);
                }else{
                    mCurrentMusicState = MusicState.PAUSE;
                    startMusicService(MusicState.PAUSE);
                    mPlayImageView.setImageResource(R.mipmap.pause_blue);
                }
                break;
            case R.id.img_next:
                mCurrentMusicIndex++;
                if(mCurrentMusicIndex == mMusics.size()){
                    mCurrentMusicIndex = 0;
                }
                startMusicService(MusicState.START);
                break;
            case R.id.re_layout_control_pannel:
                Intent intent = new Intent(this, MusicPalyerAcitivity.class);
                intent.putExtra(MyConstant.MUSIC_INDEX_KEY, mCurrentMusicIndex);
                intent.putExtra(MyConstant.MUSICS_KEY, (Serializable) mMusics);
                intent.putExtra(MyConstant.MUSIC_STATE_KEY, mCurrentMusicState);
                startActivityForResult(intent, TO_MUSIC_PALYER_ACTIVITY);
                break;
        }
    }

    /**
     * 开启播放音乐服务
     * @param pause
     */
    public void startMusicService(int pause) {
        Intent intent = new Intent(this, MediaPlayerService.class);
        intent.putExtra(MyConstant.MUSIC_STATE_KEY, pause);
        intent.putExtra(MyConstant.MUSIC_PATH_KEY, mMusics.get(mCurrentMusicIndex).getData());
        intent.putExtra(MyConstant.MUSICS_KEY, (Serializable) mMusics);
        startService(intent);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case TO_MUSIC_PALYER_ACTIVITY:
                if(resultCode == RESULT_OK){
                    mCurrentMusicState = data.getIntExtra(MyConstant.MUSIC_STATE_KEY, MusicState.PAUSE);
                    if(mCurrentMusicState == MusicState.START){
                        mPlayImageView.setImageResource(R.mipmap.play_blue);
                    }else{
                        mPlayImageView.setImageResource(R.mipmap.pause_blue);
                    }
                }
                break;
        }
    }

    public class CurrentMusicReciver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            mCurrentMusicState = intent.getIntExtra(MyConstant.MUSIC_STATE_KEY, MusicState.PAUSE);
            int index = intent.getIntExtra(MyConstant.MUSIC_INDEX_KEY, -1);
            Music music = mMusics.get(index);
            mMusicNameTextView.setText(music.getName());
            mArtistNameTextView.setText(music.getArtist());

//            mCurrentMusicState = MusicState.START;
            mCurrentMusicIndex = index;
            if(mCurrentMusicState == MusicState.START){
                mPlayImageView.setImageResource(R.mipmap.play_blue);
            }else{
                mPlayImageView.setImageResource(R.mipmap.pause_blue);
            }
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
