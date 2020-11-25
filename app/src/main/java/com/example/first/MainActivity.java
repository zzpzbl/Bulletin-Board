package com.example.first;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import com.alibaba.fastjson.JSON;

import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import static android.widget.NumberPicker.OnScrollListener.SCROLL_STATE_IDLE;

public class MainActivity extends AppCompatActivity{

    private List<News> newsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //获得读取存储权限
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
        final NewsAdapter adapter = new NewsAdapter(this, newsList);
        //对 recyclerview 进行监听，提升滑动流畅度
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if(newState == SCROLL_STATE_IDLE) {
                    adapter.setScrolling(false);
                    adapter.notifyDataSetChanged();
                } else {
                    adapter.setScrolling(true);
                }
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
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

    private JSONArray parseJSONWithJSONObject(String jsonData) {
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            for(int i = 0; i < jsonArray.length(); ++i) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String id = jsonObject.getString("id");
                String title = jsonObject.getString("title");
            }
            return jsonArray;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void initNews() throws FileNotFoundException, UnsupportedEncodingException {
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
//            com.alibaba.fastjson.JSONObject obj = JSON.parseObject(jsonStr);
            JSONArray jsonArray = parseJSONWithJSONObject(jsonStr);
            for(int i = 0; i < jsonArray.length(); ++i) {
                News news = new News();
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String id = jsonObject.getString("id");
                String title = jsonObject.getString("title");
                String author = jsonObject.getString("author");
                String publishTime = jsonObject.getString("publishTime");
                String cover = "";
                List<String> covers = new ArrayList<>();
                int type = jsonObject.getInt("type");
                if(jsonObject.has("cover")) {
                    cover = jsonObject.getString("cover");
                }
                if(jsonObject.has("covers")) {
                    covers = JSON.parseArray(jsonObject.getString("covers"), String.class);
                }

                news.setType(type);
                news.setCover(cover);
                news.setAuthor(author);
                news.setCovers(covers);
                news.setTitle(title);
                news.setPublishTime(publishTime);
                news.setId(id);
                Log.d("id", id);
                Log.d("author", author);
                newsList.add(news);
            }

        }catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        Log.v("TAGgggggg", Environment.getExternalStorageDirectory().getAbsolutePath());
    }
}

