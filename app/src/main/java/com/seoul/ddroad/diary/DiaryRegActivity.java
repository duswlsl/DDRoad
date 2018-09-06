package com.seoul.ddroad.diary;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.seoul.ddroad.R;

/**
 * Created by guitarhyo on 2018-08-15.
 */
public class DiaryRegActivity extends AppCompatActivity {
    @Override
    public void onCreate( Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diaryreg);

        //액션바 사용
        ActionBar ab = getSupportActionBar() ;
        ab.setTitle("캘린더 등록하기") ;
        //메뉴바에 '<' 버튼이 생긴다.(두개는 항상 같이다닌다)
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeButtonEnabled(true);
        // 출처: http://ande226.tistory.com/141 [안디스토리]
    }
}