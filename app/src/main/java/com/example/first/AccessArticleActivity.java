package com.example.first;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.ImageReader;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.text.Html;
import android.text.Spanned;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.zzhoujay.markdown.MarkDown;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

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
    private String data;
    private DisplayMetrics dm;
    private int screenWidth;
    private int screenHeight;
    private Context context;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        this.preferences = PreferenceManager.getDefaultSharedPreferences(this);
        setContentView(R.layout.article);
        title = findViewById(R.id.newsTitle);
        author = findViewById(R.id.newsAuthor);
        publishTime = findViewById(R.id.newsPublishTime);
        article = findViewById(R.id.article);
        context = this;
        dm = getResources().getDisplayMetrics();
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;

        Bundle bundle = getIntent().getExtras();
        String id = bundle.getString("id");
        title.setText(bundle.getString("title"));
        author.setText(bundle.getString("author"));
        publishTime.setText(bundle.getString("publishTime"));
        sendRequestWithHttpURLConnection(id);
    }

    public static String removeCharAt(String s, int pos) {

        return s.substring(0, pos) + s.substring(pos + 1);// 使用substring()方法截取0-pos之间的字符串+pos之后的字符串，相当于将要把要删除的字符串删除
    }

    private void sendRequestWithHttpURLConnection(final String id) {
        String token = preferences.getString("TOKEN", "");
        //开启线程来发起网络请求
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d("Tokenlp", preferences.getString("TOKEN", "") + "555");
                    OkHttpClient client;
                    client = new OkHttpClient.Builder()
                            .addInterceptor(new TokenInterceptor(getApplicationContext()))
                            .build();
                    Log.d("Tokenlppp", preferences.getString("TOKEN", "") + "555");
                    Request request = new Request.Builder()
                            .url("https://vcapi.lvdaqian.cn/article/" + id + "?markdown=true")
                            .header("id", id)
                            .header("authorization", "Bearer" +  " " + preferences.getString("TOKEN", ""))
                            .build();
                    Response response = null;
                    response = client.newCall(request).execute();
                    Log.d("code ", String.valueOf(response.code()));
                    if(response.isSuccessful()) {
                        String str = response.body().string();
                        JSONObject jsonObject = new JSONObject(str);
                        data = jsonObject.getString("data");
                        int pos = data.indexOf("演讲嘉宾");
                        if(pos != -1) {
                            Log.d("pos ", String.valueOf(pos));
                            pos += 4;
                            while(pos < data.length() && data.charAt(pos) != '!') data = removeCharAt(data, pos);
                        }
                        pos = data.indexOf("## 流程");
                        if(pos != -1) {
                            pos--;
                            StringBuilder stringBuilder = new StringBuilder(data);
                            stringBuilder.setCharAt(pos, '\n');
                            data = stringBuilder.toString();
                        }

                        Log.e("文章", data);
                        AccessArticleActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                                article.setText(data.substring(10));
                                Log.e("pkm", "qifei");
                                final InputStream stream = new ByteArrayInputStream(data.substring(10).getBytes());
                                article.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        long time = System.nanoTime();
                                        Spanned spanned = MarkDown.fromMarkdown(stream, new Html.ImageGetter() {
                                            @Override
                                            public Drawable getDrawable(String source) {
                                                Log.d("ssss", "getDrawable() called with: source = [" + source + "]");
                                                source = source.toLowerCase();
                                                source = source.substring(0, source.length() - 4);
                                                int id = getResources().getIdentifier(source, "drawable", context.getPackageName());
                                                Log.d("idlp", String.valueOf(id));
                                                Drawable drawable;
                                                try {
//                                                    drawable = drawableFromUrl(source);
                                                    drawable = getResources().getDrawable(id);
                                                    Log.d("height", String.valueOf(screenHeight));
                                                    Log.d("width", String.valueOf(screenWidth));
                                                    drawable.setBounds(0, 0, screenWidth - 60, drawable.getIntrinsicHeight() * (screenWidth - 60) / drawable.getIntrinsicWidth());
                                                } catch (Exception e) {
                                                    Log.w("wwww", "can't get image", e);
                                                    drawable = new ColorDrawable(Color.LTGRAY);
                                                    drawable.setBounds(0, 0, article.getWidth() - article.getPaddingLeft() - article.getPaddingRight(), 400);
                                                }
                                                return drawable;
                                            }
                                        }, article);
                                        long useTime = System.nanoTime() - time;
                                        Toast.makeText(getApplicationContext(), "use time: " + useTime + "ns", Toast.LENGTH_LONG).show();
                                        article.setText(spanned);
                                    }
                                });
                            }
                        });
                    } else {
                        Log.d("failed", "555");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
