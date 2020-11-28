package com.example.first;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.youth.banner.adapter.BannerAdapter;
import com.youth.banner.holder.BannerImageHolder;
import com.youth.banner.util.BannerUtils;

import java.net.CookieHandler;
import java.sql.ClientInfoStatus;
import java.util.ArrayList;
import java.util.List;

public class ImageNetAdapter extends BannerAdapter<News, ImageHolder> {
    private final Context context;

    public ImageNetAdapter(Context context, List<News> datas) {
        super(datas);
        this.context=context;
        Log.v("--find1","is null?"+context);
    }

    @Override
    public ImageHolder onCreateHolder(ViewGroup parent, int viewType) {
        ImageView view =(ImageView)BannerUtils.getView(parent,R.layout.banner_image) ;
        view.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return new ImageHolder(view);
    }

    @Override
    public void onBindView(ImageHolder holder, News news, int position, int size) {

        Glide.with(context)
                .load(news.getimgagid())
//                    .load("/storage/emulated/0/Android/data/com.tencent.mobileqq/Tencent/QQfile_recv/assets/" + news.getCovers().get(0))
                .into(holder.newsImage);


    }
}
