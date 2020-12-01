package com.example.first;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TokenInterceptor implements Interceptor {
    private static final String TAG = "TokenInterceptor";
    private Context context;
    private SharedPreferences preferences;

    public TokenInterceptor(Context context) {
        this.context = context;
        this.preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);
        Log.d(TAG, "response.code=: " + response.code());

        //根据和服务端的约定判断token过期
        if(isTokenExpired(response)) {
            Log.d(TAG, "自动刷新Token,然后重新请求数据");
            //同步请求方式，获取最新的Token
            String newToken = getNewToken();
            Request newRequest = chain.request()
                    .newBuilder()
                    .addHeader("authorization", "Bearer" +  " " + newToken)
                    .build();
            return chain.proceed(newRequest);
        }
        return response;
    }

    /**
     * 根据Response，判断Token是否失效
     *
     * @param response
     * @return
     */
    private boolean isTokenExpired(Response response) {
        if (response.code() == 401) {
            return true;
        }
        return false;
    }

    private String getNewToken() {
        sendRequestWithOKHttpURLConnection();
        return "";
    }

    private void sendRequestWithOKHttpURLConnection() {
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("username", preferences.getString("USERNAME", ""))
                .add("password", preferences.getString("PASSWORD", ""))
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
                        Log.v("how say", "nice!");
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
                        Log.e("tokenlpp", token + " ");
                    } else {
                        Log.v("wuyv", "sb");
                    }
                }
                else {
                    Log.e("zms","failed");
                }
            }
        });
    }
}
