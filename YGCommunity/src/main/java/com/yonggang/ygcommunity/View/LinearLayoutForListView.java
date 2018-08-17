package com.yonggang.ygcommunity.View;

import android.content.Context;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.widget.Adapter;
import android.widget.LinearLayout;

/**
 * Created by liyangyang on 2017/4/27.
 */

public class LinearLayoutForListView extends LinearLayout {
    private Adapter mAdapter;
    private SparseArray<View> mViewHolders;
//    private AdapterDataSetObserver mDataSetObserver;
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public LinearLayoutForListView(Context context) {
        super(context);
    }

    public LinearLayoutForListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LinearLayoutForListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void bindView() {
        removeAllViews();
        setOrientation(LinearLayout.VERTICAL);
        final int count = mAdapter.getCount();
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mViewHolders = new SparseArray<View>(count);
        for (int i = 0; i < count; i++) {
            final int position = i;
            View v = mAdapter.getView(i, null, null);
            v.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onclick(position);
                    }
                }
            });
            mViewHolders.put(i, v);
            addView(v, layoutParams);
        }
    }

    public void setAdapter(Adapter adapter) {
        mAdapter = adapter;
        if(mAdapter==null){
            removeAllViews();
        }else{
            bindView();
        }

    }

    public Adapter getAdapter() {
        return mAdapter;
    }

//    @Override
//    protected void onAttachedToWindow() {
//        super.onAttachedToWindow();
//        if (mAdapter != null && mDataSetObserver == null) {
//            mDataSetObserver = new AdapterDataSetObserver();
//            mAdapter.registerDataSetObserver(mDataSetObserver);
//        }
//    }
//
//    @Override
//    protected void onDetachedFromWindow() {
//        super.onDetachedFromWindow();
//        if (mAdapter != null && mDataSetObserver != null) {
//            //mAdapter.unregisterDataSetObserver(mDataSetObserver);
//        }
//    }

//    private class AdapterDataSetObserver extends DataSetObserver {
//        @Override
//        public void onChanged() {
//            final int count = mAdapter.getCount();
//            for (int i = 0; i < count; i++) {
//                mAdapter.getView(i, mViewHolders.get(i, null), null);
//            }
//            requestLayout();
//        }
//    }

    public interface OnItemClickListener {
        void onclick(int position);
    }

}
