package com.ksu.smartagent;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar; //宣告Toolbar

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init(); //呼叫初始化Function
    }

    //初始化Function
    private void init(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        StatusBarColorChange statusBarColorChange = new StatusBarColorChange(this); //建立更改狀態列顏色的物件

        toolbar.setTitle(""); //為了在Toolbar上顯示置中的中文app name，這邊將原本的Toolbar Title設為空值
    }
}
