package com.example.asus.hillplayer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.asus.hillplayer.R;

import java.util.List;

/**
 * 侧滑里面的菜单项的适配器
 * Created by asus-cp on 2016-12-19.
 */

public class ListSlidAdapter extends BaseAdapter {

    private Context mContex;

    private List<String> mMenus;

    private LayoutInflater mLayoutInflater;

    public ListSlidAdapter(Context mContex, List<String> mMenus) {
        this.mContex = mContex;
        this.mMenus = mMenus;
        mLayoutInflater=LayoutInflater.from(mContex);
    }

    @Override
    public int getCount() {
        return mMenus.size();
    }

    @Override
    public Object getItem(int position) {
        return mMenus.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v=convertView;
        ViewHolder viewHolder=null;
        if(v==null){
            v=mLayoutInflater.inflate(R.layout.item_slid_menu_layout,null);
            viewHolder=new ViewHolder();
            viewHolder.mTextView= (TextView) v.findViewById(R.id.text_item_slid_menu);
            v.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) v.getTag();
        }
        viewHolder.mTextView.setText(mMenus.get(position));
        return v;
    }

    private class ViewHolder{
        TextView mTextView;
    }
}
