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

public class AccessArticleActivity extends AppCompatActivity {
    private TextView title;
    private TextView author;
    private TextView publishTime;
    private TextView article;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        this.preferences = PreferenceManager.getDefaultSharedPreferences(this);
        setContentView(R.layout.article);
        Bundle bundle = getIntent().getExtras();
        String id = bundle.getString("id");
        sendRequestWithHttpURLConnection(id);
    }

    private void sendRequestWithHttpURLConnection(final String id) {
        Log.e("article_id", id);
        String token = preferences.getString("TOKEN", "");
        //开启线程来发起网络请求
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                BufferedReader reader = null;
                try {
                    URL url = new URL("https://vcapi.lvdaqian.cn/article/" + id);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.addRequestProperty("id", id);
                    connection.addRequestProperty("authorization", "Bearer" + " " + preferences.getString("TOKEN", ""));
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    InputStream inputStream = connection.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while((line = reader.readLine()) != null) {
                        response.append(line);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }
}
