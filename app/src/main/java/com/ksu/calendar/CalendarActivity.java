package com.ksu.calendar;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.ksu.custom_view.CalendarDayEvent;
import com.ksu.custom_view.CompactCalendarView;
import com.ksu.smartagent.R;
import com.ksu.smartagent.StatusBarColorChange;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CalendarActivity extends AppCompatActivity {

    //宣告View
    private Toolbar toolbar;
    private CompactCalendarView calendarView;

    private Calendar currentCalender = Calendar.getInstance(Locale.getDefault());
    private SimpleDateFormat dateFormatForMonth = new SimpleDateFormat("yyyy年 MMM", Locale.getDefault());

    //"按兩下返回鍵結束程式" 使用的變數
    private long mExitTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        init();
    }

    private void init(){
        //建立更改StatusBar顏色的物件
        StatusBarColorChange statusBarColorChange = new StatusBarColorChange(this);

        //setting Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //建立日曆
        calendarView = (CompactCalendarView) findViewById(R.id.calendar_view);
        calendarView.drawSmallIndicatorForEvents(true); //產生小型指示器
        calendarView.setShouldShowMondayAsFirstDay(false); //是否指定星期一為每周的第一天
        calendarView.setUseThreeLetterAbbreviation(true); //是否使用三字縮寫來顯示星期幾

        //設定Toolbar Title
        setToolbarTitle();

        calendarView.invalidate(); //無效化...不知道甚麼意思
        calendarView.setListener(CalendarViewListener); //指定日曆的傾聽事件

    }

    //日曆的傾聽事件
    CompactCalendarView.CompactCalendarViewListener CalendarViewListener = new CompactCalendarView.CompactCalendarViewListener() {
        @Override
        public void onDayClick(Date dateClicked) {
            Log.d("MainActivity", "點擊的日期為 : " + dateClicked.getDate());
            addEvents(calendarView,  dateClicked);
        }

        @Override
        public void onMonthScroll(Date firstDayOfNewMonth) {
            //滑動時改變Toolbar Title顯示的月份
            setToolbarTitle();
        }
    };

    private void addEvents(CompactCalendarView CalendarView, Date Date) {
        //currentCalender.setTime(new Date());
        //currentCalender.set(Calendar.DAY_OF_MONTH, 1);
        //Date firstDayOfMonth = currentCalender.getTime();

        currentCalender.set(Calendar.MONTH , Date.getMonth());
        currentCalender.set(Calendar.DATE , Date.getDate());
        calendarView.addEvent(new CalendarDayEvent(currentCalender.getTimeInMillis(), Color.argb(255, 169, 68, 65)), false);

        String str = String.format("%tF %<tT", currentCalender.getTimeInMillis());
    }

    //設定Toolbar Title的method
    private void setToolbarTitle() {
        getSupportActionBar().setTitle(dateFormatForMonth.format(calendarView.getFirstDayOfCurrentMonth()));
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
