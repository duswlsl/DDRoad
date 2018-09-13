package com.seoul.ddroad.diary;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.seoul.ddroad.R;

public class DetailActivity extends AppCompatActivity {

    private TextView comeOn; //상세화면 텍스트뷰

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        int diaryId = intent.getExtras().getInt("diaryId");

        comeOn = (TextView) findViewById(R.id.comeOn); //상세화면 텍스트 뷰 선언
        comeOn.setText("개어려뷰.." + diaryId);



        }
}


