package com.example.asus.hillplayer.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.asus.hillplayer.R;
import com.example.asus.hillplayer.activity.MainActivity;
import com.example.asus.hillplayer.adapter.LocalMusicListAdapter;
import com.example.asus.hillplayer.beans.Music;
import com.example.asus.hillplayer.callback.OnItemClikListenerMy;
import com.example.asus.hillplayer.constant.MusicState;
import com.example.asus.hillplayer.constant.MyConstant;
import com.example.asus.hillplayer.itemderaction.ListDeraction;
import com.example.asus.hillplayer.presenter.fragmentPresenter.PLocalMusicList;
import com.example.asus.hillplayer.service.MediaPlayerService;
import com.example.asus.hillplayer.util.MyLog;
import com.example.asus.hillplayer.view.fragmentViewL.IViewLoaclMusicList;

import java.io.Serializable;
import java.util.List;

/**
 * 本地音乐列表
 * Created by asus-cp on 2016-12-22.
 */

public class LocalMusicListFragment extends BaseFragment<IViewLoaclMusicList, PLocalMusicList>
implements IViewLoaclMusicList{

    private RecyclerView mRecyclerView;

    private LocalMusicListAdapter mAdapter;

    private List<Music> mMusics;

    private LocalBroadcastManager mLocalBroadcastManager;

    private CurrentMusicReciver mCurrentMusicReciver;

    private MainActivity mMainActivity;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_local_music_list,container, false);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.recyler_local_music_list);
        refreshVaryViewController(mRecyclerView);
        initData();
        return v;
    }

    @Override
    protected PLocalMusicList createPresenter() {
        return new PLocalMusicList();
    }


    @Override
    public void initData() {
        Bundle bundle = getArguments();
        mMusics = (List<Music>) bundle.getSerializable(MyConstant.MUSICS_KEY);
        mAdapter = new LocalMusicListAdapter(mContext, mMusics);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext,
                LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        ListDeraction listDeraction = new ListDeraction(mContext);
        mRecyclerView.addItemDecoration(listDeraction);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setmOnItemClikListenerMy(new OnItemClikListenerMy() {
            @Override
            public void onItemClick(View v, int position) {
                Music music = mMusics.get(position);
                Intent intent = new Intent(mContext, MediaPlayerService.class);
                intent.putExtra(MyConstant.MUSIC_PATH_KEY, music.getData());
                intent.putExtra(MyConstant.MUSIC_STATE_KEY, MusicState.START);
                intent.putExtra(MyConstant.MUSICS_KEY, (Serializable) mMusics);
                mContext.startService(intent);
            }
        });
        mMainActivity = (MainActivity) getActivity();

    }


    @Override
    public void refreshUI(List<Music> musics) {
        MyLog.d(TAG, "musics="+musics);
        mMusics.clear();
        mMusics.addAll(musics);
        mAdapter.notifyDataSetChanged();

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(mContext);
        mCurrentMusicReciver = new CurrentMusicReciver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MyConstant.CURRENT_MUSIC_RECEIVER_ACTION);
        mLocalBroadcastManager.registerReceiver(mCurrentMusicReciver, intentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mLocalBroadcastManager.unregisterReceiver(mCurrentMusicReciver);
    }

    public class CurrentMusicReciver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            int index = intent.getIntExtra(MyConstant.MUSIC_INDEX_KEY, -1);
            mAdapter.changItemTextColor(index);
        }
    }
}
