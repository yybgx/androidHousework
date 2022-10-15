package com.example.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class EditImformation extends AppCompatActivity {
    private ImageView avatar;
    private ImageView back;
    private EditText username;
    private EditText collegename;
    private EditText realname;
    private EditText student_Number;
    private EditText gender;
    private EditText inschooltime;
    private EditText email;
    private EditText phone;
    private Button button;

    private final Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_information);
        inintView();
        initEvent();
    }

    private void initEvent() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //头像
                String Str_username = username.getText().toString();
                String Str_school = collegename.getText().toString();
                String Str_real_name = realname.getText().toString();
                String Str_student_Number = student_Number.getText().toString();
                String Str_gender = gender.getText().toString();
                String Str_inschooltimete = inschooltime.getText().toString();
                String Str_email = email.getText().toString();
                String Str_phone = phone.getText().toString();
                int int_inschooltimete =  Integer.parseInt(Str_inschooltimete);
                boolean bool_gender = true;
                if(Str_gender.equals("女")){
                    bool_gender = false;
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditImformation.this,PersonActivity.class));
            }
        });
    }

    private void inintView() {
        avatar = findViewById(R.id.Avatar);
        username = findViewById(R.id.userName);
        collegename = findViewById(R.id.collegeName);
        realname = findViewById(R.id.realName);
        student_Number = findViewById(R.id.student_Number);
        gender = findViewById(R.id.cv_user_head);
        inschooltime = findViewById(R.id.inSchoolTime);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.Phone);
        button = findViewById(R.id.edit_Information_button);
        back = findViewById(R.id.back);
    }
    private void editImformation(){
        new Thread(() -> {

            // url路径
            String url = "http://47.107.52.7:88/member/sign/user/update";

            // 请求头
            Headers headers = new Headers.Builder()
                    .add("Accept", "application/json, text/plain, */*")
                    .add("appId", "cfa7b08b01fd4c0b90f40f94798cfc2d")
                    .add("appSecret", "17942d0b56a59da3e4d2abbb11b71e98f8b12")
                    .add("Content-Type", "application/json")
                    .build();

            // 请求体
            // PS.用户也可以选择自定义一个实体类，然后使用类似fastjson的工具获取json串
            Map<String, Object> bodyMap = new HashMap<>();
            bodyMap.put("collegeName", "string");
            bodyMap.put("realName", "string");
            bodyMap.put("gender", true);
            bodyMap.put("phone", "string");
            bodyMap.put("avatar", "string");
            bodyMap.put("id", 0);
            bodyMap.put("idNumber", 0);
            bodyMap.put("userName", "string");
            bodyMap.put("email", "string");
            bodyMap.put("inSchoolTime", 0);
            // 将Map转换为字符串类型加入请求体中
            String body = gson.toJson(bodyMap);

            MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");

            //请求组合创建
            Request request = new Request.Builder()
                    .url(url)
                    // 将请求头加至请求中
                    .headers(headers)
                    .post(RequestBody.create(MEDIA_TYPE_JSON, body))
                    .build();
            try {
                OkHttpClient client = new OkHttpClient();
                //发起请求，传入callback进行回调
                client.newCall(request).enqueue(callback);
            }catch (NetworkOnMainThreadException ex){
                ex.printStackTrace();
            }
        }).start();
    }

    /**
     * 回调
     */
    private final Callback callback = new Callback() {
        @Override
        public void onFailure(@NonNull Call call, IOException e) {
            //TODO 请求失败处理
            e.printStackTrace();
        }
        @Override
        public void onResponse(@NonNull Call call, Response response) throws IOException {
            //TODO 请求成功处理
            Type jsonType = new TypeToken<ResponseBody<Object>>(){}.getType();
            // 获取响应体的json串
            String body = response.body().string();
            Log.d("info", body);
            // 解析json串到自己封装的状态
            ResponseBody<Object> dataResponseBody = gson.fromJson(body,jsonType);
            Log.d("info", dataResponseBody.toString());
        }
    };

    /**
     * http响应体的封装协议
     * @param <T> 泛型
     */
    public static class ResponseBody <T> {

        /**
         * 业务响应码
         */
        private int code;
        /**
         * 响应提示信息
         */
        private String msg;
        /**
         * 响应数据
         */
        private T data;

        public ResponseBody(){}

        public int getCode() {
            return code;
        }
        public String getMsg() {
            return msg;
        }
        public T getData() {
            return data;
        }

        @NonNull
        @Override
        public String toString() {
            return "ResponseBody{" +
                    "code=" + code +
                    ", msg='" + msg + '\'' +
                    ", data=" + data +
                    '}';
        }
    }
}
