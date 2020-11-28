package com.example.first;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.first.util.ViewUtil;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
    private final int type_0 = 0;
    private final int type_1 = 1;
    private final int type_2 = 2;
    private final int type_3 = 3;
    private final int type_4 = 4;

    private Context context;
    private List<News> newsList;
    private SharedPreferences preferences;

    List<ImageView> imageViewsList;

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
        LinearLayout linearLayout;

        public ViewHolder(View view) {
            super(view);
            newsView = view;
            newsAuthor = (TextView) view.findViewById(R.id.newsAuthor);
            newsTitle = (TextView) view.findViewById(R.id.newsTitle);
            newsImage = (ImageView) view.findViewById(R.id.newsImage);
            newsPublishTime=(TextView) view.findViewById(R.id.newsPublishTime);
            linearLayout = view.findViewById(R.id.linearLayout3);
        }
    }

    public NewsAdapter(Context context, List<News> newsList) {
        this.context = context;
        this.newsList = newsList;
        this.preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
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

        final ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        News news = newsList.get(position);
        holder.newsTitle.setText(news.getTitle());
        holder.newsAuthor.setText(news.getAuthor());
        holder.newsPublishTime.setText(news.getPublishTime());
        if(news.getType() > 0 && news.getCover() != "") {
            String cover = news.getCover().substring(0, news.getCover().length() - 4);
            Glide.with(context)
                    .load(context.getResources().getIdentifier(cover, "drawable", context.getPackageName()))
//                    .load("/storage/emulated/0/Android/data/com.tencent.mobileqq/Tencent/QQfile_recv/assets/" + news.getCover())
                    .into(holder.newsImage);
        }

        if(news.getCovers().size() > 0) {
            for(int i = 0; i < news.getCovers().size(); ++i) {
                Log.d("image", news.getCovers().get(i));
                String cover = news.getCovers().get(i).substring(0, news.getCovers().get(i).length() - 4);
                Log.d("cover", cover);
                Glide.with(context)
                        .load(context.getResources().getIdentifier(cover, "drawable", context.getPackageName()))
//                    .load("/storage/emulated/0/Android/data/com.tencent.mobileqq/Tencent/QQfile_recv/assets/" + news.getCovers().get(0))
                        .into((ImageView) holder.linearLayout.getChildAt(i));
            }
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