package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import base.BaseActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TeacherHomeActivity extends BaseActivity {

    public RecyclerView recyclerView;
    private CourseListAdapter courseListAdapter;
    private TextView view;
    private static final String TAG = "MainActivity";

    private ArrayList<RecordsBean> list;

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
                    courseListAdapter.notifyDataSetChanged();
                    break;
                case FAILURE:

                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_home);


        view= findViewById(R.id.image);
        list=new ArrayList<>();

        getRequest();

        recyclerView=findViewById(R.id.Course_RecyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(TeacherHomeActivity.this));

        courseListAdapter=new CourseListAdapter(TeacherHomeActivity.this,R.layout.activity_course,list);

        recyclerView.setAdapter(courseListAdapter);
    }


    private void getRequest(){
        Headers headers=new Headers.Builder()
                .add("Accept","application/json, text/plain, */*")
                .add("Content-Type","application/json")
                .add("appId", "1c53542a2bfa43b2a690ad8b8e987301")
                .add("appSecret", "03998a873144910df439eb0691b841601a601")
                .build();

        //????????????
        Request request=new Request.Builder()
                .url("http://47.107.52.7:88/member/sign/course/all")
                .headers(headers)
                .get()
                .build();
        //????????????
        OkHttpClient client = new OkHttpClient();
        //?????????????????????callback????????????
        Call call=client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "onFailure: " + e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Gson gson=new Gson();
                Type userListType = new TypeToken<courseList>(){}.getType();
                courseList courseLists=gson.fromJson(response.body().string(),userListType);
                list.addAll(courseLists.getData().getRecords());
                Log.d(TAG, "onResponse: "+list.get(0).getCollegeName());
                handler.sendEmptyMessage(SUCCESS);
            }
        });
    }
    /**
     * onNavButtonsTapped(): ???????????????????????????????????????
     *
     * @param v ??????????????????????????? v.getId() ??????????????? ID???
     */
    public void onNavButtonsTapped(View v) {
        switch (v.getId()) {
            case R.id.btnNavMessage:
                open(TeacherCourse.class);
                break; // case R.id.btnNavMessage

            case R.id.btnNavSettings:
                open(Teacher.class);
                break; // case R.id.btnNavSettings
        } // switch (v.getId())
    } // onNavButtonsTapped()

    /**
     * onKeyDown(): ???????????????????????????
     * ??????????????????????????????????????????
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            showExitDialog();
            return true;
        }
        else {
            return super.onKeyDown(keyCode, event);
        }
    }
}