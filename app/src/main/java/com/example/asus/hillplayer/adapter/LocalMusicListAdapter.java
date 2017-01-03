package com.example.asus.hillplayer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.hillplayer.R;
import com.example.asus.hillplayer.beans.Music;
import com.example.asus.hillplayer.callback.OnItemClikListenerMy;
import com.example.asus.hillplayer.util.MyLog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asus-cp on 2016-12-23.
 */

public class LocalMusicListAdapter extends RecyclerView.Adapter<LocalMusicListAdapter.MyViewHolder> {

    private static String TAG;

    private Context mContext;

    private List<Music> mMusics;

    private LayoutInflater mLayoutInflater;

    private OnItemClikListenerMy mOnItemClikListenerMy;

    private List<Boolean> isNeedChangeColor;//记录每一个小项是否需要改变文字颜色

    public LocalMusicListAdapter(Context mContext, List<Music> mMusics) {
        TAG = this.getClass().getSimpleName();
        this.mContext = mContext;
        this.mMusics = mMusics;
        mLayoutInflater = LayoutInflater.from(mContext);
        isNeedChangeColor = new ArrayList<>();
        for(int i = 0; i < mMusics.size(); i++){
            isNeedChangeColor.add(false);
        }
        MyLog.d(TAG, "mMusics="+mMusics);
    }

    private void allNotSelected(){
        for(int i = 0; i < mMusics.size(); i++){
            isNeedChangeColor.set(i, false);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = mLayoutInflater.inflate(R.layout.item_local_music_list,parent,false);//这里的最后一项，一定要是false
        MyViewHolder myViewHolder = new MyViewHolder(v);
        MyLog.d(TAG,"onCreateViewHolder");
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        Music music = mMusics.get(position);
        holder.nameTextView.setText(music.getName());
        holder.artistTextView.setText(music.getArtist());
        if(isNeedChangeColor.get(position)){
            holder.nameTextView.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
            holder.artistTextView.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
        }else {
            holder.nameTextView.setTextColor(mContext.getResources().getColor(R.color.primary_text));
            holder.artistTextView.setTextColor(mContext.getResources().getColor(R.color.secondary_text));
        }

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "点击了", Toast.LENGTH_SHORT).show();
            }
        });
        MyLog.d(TAG,"onBindViewHolder"+"position="+position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnItemClikListenerMy != null){
                    mOnItemClikListenerMy.onItemClick(v, position);
                }
                changItemTextColor(position);
            }
        });
    }

    /**
     * 改变item的文字颜色
     * @param position
     */
    public void changItemTextColor(int position) {
        allNotSelected();
        isNeedChangeColor.set(position, true);
        notifyDataSetChanged();//之所以点击能改变颜色，这个是核心，这个方法会造成重新调用onBindViewHolder（）方法
    }

    @Override
    public int getItemCount() {
        return mMusics.size();
    }

    public void setmOnItemClikListenerMy(OnItemClikListenerMy onItemClikListenerMy){
        this.mOnItemClikListenerMy = onItemClikListenerMy;
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

         ImageView imageView;

         TextView nameTextView;

         TextView artistTextView;

         MyViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.img_close);
            nameTextView = (TextView) itemView.findViewById(R.id.text_music_name);
            artistTextView = (TextView) itemView.findViewById(R.id.text_music_artist);
        }
    }
}
