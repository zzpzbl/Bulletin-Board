package com.example.first;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AccessArticleActivity extends AppCompatActivity {
    private TextView title;
    private TextView author;
    private TextView publishTime;
    private TextView article;
    private SharedPreferences preferences;
    private String str = "";

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        this.preferences = PreferenceManager.getDefaultSharedPreferences(this);
        setContentView(R.layout.article);
        title = findViewById(R.id.newsTitle);
        author = findViewById(R.id.newsAuthor);
        publishTime = findViewById(R.id.newsPublishTime);
        article = findViewById(R.id.article);

        Bundle bundle = getIntent().getExtras();
        String id = bundle.getString("id");
        title.setText(bundle.getString("title"));
        author.setText(bundle.getString("author"));
        publishTime.setText(bundle.getString("publishTime"));
        sendRequestWithHttpURLConnection(id);
    }

    private void sendRequestWithHttpURLConnection(final String id) {
        Log.e("article_id", id);
        String token = preferences.getString("TOKEN", "");
        //开启线程来发起网络请求
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url("https://vcapi.lvdaqian.cn/article/" + id)
                            .header("id", id)
                            .header("authorization", "Bearer" +  " " + preferences.getString("TOKEN", ""))
                            .build();
                    Response response = null;
                    response = client.newCall(request).execute();
                    if(response.isSuccessful()) {
                        String str = response.body().string();
                        JSONObject jsonObject = new JSONObject(str);
                        final String data;
                        data = jsonObject.getString("data");
                        Log.e("文章", data);
                        AccessArticleActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                article.setText(data.substring(137));
                            }
                        });
                    } else {
                        Log.d("failed", "sb");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static String getToken() {
        final String token = "";
        return token;
    }
}
