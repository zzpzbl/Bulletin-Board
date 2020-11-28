package com.example.first;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.bumptech.glide.Glide;
import com.youth.banner.Banner;
import com.youth.banner.indicator.RoundLinesIndicator;
import com.youth.banner.util.BannerUtils;

public class NewsRecycleAdapter extends RecyclerView.Adapter {
    private final int type_0 = 0;
    private final int type_1 = 1;
    private final int type_2 = 2;
    private final int type_3 = 3;
    private final int type_4 = 4;

    private Context context;
    private List<News> newsList;
    private SharedPreferences preferences;

    List<Integer> ImageID;
    protected boolean isScrolling = false;

    public void setScrolling(boolean scrolling) {
        isScrolling = scrolling;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        View newsView;
        TextView newsAuthor;
        TextView newsTitle;
        ImageView newsImage;
        TextView newsPublishTime;
        ImageView newsImage1;
        ImageView newsImage2;
        ImageView newsImage3;
        ImageView newsImage4;

        public ViewHolder(View view) {
            super(view);
            newsView = view;
            newsAuthor = (TextView) view.findViewById(R.id.newsAuthor);
            newsTitle = (TextView) view.findViewById(R.id.newsTitle);
            newsImage = (ImageView) view.findViewById(R.id.newsImage);
            newsPublishTime=(TextView) view.findViewById(R.id.newsPublishTime);
        }
    }

    static class MyBannerHolder extends RecyclerView.ViewHolder{
        public Banner banner;
        View newsView;
        TextView newsAuthor;
        TextView newsTitle;
        TextView newsPublishTime;
        ImageView newsImage1;
        ImageView newsImage2;
        ImageView newsImage3;
        ImageView newsImage4;

        public MyBannerHolder(View view) {
            super(view);
            banner=view.findViewById(R.id.banner);
            newsView = view;
            newsAuthor = (TextView) view.findViewById(R.id.newsAuthor);
            newsTitle = (TextView) view.findViewById(R.id.newsTitle);
            newsPublishTime=(TextView) view.findViewById(R.id.newsPublishTime);
        }
    }

    public NewsRecycleAdapter(Context context, List<News> newsList) {
        this.context = context;
        this.newsList = newsList;
        this.preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;

        switch (viewType) {
            case type_0:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.news_item_type0, parent, false);
                break;
            case type_1:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.news_item_type1, parent, false);
                break;
            case type_2:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.news_item_type2, parent, false);
                break;
            case type_3:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.news_item_type3, parent, false);
                break;
            case type_4:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.news_item_type4, parent, false);
                break;
        }
        if(viewType!=type_4)
        return new ViewHolder(view);
        else return new MyBannerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        News news = newsList.get(position);
        ViewHolder holder2=null;
        MyBannerHolder holder3=null;
        //转换holder类型
        if(news.getCovers().size()==0)
        {
            holder2=(ViewHolder) holder;
            holder2.newsTitle.setText(news.getTitle());
            holder2.newsAuthor.setText(news.getAuthor());
            holder2.newsPublishTime.setText(news.getPublishTime());
        }
        else {
            holder3=(MyBannerHolder) holder;
            holder3.newsTitle.setText(news.getTitle());
            holder3.newsAuthor.setText(news.getAuthor());
            holder3.newsPublishTime.setText(news.getPublishTime());
        }

        if(news.getType() > 0 && news.getCover() != "") {
            if(news.getCover().equals("tancheng.jpg")) {
                Glide.with(context)
                        .load(R.mipmap.tancheng)
//                    .load("/storage/emulated/0/Android/data/com.tencent.mobileqq/Tencent/QQfile_recv/assets/" + news.getCover())
                        .into(holder2.newsImage);
            }
            if(news.getCover().equals("event_02.png")) {
                Glide.with(context)
                        .load(R.mipmap.event_02)
//                    .load("/storage/emulated/0/Android/data/com.tencent.mobileqq/Tencent/QQfile_recv/assets/" + news.getCover())
                        .into(holder2.newsImage);
            }
            if(news.getCover().equals("teambuilding_04.png")) {
                Glide.with(context)
                        .load(R.mipmap.teambuilding_04)
//                    .load("/storage/emulated/0/Android/data/com.tencent.mobileqq/Tencent/QQfile_recv/assets/" + news.getCover())
                        .into(holder2.newsImage);
            }
        }

        if(news.getCovers().size() > 0) {
           Banner banner=holder3.banner;
            newsList.get(0).setImageid(R.mipmap.tb09_1);
            newsList.get(1).setImageid(R.mipmap.tb09_2);
            newsList.get(2).setImageid(R.mipmap.tb09_3);
            newsList.get(3).setImageid(R.mipmap.tb09_4);
            newsList.get(4).setImageid(R.mipmap.tb09_1);
            banner.setAdapter(new  ImageNetAdapter(context,newsList));
            banner.setIndicator(new RoundLinesIndicator(context));
            banner.setIndicatorSelectedWidth((int) BannerUtils.dp2px(15));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                preferences.edit().clear().commit();
                String token = preferences.getString("TOKEN", "");
                //如果 token 为空，那么进入登录页面，否则进入文章页面
                if(token.equals("")) {
                    Intent intent = new Intent(v.getContext(), LoginActivity.class);
                    v.getContext().startActivity(intent);
                } else {
                    Intent intent = new Intent(v.getContext(), AccessArticleActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("id", newsList.get(position).getId());
                    bundle.putString("title", newsList.get(position).getTitle());
                    bundle.putString("author", newsList.get(position).getAuthor());
                    bundle.putString("publishTime", newsList.get(position).getPublishTime());
                    intent.putExtras(bundle);
                    v.getContext().startActivity(intent);
                }
            }
        });
    }



    @Override
    public int getItemCount() { return newsList.size(); }

    @Override
    public int getItemViewType(int position) {
        if(newsList.get(position).getType() == 0) {
            return type_0;
        } else if(newsList.get(position).getType() == 1) {
            return type_1;
        } else if(newsList.get(position).getType() == 2) {
            return type_2;
        } else if(newsList.get(position).getType() == 3) {
            return type_3;
        } else {
            return  type_4;
        }
    }
}