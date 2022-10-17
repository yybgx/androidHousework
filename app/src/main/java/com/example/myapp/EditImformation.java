package com.example.myapp;

import android.content.Context;
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

import base.BaseActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class EditImformation extends BaseActivity {
    private ImageView avatar;
    private ImageView back;
    private int id;
    private EditText username;
    private EditText collegename;
    private EditText realname;
    private EditText idNumber;
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
        initInformation();
        initEvent();
    }

    private void initEvent() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int number=9;//参数数量
                //头像
                String[] information = new String[number];
                information[0] = username.getText().toString();
                information[1] = collegename.getText().toString();
                information[2] = realname.getText().toString();
                information[3] = idNumber.getText().toString();
                information[4] = gender.getText().toString();
                information[5] = inschooltime.getText().toString();
                information[6] = email.getText().toString();
                information[7] = phone.getText().toString();
                information[8] = tools.info_deal.getUserInfo(EditImformation.this,"user.xml","Id");
                editInformation(information);
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
        idNumber = findViewById(R.id.id_Number);
        gender = findViewById(R.id.gender);
        inschooltime = findViewById(R.id.inSchoolTime);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.Phone);
        button = findViewById(R.id.edit_Information_button);
        back = findViewById(R.id.back);
    }
    private void initInformation(){
        String  str_avater = tools.info_deal.getUserInfo(EditImformation.this,"user.xml","UserName");
        String str_id = tools.info_deal.getUserInfo(EditImformation.this,"user.xml","Id");;
        String str_username = tools.info_deal.getUserInfo(EditImformation.this,"user.xml","UserName");
        String str_collegename = tools.info_deal.getUserInfo(EditImformation.this,"user.xml","CollegeName");
        String str_realname = tools.info_deal.getUserInfo(EditImformation.this,"user.xml","RealName");
        String str_idNumber = tools.info_deal.getUserInfo(EditImformation.this,"user.xml","IdNumber");
        String str_gender = tools.info_deal.getUserInfo(EditImformation.this,"user.xml","Gender");
        String str_inschooltime = tools.info_deal.getUserInfo(EditImformation.this, "user.xml","InSchoolTime");
        String str_email = tools.info_deal.getUserInfo(EditImformation.this,"user.xml","Email");
        String str_phone = tools.info_deal.getUserInfo(EditImformation.this,"user.xml","Phone");

        if(tools.string_deal.isNotEmpty(str_username))
        username.setText(tools.info_deal.getUserInfo(EditImformation.this,"user.xml","UserName"));
        if(tools.string_deal.isNotEmpty(str_collegename))
        collegename.setText(tools.info_deal.getUserInfo(EditImformation.this,"user.xml","CollegeName"));
        if(tools.string_deal.isNotEmpty(str_realname))
        realname.setText(tools.info_deal.getUserInfo(EditImformation.this,"user.xml","RealName"));
        if(tools.string_deal.isNotEmpty(str_idNumber))
        idNumber.setText(tools.info_deal.getUserInfo(EditImformation.this,"user.xml","IdNumber"));
        if(tools.string_deal.isNotEmpty(str_gender))
        if("flase".equals(tools.info_deal.getUserInfo(EditImformation.this,"user.xml","Gender"))){
            gender.setText("女");
        }else{
            gender.setText("男");
        }
        if(tools.string_deal.isNotEmpty(str_inschooltime))
        inschooltime.setText(tools.info_deal.getUserInfo(EditImformation.this, "user.xml","InSchoolTime"));
        if(tools.string_deal.isNotEmpty(str_email))
        email.setText(tools.info_deal.getUserInfo(EditImformation.this,"user.xml","Email"));
        if(tools.string_deal.isNotEmpty(str_phone))
        phone.setText(tools.info_deal.getUserInfo(EditImformation.this,"user.xml","Phone"));
    }
    private void editInformation(String[] information){
        new Thread(() -> {

            // url路径
            String url = "http://47.107.52.7:88/member/sign/user/update";

            // 请求头
            Headers headers = new Headers.Builder()
                    .add("Accept", "application/json, text/plain, */*")
                    .add("appId", "1c53542a2bfa43b2a690ad8b8e987301")
                    .add("appSecret", "03998a873144910df439eb0691b841601a601")
                    .add("Content-Type", "application/json")
                    .build();

            // 请求体
            // PS.用户也可以选择自定义一个实体类，然后使用类似fastjson的工具获取json串
            Map<String, Object> bodyMap = new HashMap<>();
            bodyMap.put("collegeName", information[0]);
            bodyMap.put("realName", information[2]);
            boolean bool_gender = true;
            if(information[4].equals("女")){
                bool_gender = false;
            }
            bodyMap.put("gender", bool_gender);
            bodyMap.put("phone", information[7]);
            bodyMap.put("avatar", "https://guet-lab.oss-cn-hangzhou.aliyuncs.com/api/2022/10/13/e465c552-720a-400e-8056-30f158b86914.jpg");
            bodyMap.put("id", Integer.parseInt(information[8]));
            bodyMap.put("idNumber", Integer.parseInt(information[3]));
            bodyMap.put("userName", information[0]);
            bodyMap.put("email", information[6]);
            bodyMap.put("inSchoolTime", Integer.parseInt(information[5]));
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
            if(dataResponseBody.getCode()==200){
                tools.send_tip_msgs.showToast(EditImformation.this,"修改成功");
            }
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
