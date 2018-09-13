package com.seoul.ddroad.diary;


import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.seoul.ddroad.R;

import java.util.HashMap;

public class DetailActivity extends AppCompatActivity {

    private TextView comeOn; //상세화면 텍스트뷰

    private String diaryTableName = "diary"; //테이블 이름
    private String diaryDatabaseName = "ddroad.db"; //데이터베이스 이름
    SqlLiteOpenHelper helper;
    SQLiteDatabase database;  // database를 다루기 위한 SQLiteDatabase 객체 생성
    HashMap<String,Object> diaryMap = new HashMap<String,Object>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //DB 선언
        helper = new SqlLiteOpenHelper(this, // 현재 화면의 context
                diaryDatabaseName, // 파일명
                null, // 커서 팩토리
                1); // 버전 번호
        database = helper.getWritableDatabase();


        Intent intent = getIntent();
        int diaryId = intent.getExtras().getInt("diaryId");

        diaryMap = selectDiary(diaryId); //메서드에서 데이터를 가져옵니다.!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!원래 이거 만들었어야함


        comeOn = (TextView) findViewById(R.id.comeOn); //상세화면 텍스트 뷰 선언

        comeOn.setText("권바보야 MAP이 뭐냐~~!!" + diaryMap.toString()); //데이터 확인합니다.

        //데이터 확인이되면 화면을 만들고 각각 view 레이아웃에 뿌려줍니다.
        //diaryId, title, content, imgstr, regdt 의 데이터를 뿌려야겠죠?????????????


        }


    public HashMap<String,Object> selectDiary(int diaryId){
        HashMap<String,Object> map = new HashMap<String,Object>();

        if(database != null){
            //sqlite에서 값을 가져와서  map 에 담아야합니다~~~~~~!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!꼭 해야봐야함~~~~~~~!!!!!!!!!
            //http://here4you.tistory.com/49 참고 해보세요~ 정말 쉽죠~~
        }
        return map;
    }
}


