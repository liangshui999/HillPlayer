package com.example.asus.hillplayer.activity;

import android.app.Activity;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.asus.hillplayer.R;
import com.example.asus.hillplayer.adapter.ListSlidAdapter;
import com.example.asus.hillplayer.receiver.INetObserver;
import com.example.asus.hillplayer.receiver.INetStateReceiver;
import com.example.asus.hillplayer.receiver.NetStateReceiver;
import com.example.asus.hillplayer.util.VaryViewController;
import com.example.asus.hillplayer.util.VaryViewHelper;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Toolbar mToolBar;

    private DrawerLayout mDrawerLayout;

    private ListView mSlidListView;

    private LinearLayout mTargetView;

    private NetStateReceiver mNetStateReceiver;

    private VaryViewController mVaryViewController;

    public static final int LOAD=1;

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
                        mainActivity.mVaryViewController.showErrorView("加载出错了...", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(mainActivity,"错了",Toast.LENGTH_SHORT).show();
                                mainActivity.mVaryViewController.showTargetView();
                            }
                        });
                        //mainActivity.myHandler.sendEmptyMessageDelayed(TARGET,3000);
                        break;
                    case TARGET:
                        mainActivity.mVaryViewController.showTargetView();
                        break;
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolBar= (Toolbar) findViewById(R.id.toolbar);
        mDrawerLayout= (DrawerLayout) findViewById(R.id.drawlayout_slid);
        mSlidListView= (ListView) findViewById(R.id.list_slid_menu);
        mTargetView= (LinearLayout) findViewById(R.id.ll_container);

        //必须写在最前面
        mToolBar.setTitle(R.string.test_text);
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

        List<String> menus=new ArrayList<>();
        menus.add(getResources().getString(R.string.music_player));
        menus.add(getResources().getString(R.string.radio));
        menus.add(getResources().getString(R.string.video_player));
        ListSlidAdapter adapter=new ListSlidAdapter(this,menus);
        mSlidListView.setAdapter(adapter);

        mVaryViewController=new VaryViewController(new VaryViewHelper(mTargetView));
        mVaryViewController.showLoadingView("正在加载...");
        myHandler.sendEmptyMessageDelayed(ERROR,3000);

        mNetStateReceiver=new NetStateReceiver();
        mNetStateReceiver.registerObserver(new INetObserver() {
            @Override
            public void onNetConnected() {
                Toast.makeText(MainActivity.this,"网络连接",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNetDisConnected() {
                Toast.makeText(MainActivity.this,"网络断开连接",Toast.LENGTH_SHORT).show();
            }
        });
        IntentFilter intentFilter=new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(mNetStateReceiver,intentFilter);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mNetStateReceiver);
    }

    /* @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home: //自带返回键的点击事件
                //finish();
            break;
        }
        return super.onOptionsItemSelected(item);
    }*/
}
