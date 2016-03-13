package com.ksu.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by SxNight on 2015/5/10.
 */
public class KeywordDBHelper extends SQLiteOpenHelper {

    //資料庫名稱
    private static final String Keyword_DATABASE_NAME = "Keyword.db";

    //資料庫版本，資料結構改變的時候要更改這個數字，通常是加一
    private static final int DATABASE_VERSION = 1;

    //資料表名稱
    private static final String PLACE_TABLE_NAME  = "PlaceKeywordDB";
    private static final String TIME_TABLE_NAME   = "TimeKeywordDB";
    private static final String CUSTOM_TABLE_NAME = "CustomKeywordDB";

    // 資料庫物件，固定的欄位變數
    private static SQLiteDatabase database;

    //建構子
    public KeywordDBHelper(Context context) {
        super(context, Keyword_DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //建立地點資料表
        db.execSQL("CREATE TABLE " + PLACE_TABLE_NAME +"(" +
                "_id integer primary key autoincrement, " + //_id 為主鍵 會自動增長
                "Keyword text no null, Priority integer no null)"); // Keyword = 關鍵字 , Priority = 優先序

        //時間
        db.execSQL("CREATE TABLE " + TIME_TABLE_NAME + "(" +
                "_id integer primary key autoincrement, " +
                "Keyword text no null, TimeCategory text no null)"); //TimeCategory = 時間分類


        //自訂
        db.execSQL("CREATE TABLE " + CUSTOM_TABLE_NAME + "(" +
                "_id integer primary key autoincrement, " +
                "Keyword text no null, CustomCategory text no null)"); //Category用來判別是時間還是地點
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + PLACE_TABLE_NAME  + ")" );
        db.execSQL("DROP TABLE IF EXISTS " + TIME_TABLE_NAME   + ")" );
        db.execSQL("DROP TABLE IF EXISTS " + CUSTOM_TABLE_NAME + ")" );
        onCreate(db);
    }

    // 需要資料庫的元件呼叫這個方法，這個方法在一般的應用都不需要修改
    public static SQLiteDatabase getDatabase(Context context) {
        if (database == null || !database.isOpen()) {
            database = new KeywordDBHelper(context).getWritableDatabase();
        }

        return database;
    }
}
