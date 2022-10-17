package com.example.myapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.NetworkOnMainThreadException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;

import base.BaseActivity;
import base.MyImageView;
import entity.CourseDetail;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import tools.send_tip_msgs;

public class CourseDetailActivity  extends BaseActivity {
    private TextView collegeName;
    private TextView courseName;
    private MyImageView coursePhoto;
    private TextView createTime;
    private TextView endTime;
    private ImageView back;
//后面还要定义一个是否选课的变量，这个变量决定那个按钮是选课还是退课。

    private TextView introduce;
    private TextView realName;
    private TextView startTime;
    private TextView userName;
    private Button button;
    private ImageView imageView;
    private CourseDetail courseDetail;

    static final int SUCCESS = 0;
    static final int FAILURE = 1;
    Handler handler = new Handler(Looper.getMainLooper())
    {
        @Override
        public void handleMessage(@NonNull Message msg)
        {
            switch (msg.what)
            {
                case SUCCESS:
                    setEvent();
                    break;
                case FAILURE:

                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getCourseDetail();
        setContentView(R.layout.course_detail);
        initView();
        getCourseDetail();
//        setEvent();
    }
    private void initView(){
        collegeName = findViewById(R.id.collegeName);//大学名字
        courseName = findViewById(R.id.courseName);//课程名字

        createTime = findViewById(R.id.createTime);//创建时间
        endTime = findViewById(R.id.endTime);//结束时间
        introduce=findViewById(R.id.introduce);//介绍
        realName=findViewById(R.id.realName);//真实名字
        startTime=findViewById(R.id.startTime);//开始时间

        button=findViewById(R.id.myButton);//加入课程按钮
        imageView=findViewById(R.id.attendance);//那个铃铛，用来跳转到签到列表
        back=findViewById(R.id.back);
//        Intent intent = getIntent();
//        tools.msgs_class_to_class.get_msgs(intent,"id",id);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CourseDetailActivity.this,HomeActivity.class));
            }
        });
    }
    private void setEvent(){
           // getCourseDetail();
            Log.d("大学名字 ",courseDetail.getCollegeName());


        collegeName.setText(courseDetail.getCollegeName());
        courseName.setText(courseDetail.getCourseName());
//        coursePhoto.setImageURL(courseDetail.getCoursePhoto());
        createTime.setText(courseDetail.getCreateTime().toString());
        endTime.setText(courseDetail.getEndTime().toString());
        introduce.setText(courseDetail.getIntroduce());
        realName.setText(courseDetail.getRealName());
        startTime.setText(courseDetail.getStartTime().toString());

    }
    private void getCourseDetail(){

            new Thread(() -> {
                SharedPreferences sp2 = getSharedPreferences("user.xml",MODE_PRIVATE);
                String userId = sp2.getString("userId","");


                String courseId=getIntent().getStringExtra("courseId");
                if(userId==null){
                    Log.d("id    ","id是空的");
                }else {
                    Log.d("用户id为   ",userId);
                }
                if(courseId==null){
                    Log.d("课程     ","课程id是空的");
                }else {
                    Log.d("课程    ",courseId);
                }
                // url路径
                String url = "http://47.107.52.7:88/member/sign/course/detail?courseId="+courseId+"&userId="+userId;

                // 请求头
                Headers headers = new Headers.Builder()
                        .add("Accept", "application/json, text/plain, */*")
                        .add("appId", "1c53542a2bfa43b2a690ad8b8e987301")
                        .add("appSecret", "03998a873144910df439eb0691b841601a601")
                        .build();

                //请求组合创建
                Request request = new Request.Builder()
                        .url(url)
                        // 将请求头加至请求中
                        .headers(headers)
                        .get()
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
                Type jsonType = new TypeToken<ResponseBody<CourseDetail>>(){}.getType();
                // 获取响应体的json串
                String body = response.body().string();

                Log.d("info", body);
                // 解析json串到自己封装的状态
                Gson gson=new Gson();
                ResponseBody<CourseDetail> data=gson.fromJson(body,jsonType);
            courseDetail=data.getData();
            Log.d("课程详情   ",courseDetail.getCollegeName());
            handler.sendEmptyMessage(SUCCESS);
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
