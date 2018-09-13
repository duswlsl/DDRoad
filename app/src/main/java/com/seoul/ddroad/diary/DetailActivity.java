package com.seoul.ddroad.diary;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.seoul.ddroad.R;

import java.util.HashMap;

public class DetailActivity extends AppCompatActivity {

    private TextView comeOn; //상세화면 텍스트뷰
    private TextView textView4;
    private TextView textView5;
    private ImageView imageView2;
    private ScrollView content;

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
        textView4 = (TextView) findViewById(R.id.textView4);
        textView5 = (TextView) findViewById(R.id.textView5);
        imageView2 = (ImageView) findViewById(R.id.imageView2);
        content = (ScrollView) findViewById(R.id.content);


        comeOn.setText("권바보야 MAP이 뭐냐~~!!" + diaryMap.toString()); //데이터 확인합니다.


        //데이터 확인이되면 화면을 만들고 각각 view 레이아웃에 뿌려줍니다.
        //diaryId, title, content, imgstr, regdt 의 데이터를 뿌려야겠죠?????????????


        }


    public HashMap<String,Object> selectDiary(int diaryId){
        HashMap<String,Object> map = new HashMap<String,Object>();

        if(database != null){
            //sqlite에서 값을 가져와서  map 에 담아야합니다~~~~~~!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!꼭 해야봐야함~~~~~~~!!!!!!!!!
            //http://here4you.tistory.com/49 참고 해보세요~ 정말 쉽죠~~
            String sql = "select diaryId, title, content, imgstr, regdt from " +diaryTableName+ " where diaryId = "+diaryId+";";
            Cursor result = database.rawQuery(sql, null);

            if(result.moveToFirst()){ //커서가 처음지점이 값이있으면,
                int id = result.getInt(0);
                String title = result.getString(1); // 두번째 속성
                String content = result.getString(2);    // 세번째 속성
                String imgstr = result.getString(3);    // 세번째 속성
                String regdt = result.getString(4);    // 세번째 속성

                map.put("diaryId",diaryId);
                map.put("title",title);
                map.put("content",content);
                map.put("imgstr",imgstr);
                map.put("regdt",regdt);

                Toast.makeText(this, "diaryId= "+diaryId, Toast.LENGTH_LONG).show();
            }
            result.close();

        }
        return map;
    }
}


