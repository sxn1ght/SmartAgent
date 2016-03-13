package com.ksu.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.util.EncodingUtils;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by SxNight on 2015/5/10.
 */
public class DAOKeywordDB {

    //宣告Json檔案名稱、讀取Json檔案所使用的編碼
    private static final String FileName = "keyword.json";
    private static final String ENCODING = "UTF-8";

    //宣告資料表名稱
    private static final String PLACE_TABLE_NAME = "PlaceKeywordDB";
    private static final String TIME_TABLE_NAME = "TimeKeywordDB";
    private static final String CUSTOM_TABLE_NAME = "CustomKeywordDB";

    //宣告資料庫物件
    private SQLiteDatabase db;

    // 建構子
    public DAOKeywordDB(Context context) {
        //開啟資料庫
        db = KeywordDBHelper.getDatabase(context);

    }

    // 關閉資料庫
    public void close() {
        db.close();
    }


    //參數 1=地點 , 2=時間 , 3=自訂
    public List<Map<String,Object>> getTableData(int TargetTable){

        //宣告要回傳的集合物件
        List<Map<String,Object>> Result = new ArrayList<>();

        switch (TargetTable){
            //讀取"地點"資料表
            case 1:
                //建立Cursor , 查詢PlaceDB
                Cursor c = db.query(PLACE_TABLE_NAME , null , null , null , null , null , null);
                //移至第一筆
                c.moveToFirst();
                for(int i = 0 ; i<c.getCount(); i++){
                    HashMap<String,Object> item = new HashMap<String,Object>();
                    //put所有item
                    item.put("ID", c.getString(0));
                    item.put("KEYWORD", c.getString(1));
                    item.put("PRIORITY", c.getString(2));
                    //Log.d("PLACElistitem" , c.getString(0) + "/" + c.getString(1) + "/" + c.getString(2));
                    Result.add(item);
                    c.moveToNext(); //移至下一筆
                }
                //執行完將Cursor關閉
                c.close();
                c = null;
                break;

            //讀取"時間"資料表
            case 2:
                Cursor c1 = db.query(TIME_TABLE_NAME , null , null , null , null , null , null);
                c1.moveToFirst();
                for(int i = 0 ; i<c1.getCount(); i++){
                    HashMap<String,Object> item = new HashMap<String,Object>();
                    item.put("ID", c1.getString(0));
                    item.put("KEYWORD", c1.getString(1));
                    item.put("TIMECATEGORY", c1.getString(2));
                    //Log.d("TIMElistitem" , c1.getString(0) + "/" + c1.getString(1) + "/" + c1.getString(2));
                    Result.add(item);
                    c1.moveToNext();
                }
                c1.close();
                c1 = null;
                break;

            //讀取"自訂"資料表
            case 3:
                Cursor c2 = db.query(CUSTOM_TABLE_NAME , null , null , null , null , null , null);
                if(!c2.moveToFirst()){
                    HashMap<String,Object> item = new HashMap<String,Object>();
                    item.put("ID" , "Nothing");
                    item.put("KEYWORD", "資料表內沒有資料...");
                    item.put("CUSTOMCATEGORY", "Nothing.");
                    Result.add(item);
                }else{
                    c2.moveToFirst();
                    for(int i = 0 ; i<c2.getCount() ; i++){
                        HashMap<String,Object> item = new HashMap<String,Object>();
                        item.put("ID", c2.getString(0));
                        item.put("KEYWORD", c2.getString(1));
                        item.put("CUSTOMCATEGORY", c2.getString(2));
                        Result.add(item);
                        c2.moveToNext();
                    }
                    c2.close();
                    c2 = null;
                }
                break;
        }
        return Result;
    }

    //註冊時用到 , 將json內讀出的資料陣列寫入Database
    public void WriteKeywordToDB(Context context){
        List<Map<String,Object>> getJson = this.JsonPaser(context);

        ContentValues cv = new ContentValues();
        ContentValues cv1 = new ContentValues();
        long id ;

        //讀出Json解析的資料後傳到各個陣列存放
        for(int i = 0 ; i<getJson.size() ; i++){
            if(getJson.get(i).get("PlaceKeyword") != null){
                cv.put("Keyword", getJson.get(i).get("PlaceKeyword").toString());
                cv.put("Priority", "1");
                id = db.insert(PLACE_TABLE_NAME, null, cv);

                Log.d("地點資料庫寫入成功", getJson.get(i).get("PlaceKeyword").toString());

            }else if(getJson.get(i).get("TimeKeyword") != null){
                cv1.put("Keyword", getJson.get(i).get("TimeKeyword").toString());
                cv1.put("TimeCategory", getJson.get(i).get("TimeCategory").toString());
                id = db.insert(TIME_TABLE_NAME, null , cv1);

                Log.d("時間資料庫寫入成功" , "關鍵字:" + getJson.get(i).get("TimeKeyword").toString() + ", 類別:" + getJson.get(i).get("TimeCategory").toString());
            }
        }

    }

    //註冊時用到 , 解析Json檔案 , 將解析出來的資料分別寫入各個陣列
    public List<Map<String,Object>> JsonPaser(Context context){

        //宣告要回傳的結果
        List<Map<String, Object>> Result = new ArrayList<Map<String, Object>>();

        //建立Gson物件
        Gson gson = new Gson();

        //Json為陣列包物件 , 直接用arraylist把整個物件包起來
        Type listType = new TypeToken<ArrayList<KeywordJsonBean>>() {}.getType();
        ArrayList<KeywordJsonBean> jsonArr = gson.fromJson(getFromAssets(FileName, context), listType);

        //跑迴圈將Json內的資料讀出來後放入Result內
        for(KeywordJsonBean obj : jsonArr){
            //若類別為地點 ,  則將從json內獲得的資料寫入PlaceKeywordArray陣列中
            if(obj.getCategoryStr().toString().equals("PLACE")) {
                HashMap<String,Object> item = new HashMap<String,Object>();
                item.put("PlaceKeyword" , obj.getKeywordStr());
                Result.add(item);

                //類別為時間
            }else if(obj.getCategoryStr().toString().equals("TIME")) {
                HashMap<String,Object> item = new HashMap<String,Object>();
                item.put("TimeKeyword" , obj.getKeywordStr());
                item.put("TimeCategory" , obj.getTimeCategory());
                Result.add(item);

            }
        }

        return Result;
    }

    // 從Assets資料夾內讀取檔案的function
    private String getFromAssets(String fileName , Context context){
        //宣告要回傳的結果變數
        String result = "";

        try{
            InputStream in = context.getResources().getAssets().open(fileName);
            // 獲取檔案的字節數目
            int lenght = in.available();
            // 建立Byte buffer
            byte[] buffer = new byte[lenght];
            // 將檔案中的數據讀到byte中
            in.read(buffer);
            result = EncodingUtils.getString(buffer, ENCODING);
        }catch (Exception e){
            e.printStackTrace();
        }

        return result;
    }


}
