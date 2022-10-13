package com.example.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;

import base.BaseActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import tools.send_tip_msgs;


public class Login extends BaseActivity {
    private ImageView logo;
    private EditText id;
    private EditText password;
    private Button login;
    private TextView register;
    private TextView forget_password;
    private final Gson gson = new Gson();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        intView();
        intEvent();
    }

    private void intView(){
        id = findViewById(R.id.et_Login_Id);
        password = findViewById(R.id.et_Login_Psw);
        login = findViewById(R.id.login);
        register = findViewById(R.id.register);
        forget_password = findViewById(R.id.forget_password);
        Intent intent = getIntent();
        tools.msgs_class_to_class.get_msgs(intent,"id",id);
    }

    private void intEvent(){
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str_id = id.getText().toString();
                String str_password = password.getText().toString();
                if(str_id.equals("")){
                    Toast.makeText(getApplication(),"账号不能为空",Toast.LENGTH_LONG).show();
                }else if(str_password.equals("")){
                    Toast.makeText(getApplication(),"密码不能为空",Toast.LENGTH_LONG).show();
                }else {
                    login();
                }
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this,Register.class));
                Intent intent = tools.msgs_class_to_class.sned_msgs(Login.this,Register.class,"id",id.getText().toString());
                startActivity(intent);
            }
        });
    }
    private void login(){
        new Thread(() -> {

            // url路径
            String url = "http://47.107.52.7:88/member/sign/user/login?password="+password.getText().toString()+"&username="+id.getText().toString();
            Log.d("url",url);
            // 请求头
            Headers headers = new Headers.Builder()
                    .add("Accept","application/json, text/plain, */*")
                    .add("Content-Type","application/json")
                    .add("appId", "1c53542a2bfa43b2a690ad8b8e987301")
                    .add("appSecret", "03998a873144910df439eb0691b841601a601")
                    .build();


            MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");

            //请求组合创建
            Request request = new Request.Builder()
                    .url(url)
                    // 将请求头加至请求中
                    .headers(headers)
                    .post(RequestBody.create(MEDIA_TYPE_JSON, ""))
                    .build();
            try {
                OkHttpClient client = new OkHttpClient();
                //发起请求，传入callback进行回调
                client.newCall(request).enqueue((Callback) callback);
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
            Type jsonType = new TypeToken<ResponseBody>(){}.getType();
            // 获取响应体的json串
            String body = response.body().string();
//            Log.d("info", body);
            // 解析json串到自己封装的状态
            ResponseBody dataResponseBody = gson.fromJson(body,jsonType);
            Log.d("info", dataResponseBody.toString());
            send_tip_msgs.showToast(Login.this,dataResponseBody.getMsg());
            if(dataResponseBody.getData().getRoleId()==0 &&dataResponseBody.getCode()==200){
                startActivity(new Intent(Login.this,HomeActivity.class));
            }
            else if(dataResponseBody.getData().getRoleId()==1&&dataResponseBody.getCode()==200){
                startActivity(new Intent(Login.this, TeacherHomeActivity.class));
            }
        }
    };

    /**
     * http响应体的封装协议
     */
    public static class ResponseBody {

        private int code;

        private String msg;

        private Data data;
        public ResponseBody(){}

        public int getCode() {
            return code;
        }
        public String getMsg() {
            return msg;
        }
        public Data getData() {
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
    public class Data {
        private String id;
        private String appKey;
        private String userName;
        private int roleId;
        private String realName;
        private String idNumber;
        private String collegeName;
        private String gender;
        private String phone;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getAppKey() {
            return appKey;
        }

        public void setAppKey(String appKey) {
            this.appKey = appKey;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public int getRoleId() {
            return roleId;
        }

        public void setRoleId(int roleId) {
            this.roleId = roleId;
        }

        public String getRealName() {
            return realName;
        }

        public void setRealName(String realName) {
            this.realName = realName;
        }

        public String getIdNumber() {
            return idNumber;
        }

        public void setIdNumber(String idNumber) {
            this.idNumber = idNumber;
        }

        public String getCollegeName() {
            return collegeName;
        }

        public void setCollegeName(String collegeName) {
            this.collegeName = collegeName;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getInSchoolTime() {
            return inSchoolTime;
        }

        public void setInSchoolTime(String inSchoolTime) {
            this.inSchoolTime = inSchoolTime;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getLastUpdateTime() {
            return lastUpdateTime;
        }

        public void setLastUpdateTime(String lastUpdateTime) {
            this.lastUpdateTime = lastUpdateTime;
        }

        private String email;
        private String avatar;
        private String inSchoolTime;
        private String createTime;
        private String lastUpdateTime;


    }
}