package com.example.first;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
    private List<News> newsList;

    private final int type_0 = 0;
    private final int type_1 = 1;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View newsView;
        TextView newsAuthor;
        TextView newsTitle;
        ImageView newsImage;

        public ViewHolder(View view) {
            super(view);
            newsView = view;
            newsAuthor = (TextView) view.findViewById(R.id.newsAuthor);
            newsTitle = (TextView) view.findViewById(R.id.newsTitle);
            newsImage = (ImageView) view.findViewById(R.id.newsImage);
        }
    }

    public NewsAdapter(List<News> newsList) {
        this.newsList = newsList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if(viewType == type_0) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.news_item_type0, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.news_item_type1, parent, false);
        }
        final ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        News news = newsList.get(position);
        holder.newsTitle.setText(news.getTitle());
        holder.newsAuthor.setText(news.getAuthor());
        if(news.getImageId() > 0) {
//            holder.newsImage.setImageResource(news.getImageId());

            Bitmap bitmap = getLocalBitmap("/storage/emulated/0/Android/data/com.tencent.mobileqq/Tencent/QQfile_recv/assets/" + news.getCover());
//            Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream("../../../res/drawable/" + news.getCover()));
            holder.newsImage.setImageBitmap(bitmap);
        }
    }

    private Bitmap getLocalBitmap(String s) {
        try {
            FileInputStream fis = new FileInputStream(s);
            return BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int getItemCount() { return newsList.size(); }

    @Override
    public int getItemViewType(int position) {
        if(newsList.get(position).getType() == 0) {
            return type_0;
        } else {
            return type_1;
        }
    }
}