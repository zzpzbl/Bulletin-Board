package com.example.first;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ImageHolder extends RecyclerView.ViewHolder {
    public ImageView imageView;
    public ImageView newsImage;



    public ImageHolder(@NonNull View view) {
        super(view);
        this.newsImage = (ImageView) view;
    }
}
