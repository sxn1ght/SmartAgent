package com.ksu.smartagent;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ForgotUserInfoActivity extends AppCompatActivity {

    //宣告Layout上的View
    private Toolbar toolbar;
    private EditText etEmail;
    private TextView tvShowResult;
    private Button btnCopyResult, btnQuery , btnBack;

    //宣告SharedPreference
    SharedPreferences prefs_UserAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_user_info);

        init();
    }

    private void init(){
        StatusBarColorChange statusBarColorChange = new StatusBarColorChange(this);

        //指定Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //指定Layout View
        etEmail = (EditText)findViewById(R.id.etEmail);
        tvShowResult = (TextView)findViewById(R.id.tvShowResult);
        btnQuery = (Button)findViewById(R.id.btnQuery);
        btnCopyResult = (Button)findViewById(R.id.btnCopyResult);
        btnBack = (Button)findViewById(R.id.btnBack);

        //設定可見度
        tvShowResult.setVisibility(View.INVISIBLE);
        btnCopyResult.setVisibility(View.INVISIBLE);

        //指定傾聽事件
        btnQuery.setOnClickListener(ButtonOnClickListener);
        btnCopyResult.setOnClickListener(ButtonOnClickListener);
        btnBack.setOnClickListener(ButtonOnClickListener);

        //取得SharedPreferences 應用程式偏好設定
        prefs_UserAccount = getSharedPreferences("UserAccountSystem", Context.MODE_PRIVATE);
    }

    View.OnClickListener ButtonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btnCopyResult:
                    String Password = prefs_UserAccount.getString("PASSWORD" , "");
                    copyToClipboard(Password);
                    Toast.makeText(getApplication() , "已將查詢結果複製到剪貼簿" , Toast.LENGTH_SHORT).show();
                    break;

                case R.id.btnQuery:
                    boolean isPass = EditTextInputCheck();
                    if(isPass == true){
                        //獲得SharedPreferences內的資料

                        final String PrefsEmail = prefs_UserAccount.getString("EMAIL" , ""),
                                PrefsPassword = prefs_UserAccount.getString("PASSWORD" , "");

                        //如果使用者輸入的信箱與註冊時輸入的信箱相符
                        if(PrefsEmail.equals(etEmail.getText().toString())){
                            tvShowResult.setVisibility(View.VISIBLE);
                            btnCopyResult.setVisibility(View.VISIBLE);
                            tvShowResult.setText("查詢結果 ： " + PrefsPassword);

                        }else{
                            etEmail.setError("輸入的電子信箱與註冊的電子信箱不符！");
                            etEmail.requestFocus();
                            //Toast.makeText(getApplicationContext() , "輸入的電子信箱與註冊時使用的電子信箱不符 !" , Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;

                case R.id.btnBack:
                    finish();
                    break;
            }
        }
    };

    private boolean EditTextInputCheck(){
        boolean Pass = false;

        if(etEmail.getText().toString().isEmpty()){
            etEmail.setError("請輸入註冊時使用的電子信箱！");
            //Toast.makeText(this, "請輸入註冊時使用的電子信箱 !", Toast.LENGTH_SHORT).show();
        }else{
            Pass = true;
        }

        return Pass;
    }

    /** 如何將文字複製到剪貼簿(clipboard) - http://givemepass.blogspot.tw/2013/11/clipboard.html */
    private void copyToClipboard(String str){

        int sdk = android.os.Build.VERSION.SDK_INT;

        //判斷版本 , 複製到剪貼簿的方法在API 11之後 , 有更新方法 , 因此舊版的方法被標示為Deprecated
        if(sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(str);
            Log.e("version", "1 version");
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("text label",str);
            clipboard.setPrimaryClip(clip);
            Log.e("version","2 version");
        }
    }

}
