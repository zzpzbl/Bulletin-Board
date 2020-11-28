package com.example.first;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.first.util.ViewUtil;
import com.google.android.material.internal.ViewUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText et_username;
    private EditText et_password;
    private SharedPreferences preferences;
    private Button sign_up;
    private Button sign_in;

    @Override
    protected void onCreate(Bundle saveInstanceState) {

        super.onCreate(saveInstanceState);
        setContentView(R.layout.login);

        et_username = findViewById(R.id.rb_username);
        et_password = findViewById(R.id.rb_password);
        et_username.addTextChangedListener(new HideTextWatcher(et_username));
        et_password.addTextChangedListener(new HideTextWatcher(et_password));
        this.preferences = PreferenceManager.getDefaultSharedPreferences(this);
        findViewById(R.id.btn_login).setOnClickListener(this);
        findViewById(R.id.btn_register).setOnClickListener(this);
    }
    
    private class HideTextWatcher implements TextWatcher {
        private EditText mView;
        private int mMaxLength;
        private CharSequence mStr;
        
        HideTextWatcher(EditText v) {
            super();
            mView = v;
            mMaxLength = ViewUtil.getMaxLength(v);

        }

        // 在编辑框的输入文本变化前触发
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        // 在编辑框的输入文本变化时触发
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            mStr = s;
        }

        // 在编辑框的输入文本变化后触发
        public void afterTextChanged(Editable editable) {
            if(mStr == null || mStr.length() == 0)
                return;

        }
    }

    public void sendRequestWithHttpURLConnection() {
        String username = et_username.toString();
        String password = et_password.toString();
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("username", username)
                .add("password", password)
                .build();

        final Request request = new Request.Builder()
                .url("https://vcapi.lvdaqian.cn/login")
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()) {
                    Log.d("kwwl","response.code()=="+response.code());
//                                Log.d("kwwl","response.body().string()==" + response.body().string());
                    String str = response.body().string();
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(str);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String ret = null;
                    try {
                        ret = jsonObject.getString("message");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if(ret.equals("Get token success")) {
                        Log.v("zms", "nice!");
                        SharedPreferences.Editor editor = null;
                        //获取haredPreferences.Editor对象，尝试写数据
                        editor = preferences.edit();
                        try {
                            editor.putString("TOKEN", jsonObject.getString("token"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        editor.apply();
                        String token = preferences.getString("TOKEN", "");
//                        Log.e("Token", token + " ");
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    } else {
                        Log.v("zms", "sb");
                    }
                }
                else {
                    Log.e("zms","failed");
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_login || v.getId() == R.id.btn_register) {
            Log.v("login ", "success");
            SharedPreferences.Editor editor = null;
            //获取haredPreferences.Editor对象，尝试写数据
            editor = preferences.edit();
            editor.putString("USERNAME", et_username.toString());
            editor.putString("PASSWORD", et_password.toString());
            sendRequestWithHttpURLConnection();
        }
    }
}
