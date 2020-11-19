package com.example.first;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<News> newsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initNews();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        //添加分割线
        recyclerView.addItemDecoration(new DividerItemDecoration(
                MainActivity.this, DividerItemDecoration.VERTICAL));
        NewsAdapter adapter = new NewsAdapter(newsList);
        recyclerView.setAdapter(adapter);
    }

    private void initNews() {
        News news = new News();
        news.setAuthor("bytebance");
        news.setTitle("2020字节跳动全球员工摄影大赛邀请函");
        news.setType(0);
        news.setImageId(-1);
        newsList.add(news);
        newsList.add(news);
        News news1 = new News();
        news1.setTitle("Lark.巡洋计划开发者大赛圆满结束");
        news1.setType(1);
        news1.setAuthor("bytedance");
        news1.setImageId(R.drawable.event_02);
        news1.setCover("event_02.png");
        newsList.add(news1);
        newsList.add(news1);
        newsList.add(news1);
        newsList.add(news1);
        newsList.add(news1);
        newsList.add(news1);newsList.add(news1);
        newsList.add(news1);
        newsList.add(news1);

    }
}
