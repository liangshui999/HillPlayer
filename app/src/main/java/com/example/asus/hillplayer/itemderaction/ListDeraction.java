package com.example.asus.hillplayer.itemderaction;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;

import com.example.asus.hillplayer.R;

/**
 * 给纵向的recylerview使用的itemderaction
 * Created by asus-cp on 2016-12-28.
 */

public class ListDeraction extends RecyclerView.ItemDecoration {

    private Context mContext;

    private int mDividerHeight;

    private int mColor;

    private Paint mPaint;

    private RectF mRectF;

    /**
     * 是否需要顶部分割线，默认是false
     */
    private boolean isNeedTopDivider;

    public ListDeraction(Context mContext, int mDividerHeight ,int mColor) {
        this.mContext = mContext;
        this.mDividerHeight = convertDp2px(mContext, mDividerHeight);
        this.mColor = mColor;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mColor);
        mRectF = new RectF();
    }

    public ListDeraction(Context mContext){
        this(mContext, 1, mContext.getResources().getColor(R.color.divider_color));
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        int childCount = parent.getChildCount();
        for(int i = 1; i < childCount-1; i++){
            View child = parent.getChildAt(i);
            mRectF.left = parent.getPaddingLeft();
            mRectF.top = child.getBottom();
            mRectF.right = child.getWidth() - parent.getPaddingRight();
            mRectF.bottom = mRectF.top + mDividerHeight;
            c.drawRect(mRectF, mPaint);
        }

        View childOne = parent.getChildAt(0);
        //画第一个的顶部
        if(isNeedTopDivider){
            mRectF.left = parent.getPaddingLeft();
            mRectF.right = childOne.getWidth() - parent.getPaddingRight();
            mRectF.bottom = childOne.getTop();
            mRectF.top = mRectF.bottom - mDividerHeight;
            c.drawRect(mRectF, mPaint);
        }

        //画第一个的底部
        mRectF.left = parent.getPaddingLeft();
        mRectF.top = childOne.getBottom();
        mRectF.right = childOne.getWidth() - parent.getPaddingRight();
        mRectF.bottom = mRectF.top + mDividerHeight;
        c.drawRect(mRectF, mPaint);

    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int childCount = parent.getChildCount();
        for(int i = 0; i < childCount-1; i++){
            outRect.left = 0;
            outRect.top = 0;
            outRect.right = 0;
            outRect.bottom = mDividerHeight;
            if(i == 0){
                outRect.top = mDividerHeight;
            }
        }

    }

    private int convertDp2px(Context context, int size){
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float densty = displayMetrics.density;
        return (int) (size*densty);
    }

    public void setNeedTopDivider(boolean topDivider){
        isNeedTopDivider = topDivider;
    }
}
