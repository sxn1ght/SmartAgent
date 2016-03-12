package com.ksu.smartagent;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    private StatusBarColorChange statusBarColorChange; //宣告更改狀態列顏色的物件

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        init(); //呼叫初始化Function
    }

    //初始化Function
    private void init(){
        statusBarColorChange = new StatusBarColorChange(this);

        toolbar.setTitle("");
    }
}
