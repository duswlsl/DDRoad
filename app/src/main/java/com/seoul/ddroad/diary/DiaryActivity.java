package com.seoul.ddroad.diary;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.seoul.ddroad.R;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by guitarhyo on 2018-08-15.
 */
public class DiaryActivity extends AppCompatActivity {
    private Date mCurrentDate;
    //private CaldroidFragment calendarFragment;

    @Override
    public void onCreate( Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);


        mCurrentDate = Calendar.getInstance().getTime();
        Calendar cal = Calendar.getInstance();
        Date currentDate = cal.getTime();

    }
}