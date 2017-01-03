package com.example.asus.hillplayer.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.asus.hillplayer.R;
import com.example.asus.hillplayer.adapter.ArtistListAdapter;
import com.example.asus.hillplayer.beans.Music;
import com.example.asus.hillplayer.constant.MyConstant;
import com.example.asus.hillplayer.itemderaction.ListDeraction;
import com.example.asus.hillplayer.presenter.fragmentPresenter.PArtistList;
import com.example.asus.hillplayer.util.MyLog;
import com.example.asus.hillplayer.view.fragmentViewL.IViewArtistList;

import java.util.List;

/**
 * Created by asus-cp on 2016-12-27.
 */

public class ArtistListFragment extends BaseFragment<IViewArtistList, PArtistList>
        implements IViewArtistList{

    private RecyclerView mRecyclerView;

    private List<Music> mMusics;

    private ArtistListAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_artist_list, container, false);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.recyler_artist_list);
        initData();
        return v;
    }

    @Override
    protected PArtistList createPresenter() {
        return new PArtistList();
    }

    @Override
    public void initData() {
        super.initData();
        Bundle bundle = getArguments();
        mMusics = (List<Music>) bundle.getSerializable(MyConstant.MUSICS_KEY);

        MyLog.d(TAG, mMusics.toString());
        mAdapter = new ArtistListAdapter(mContext, mMusics);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext,
                LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        ListDeraction deraction = new ListDeraction(mContext);
        mRecyclerView.addItemDecoration(deraction);
        mRecyclerView.setAdapter(mAdapter);
    }
}
