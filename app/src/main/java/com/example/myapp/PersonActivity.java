package com.example.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import base.BaseActivity;


public class PersonActivity extends BaseActivity {
    private Button editInfotmation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        initView();
        initEvent();
    } // onCreate()

    private void initEvent() {
        editInfotmation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PersonActivity.this,EditImformation.class));
            }
        });
    }

    public void initView(){
        editInfotmation = findViewById(R.id.btn_editInformation);
    }
    // 请注意本方法内容的变化
    public void onNavButtonsTapped(View v) {
        switch (v.getId()) {
            case R.id.btnNavHome:
                open(HomeActivity.class);
                break; // case R.id.btnNavHome

            case R.id.btnNavMessage:
                open(MycourseActivity.class);
                break; // case R.id.btnNavMessage
        } // switch (v.getId())
    } // onNavButtonsTapped()

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            showExitDialog();
            return true;
        } // if (keyCode == KeyEvent.KEYCODE_BACK)
        else {
            return super.onKeyDown(keyCode, event);
        } // else
    } // onKeyDown()
} // SettingsActivity Class

// E.O.F