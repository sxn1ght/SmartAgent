package com.ksu.smartagent;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.ksu.database.DAOKeywordDB;

public class RegisterActivity extends AppCompatActivity {

    //宣告資料庫操作物件
    private DAOKeywordDB daoKeywordDB;

    //宣告Layout上的View
    private Toolbar toolbar;
    private Spinner spnEmail;
    private EditText etPassword;
    private Button btnConfirm , btnCancel;

    private ArrayAdapter<String> spnEmailAdapter;

    //宣告SharedPreference 獲取偏好設定
    SharedPreferences prefs_UserAccount;

    private String SpinnerSelectedItem = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        init();
    }

    private void init(){
        StatusBarColorChange statusBarColorChange = new StatusBarColorChange(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        spnEmail = (Spinner)findViewById(R.id.spnEmail);
        etPassword = (EditText)findViewById(R.id.etPassword);
        btnConfirm = (Button)findViewById(R.id.btnConfirm);
        btnCancel = (Button)findViewById(R.id.btnCancel);


        //指定傾聽事件
        btnConfirm.setOnClickListener(ButtonOnClickListener);
        btnCancel.setOnClickListener(ButtonOnClickListener);
        spnEmail.setOnItemSelectedListener(SpinnerListener);

        //取得SharedPreferences 應用程式偏好設定
        prefs_UserAccount = getSharedPreferences("UserAccountSystem", Context.MODE_PRIVATE);

        //建立關鍵字資料庫操作物件
        daoKeywordDB = new DAOKeywordDB(this);

        //取得手機內帳號資料
        AccountManager accountManager = AccountManager.get(this);

        //取得指定type的帳號 , 這裡設為Google的帳號資料
        Account[] accounts = accountManager.getAccountsByType("com.google");

        //宣告存放所有帳號的陣列 , 也是Spinner顯示的資料 , 長度+1是為了在Spinner內的第一列Item中顯示"電子信箱"這段說明文字
        String[] accountNames = new String[accounts.length+1];

        //陣列中第一列資料顯示為"電子信箱"
        accountNames[0] = getResources().getString(R.string.Spinner_Information);

        //宣告紀錄迴圈執行次數的變數
        int i = 1;

        //跑迴圈將帳號資料讀出來後放到accountNames陣列中
        for(Account account : accounts){
            String name = account.name;
            accountNames[i++] = name;
        }

        //設定Adapter
        spnEmailAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, accountNames);

        //指定Adapter
        spnEmail.setAdapter(spnEmailAdapter);

        //指定傾聽事件
        spnEmail.setOnItemSelectedListener(SpinnerListener);
    }

    //Spinner的傾聽事件
    private AdapterView.OnItemSelectedListener SpinnerListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            //選取資料後 , 會將選取的內容存進SpinnerSelectedItem字串變數中
            SpinnerSelectedItem = spnEmailAdapter.getItem(position).toString();
            //Log.d("SpinnerSelectedItem" , SpinnerSelectedItem);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    //Button傾聽事件
    private View.OnClickListener ButtonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btnConfirm:
                    //檢查使用者輸入的資料
                    boolean isPass = checkEditTextInput();

                    if(isPass == true){
                        //將json內讀出的資料陣列寫入db中
                        daoKeywordDB.WriteKeywordToDB(getApplicationContext());

                        //關閉資料庫
                        daoKeywordDB.close();

                        //將帳號密碼Email等資料寫入SharedPreferences
                        SharedPreferences.Editor editor = prefs_UserAccount.edit();
                        editor.putString("PASSWORD",etPassword.getText().toString());
                        editor.putString("EMAIL", SpinnerSelectedItem);
                        editor.putString("REGISTERED", "Y"); //傳送已註冊訊息
                        editor.commit(); //寫入

                        //顯示註冊完成訊息
                        Toast.makeText(getApplicationContext(), "註冊成功 !", Toast.LENGTH_LONG).show();

                        finish();
                    }
                    break;

                case R.id.btnCancel:
                    finish();
                    break;
            }
        }
    };

    //檢查輸入資料是否正確
    private boolean checkEditTextInput(){

        if(etPassword.getText().toString().isEmpty()){
            etPassword.setError("請輸入密碼 !");
            etPassword.requestFocus();
            return false;

        }else if(etPassword.getText().toString().length()<5 || etPassword.getText().toString().length()>10){
            etPassword.setError("密碼欄位請輸入5至10個英文或數字 !");
            etPassword.requestFocus();
            return false;

        }else if(SpinnerSelectedItem.equals(getResources().getString(R.string.Spinner_Information))){
            Toast.makeText(getApplicationContext() , "請選擇電子信箱 !" , Toast.LENGTH_SHORT).show();
            return false;

        }else{
            return true;
        }
    }

}
