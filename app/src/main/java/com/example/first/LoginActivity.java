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

    private void sendRequestWithHttpURLConnection() {
        //开启线程来发起网络请求
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                BufferedReader reader = null;
                try {
                    String username = et_username.toString();
                    String password = et_password.toString();
                    URL url = new URL("https://vcapi.lvdaqian.cn/login");
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
                    dataOutputStream.writeBytes("username=" + username + "&password=" + password);
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    InputStream inputStream = connection.getInputStream();
                    //下面对获得的输入流进行读取
                    reader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while((line = reader.readLine()) != null) {
                        response.append(line);
                    }

                    Log.d("return ", response.toString());

                    JSONObject jsonObject = new JSONObject(response.toString());

                    String ret = jsonObject.getString("message");
//                    Log.v("ret ", ret);
                    if(ret.equals("Get token success")) {
//                        Log.v("zms", "nice!");
                        SharedPreferences.Editor editor = null;
                        //获取haredPreferences.Editor对象，尝试写数据
                        editor = preferences.edit();
                        editor.putString("TOKEN", jsonObject.getString("token"));
                        editor.apply();
                        String token = preferences.getString("TOKEN", "");
                        Log.e("Token", token + " ");
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    } else {
                        Log.v("zms", "sb");
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

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_login) {
            Log.v("login ", "success");
            sendRequestWithHttpURLConnection();
        }
    }
}
