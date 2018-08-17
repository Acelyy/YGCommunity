package com.yonggang.ygcommunity.PhotoPicker;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.yonggang.ygcommunity.R;

import java.io.File;
import java.util.ArrayList;

import me.iwf.photopicker.utils.AndroidLifecycleUtils;

/**
 * Created by liyangyang on 2017/6/1.
 */

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.ViewHolder> {

    private ArrayList<String> photoPaths = new ArrayList<String>();
    private LayoutInflater inflater;
    private Context mContext;

    public final static int TYPE_ADD = 1;
    public final static int TYPE_PHOTO = 2;
    public final static int MAX = 5;

    public PhotoAdapter(Context mContext, ArrayList<String> photoPaths) {
        this.photoPaths = photoPaths;
        this.mContext = mContext;
        this.inflater = LayoutInflater.from(mContext);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        switch (viewType) {
            case TYPE_ADD:
                itemView = inflater.inflate(R.layout.item_pic_add, parent, false);
                break;
            case TYPE_PHOTO:
                itemView = inflater.inflate(R.layout.__picker_item_photo, parent, false);
                break;
        }
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_PHOTO) {
            Uri uri = Uri.fromFile(new File(photoPaths.get(position)));
            boolean canLoadImage = AndroidLifecycleUtils.canLoadImage(holder.ivPhoto.getContext());
            if (canLoadImage) {
                Glide.with(mContext)
                        .load(uri)
                        .centerCrop()
                        .thumbnail(0.1f)
                        .placeholder(R.drawable.__picker_ic_photo_black_48dp)
                        .error(R.drawable.__picker_ic_broken_image_black_48dp)
                        .into(holder.ivPhoto);
            }
        }
    }

    @Override
    public int getItemCount() {
        int count = photoPaths.size() + 1;
        if (count > MAX) {
            count = MAX;
        }
        return count;
    }

    @Override
    public int getItemViewType(int position) {
        return (position == photoPaths.size() && position != MAX) ? TYPE_ADD : TYPE_PHOTO;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivPhoto;
        private View vSelected;

        public ViewHolder(View itemView) {
            super(itemView);
            ivPhoto = (ImageView) itemView.findViewById(R.id.iv_photo);
            vSelected = itemView.findViewById(R.id.v_selected);
            if (vSelected != null) vSelected.setVisibility(View.GONE);
        }
    }

}
