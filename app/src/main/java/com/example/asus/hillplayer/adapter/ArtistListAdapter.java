package com.example.asus.hillplayer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.hillplayer.R;
import com.example.asus.hillplayer.beans.Music;
import com.example.asus.hillplayer.callback.OnItemClikListenerMy;

import java.util.List;

/**
 * Created by asus-cp on 2016-12-28.
 */

public class ArtistListAdapter extends RecyclerView.Adapter<ArtistListAdapter.MyViewHolder> {

    private Context mContext;

    private List<Music> mMusics;

    private LayoutInflater mLayoutInflater;

    private OnItemClikListenerMy mOnItemClikListenerMy;

    public ArtistListAdapter(Context mContext, List<Music> mMusics) {
        this.mContext = mContext;
        this.mMusics = mMusics;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = mLayoutInflater.inflate(R.layout.item_artist_list, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(v);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        Music music = mMusics.get(position);
        holder.nameTextView.setText(music.getArtist());
        holder.countTextView.setText("1"+"首");
        holder.moreImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext,"点击了",Toast.LENGTH_SHORT).show();
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnItemClikListenerMy != null){
                    mOnItemClikListenerMy.onItemClick(v, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mMusics.size();
    }

    public void setmOnItemClikListenerMy(OnItemClikListenerMy mOnItemClikListenerMy) {
        this.mOnItemClikListenerMy = mOnItemClikListenerMy;
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView nameTextView;
        TextView countTextView;
        ImageView moreImageView;

        public MyViewHolder(View itemView) {
            super(itemView);
            nameTextView = (TextView) itemView.findViewById(R.id.text_artist_name);
            countTextView = (TextView) itemView.findViewById(R.id.text_song_count);
            moreImageView = (ImageView) itemView.findViewById(R.id.img_more);
        }
    }
}
