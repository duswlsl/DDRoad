package com.seoul.ddroad.diary;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.seoul.ddroad.R;

import java.util.ArrayList;
import java.util.Random;

import static java.sql.DriverManager.println;

/**
 * Created by guitarhyo on 2018-08-15.
 */
public class DiaryRegActivity extends AppCompatActivity {

    private String diaryTableName = "diary"; //테이블 이름
    private String diaryDatabaseName = "ddroad.db"; //데이터베이스 이름
    SqlLiteOpenHelper helper;
    SQLiteDatabase database;  // database를 다루기 위한 SQLiteDatabase 객체 생성
    Spinner spinner;

    @Override
    public void onCreate( Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diaryreg);
        spinner =(Spinner)findViewById(R.id.weatherSpinner);

        //액션바 사용
        ActionBar ab = getSupportActionBar() ;
        ab.setTitle("캘린더 등록하기") ;
        //메뉴바에 '<' 버튼이 생긴다.(두개는 항상 같이다닌다)
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeButtonEnabled(true);
        // 출처: http://ande226.tistory.com/141 [안디스토리]

        helper = new SqlLiteOpenHelper(this, // 현재 화면의 context
                diaryDatabaseName, // 파일명
                null, // 커서 팩토리
                1); // 버전 번호


        //날씨 이미지 스피너
        String[] arr = getResources().getStringArray(R.array.weather_item_array);
        ArrayList<String> list = new ArrayList<String>();
        for (int i=0; i < arr.length ; i++){
            list.add(arr[i]);
        }

        SpinnerAdapter spinnerAdapter = new SpinnerAdapter(this, R.layout.weather_spinner_item,list);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(DiaryRegActivity.this,"선택된 아이템 : "+spinner.getItemAtPosition(position),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    /**
     * Action Bar에 메뉴를 생성한다.
     * @param menu
     * @return
     */

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.list_menu2, menu);
        return true;
    }

    /**
     * 메뉴 아이템을 클릭했을 때 발생되는 이벤트...
     * @param item
     * @return
     */

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if( id == R.id.regPost ){//글 등록 누르면?


            final EditText diaryTitle=(EditText)findViewById(R.id.diaryTitle);
            final EditText diaryContent=(EditText)findViewById(R.id.diaryContent);

            database = helper.getWritableDatabase();
            if(database != null){
                Random randomGenerator = new Random();
                int randomInteger = randomGenerator.nextInt(100); //0 ~ 99 사이의 int를 랜덤으로 생성

                String imgstr = "@drawable/bichon1";
                String sql = "insert into diary(title, content,imgstr ,regdt) values(?, ?,?,datetime('now','localtime'))";
                Object[] params = { diaryTitle.getText(), diaryContent.getText(),imgstr};
                database.execSQL(sql, params);
                println("데이터 추가함.");
            }

            Toast.makeText(getApplicationContext(),
                    "등록 되었습니다.", Toast.LENGTH_SHORT)
                    .show();

            /*Intent intent = new Intent(
                    getApplicationContext(), // 현재 화면의 제어권자
                    DiaryActivity.class); // 다음 넘어갈 클래스 지정
            startActivity(intent); // 다음 화면으로 넘어간다*/
            onBackPressed();
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);

    }


}