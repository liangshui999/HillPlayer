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
import com.example.asus.hillplayer.util.MyLog;

import java.util.List;

/**
 * Created by asus-cp on 2016-12-23.
 */

public class LocalMusicListAdapter extends RecyclerView.Adapter<LocalMusicListAdapter.MyViewHolder> {

    private static String TAG;

    private Context mContext;

    private List<Music> mMusics;

    private LayoutInflater mLayoutInflater;

    public LocalMusicListAdapter(Context mContext, List<Music> mMusics) {
        TAG = this.getClass().getSimpleName();
        this.mContext = mContext;
        this.mMusics = mMusics;
        mLayoutInflater = LayoutInflater.from(mContext);
        MyLog.d(TAG, "mMusics="+mMusics);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = mLayoutInflater.inflate(R.layout.item_local_music_list,parent,false);//这里的最后一项，一定要是false
        MyViewHolder myViewHolder = new MyViewHolder(v);
        MyLog.d(TAG,"onCreateViewHolder");
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        Music music = mMusics.get(position);
        holder.nameTextView.setText(music.getName());
        holder.artistTextView.setText(music.getArtist());
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "点击了", Toast.LENGTH_SHORT).show();
            }
        });
        MyLog.d(TAG,"onBindViewHolder"+"position="+position);
    }

    @Override
    public int getItemCount() {
        return mMusics.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;

        TextView nameTextView;

        TextView artistTextView;

        public MyViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.img_close);
            nameTextView = (TextView) itemView.findViewById(R.id.text_music_name);
            artistTextView = (TextView) itemView.findViewById(R.id.text_music_artist);
        }
    }
}
