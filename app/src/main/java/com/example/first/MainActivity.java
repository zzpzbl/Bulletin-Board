package com.example.first;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<News> newsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{ Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        } else {
            try {
                loadData();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadData() throws FileNotFoundException, UnsupportedEncodingException {

        initNews();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(
                MainActivity.this, DividerItemDecoration.VERTICAL));
        NewsAdapter adapter = new NewsAdapter(newsList);
        recyclerView.setAdapter(adapter);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    try {
                        loadData();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(this, "You denied the permission", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    private void parseJSONWithJSONObject(String jsonData) {
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            for(int i = 0; i < jsonArray.length(); ++i) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String id = jsonObject.getString("id");
                String title = jsonObject.getString("title");
                Log.d("MainActivity", "id is " + id);
                Log.d("MainActivity", "title is " + title);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initNews() throws FileNotFoundException, UnsupportedEncodingException {
//        Log.v("Tagggg", "777");
        File JSONFile = new File("/storage/emulated/0/Android/data/com.tencent.mobileqq/Tencent/QQfile_recv/assets/metadata.json");
        FileReader fileReader = new FileReader(JSONFile);
        Reader reader = new InputStreamReader(new FileInputStream(JSONFile), "utf-8");
        int ch = 0;
        String jsonStr = "";
        StringBuffer stringBuffer = new StringBuffer();
        try {

            while((ch = reader.read()) != -1) {
                stringBuffer.append((char) ch);
            }
            fileReader.close();
            reader.close();
            jsonStr = stringBuffer.toString();
            parseJSONWithJSONObject(jsonStr);
        }catch (IOException e) {
            e.printStackTrace();
        }



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
        Log.v("TAGgggggg", Environment.getExternalStorageDirectory().getAbsolutePath());
    }
}

