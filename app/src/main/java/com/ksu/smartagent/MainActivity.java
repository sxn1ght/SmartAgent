package com.ksu.smartagent;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    //宣告Layout上的View
    private Toolbar toolbar;
    private EditText etEmail , etPassword;
    private TextView tvchkAutoLogin  , tvForget;
    private CheckBox chkAutoLogin;
    private Button btnLogin , btnRegister;

    //宣告SharedPreference
    private SharedPreferences prefs_UserAccount;

    //"按兩下返回鍵結束程式" 使用的變數
    private long mExitTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init(); //呼叫初始化Function
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    //初始化Function
    private void init(){
        //指定Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //為了在Toolbar上顯示置中的中文app name，這邊將原本的Toolbar Title設為空值
        toolbar.setTitle("");

        //指定Layout View
        etEmail = (EditText)findViewById(R.id.etEmail);
        etPassword = (EditText)findViewById(R.id.etPassword);
        chkAutoLogin = (CheckBox)findViewById(R.id.chkAutoLogin);
        tvchkAutoLogin = (TextView)findViewById(R.id.tvchkAutoLogin);
        btnLogin = (Button)findViewById(R.id.btnLogin);
        btnRegister = (Button)findViewById(R.id.btnRegister);
        tvForget = (TextView)findViewById(R.id.tvForget);

        //設定底線
        tvForget.setText(Html.fromHtml("<u>" + "忘記帳號或密碼?" + "</u>"));

        //建立更改狀態列顏色的物件
        StatusBarColorChange statusBarColorChange = new StatusBarColorChange(this);

        //指定TextView傾聽事件
        tvchkAutoLogin.setOnClickListener(TextViewOnClickListener);
        tvForget.setOnClickListener(TextViewOnClickListener);

        //指定Button傾聽事件
        btnLogin.setOnClickListener(ButtonOnClickListener);
        btnRegister.setOnClickListener(ButtonOnClickListener);

        //取得SharedPreferences 應用程式偏好設定
        prefs_UserAccount = getSharedPreferences("UserAccountSystem", Context.MODE_PRIVATE);

        Log.d("Login","信箱:" + prefs_UserAccount.getString("EMAIL", "")  + "/"
                + "密碼:" + prefs_UserAccount.getString("PASSWORD", "") + "/"
                + "是否註冊:" + prefs_UserAccount.getString("REGISTERED", "") + "/"
                + "是否勾選自動登入:" + prefs_UserAccount.getString("AutoLoginIsChecked", ""));

        //如果已註冊帳號密碼 , 則將註冊的按鈕設為隱藏
        /*if(prefs_UserAccount.getString("REGISTERED" , "").equals("Y")){
            btnRegister.setVisibility(View.GONE);
        }*/

        //若AutoLoginIsChecked欄位為"Y" , 則表示使用者上次登入帳號時有勾選CheckBox
        //因此自動填入各個EditText , 且設定Checkbox為勾選狀態
        if(prefs_UserAccount.getString("AutoLoginIsChecked" , "").equals("Y")){
            etEmail.setText(prefs_UserAccount.getString("EMAIL", ""));
            etPassword.setText(prefs_UserAccount.getString("PASSWORD",""));
            chkAutoLogin.setChecked(true);

           /* Intent i = new Intent(this , CalendarActivity.class);
            startActivity(i);
            MainActivity.this.finish();*/
        }
    }

    //TextView傾聽事件
    private View.OnClickListener TextViewOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.tvchkAutoLogin:
                    if(chkAutoLogin.isChecked()){
                        chkAutoLogin.setChecked(false);
                    }else{
                        chkAutoLogin.setChecked(true);
                    }
                    break;

                case R.id.tvForget:
                    if(!(prefs_UserAccount.getString("REGISTERED" ,"").equals("Y"))){
                        Toast.makeText(getApplicationContext(), "您還未進行帳號註冊 ! ", Toast.LENGTH_SHORT).show();
                        return;
                    }else{
                        Intent intent = new Intent(getApplicationContext() , ForgotUserInfoActivity.class);
                        startActivity(intent);
                    }
                    break;
            }

        }
    };

    //Button傾聽事件
    private View.OnClickListener ButtonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btnLogin:
                    boolean isPass = checkEditTextInput();

                    if(isPass == true){
                        //獲得SharedPreferences內的資料
                        String PrefsEmail = prefs_UserAccount.getString("EMAIL" , ""),
                                PrefsPassword = prefs_UserAccount.getString("PASSWORD" , "");

                        //獲得EditText內輸入的資料
                        String EditTextEmail = etEmail.getText().toString(),
                                EditTextPassword = etPassword.getText().toString();

                        //若使用者輸入的資料與SharedPreferences內的資料不符合 , 則跳出Toast提醒使用者並return
                        if(!(EditTextEmail.equals(PrefsEmail))){
                            etEmail.setError("電子信箱輸入錯誤 , 請重新確認 !");
                            //Toast.makeText(getApplicationContext(), "電子信箱輸入錯誤 , 請重新確認" , Toast.LENGTH_SHORT).show();
                            etEmail.requestFocus();
                            return;
                        }else if(!(EditTextPassword.equals(PrefsPassword))){
                            etPassword.setError("密碼輸入錯誤 , 請重新確認 !");
                            //Toast.makeText(getApplicationContext(), "密碼輸入錯誤 , 請重新確認" , Toast.LENGTH_SHORT).show();
                            etPassword.requestFocus();
                            return;
                        }

                        //若自動登入的checkbox有勾選 , 則將AutoLoginIsChecked設為Y , 下次進入MainActivity時會直接進入行事曆畫面
                        SharedPreferences.Editor editor = prefs_UserAccount.edit();
                        if(chkAutoLogin.isChecked()){
                            editor.putString("AutoLoginIsChecked", "Y");
                            editor.commit(); //寫入
                        }else{
                            editor.putString("AutoLoginIsChecked", "N");
                            editor.commit(); //寫入
                        }

                        //提示登入成功訊息
                        Toast.makeText(getApplication() , "登入成功 !" ,Toast.LENGTH_SHORT).show();

                        //Intent intent = new Intent(getApplicationContext() , CalendarActivity.class);
                        //startActivity(intent);
                        //finish();
                    }

                    break;

                case R.id.btnRegister:
                    Intent intent = new Intent(getApplicationContext() , RegisterActivity.class);
                    startActivity(intent);

                    break;

            }
        }
    };

    //判斷使用者是否有在EditText內輸入資料
    private boolean checkEditTextInput(){

        if(!(prefs_UserAccount.getString("REGISTERED" ,"").equals("Y"))){
            Toast.makeText(getApplicationContext(), "您還未進行帳號註冊 ! " , Toast.LENGTH_SHORT).show();
            return false;

        }else if(etEmail.getText().toString().isEmpty()){
            etEmail.setError("請輸入電子信箱 !");
            return false;

        }else if(etPassword.getText().toString().isEmpty()){
            etPassword.setError("請輸入密碼 !");
            return false;

        }else{
            return true;
        }
    }

    //按兩次返回鍵結束app的method
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Toast.makeText(this, "再按一次返回鍵退出程式", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();

            }else{
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
