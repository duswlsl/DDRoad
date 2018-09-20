package com.seoul.ddroad.diary;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.seoul.ddroad.R;

import java.util.HashMap;

public class DetailActivity extends AppCompatActivity {

  //  private TextView comeOn; //상세화면 텍스트뷰
    private TextView diaryDetailDate; //날짜
    private TextView diaryDetailTitle; //title
    private ImageView diaryDetailImage;
    private TextView diaryDetailContent;
    private Button diaryDetailEdit;
    private Button diaryDetailDelete;

    HashMap<String,Object> diaryMap = new HashMap<String,Object>();

    private SqlLiteDao sqlLiteDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        sqlLiteDao = new SqlLiteDao(DetailActivity.this);

        Intent intent = getIntent();
        int diaryId = intent.getExtras().getInt("diaryId");


        diaryMap = sqlLiteDao.selectDiary(diaryId); //메서드에서 데이터를 가져옵니다.!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!원래 이거 만들었어야함


       // comeOn = (TextView) findViewById(R.id.comeOn); //상세화면 텍스트 뷰 선언
        diaryDetailDate = (TextView) findViewById(R.id.diaryDetailDate);
        diaryDetailTitle = (TextView) findViewById(R.id.diaryDetailTitle);
        diaryDetailImage = (ImageView) findViewById(R.id.diaryDetailImage);
        diaryDetailContent = (TextView) findViewById(R.id.diaryDetailContent);

        diaryDetailEdit = (Button) findViewById(R.id.diaryDetailEdit);
        diaryDetailDelete = (Button) findViewById(R.id.diaryDetailDelete);

        diaryDetailEdit.setOnClickListener(
                new Button.OnClickListener(){
                    @Override
                    public  void  onClick(View view){
                        Toast.makeText(getApplicationContext(),"수정.",Toast.LENGTH_LONG).show();
                    }
                }
        );

        diaryDetailDelete.setOnClickListener(
                new Button.OnClickListener(){
                    @Override
                    public  void  onClick(View view){
                        Toast.makeText(getApplicationContext(),"삭제.",Toast.LENGTH_LONG).show();
                    }
                }
        );


        //comeOn.setText("권바보야 MAP이 뭐냐~~!!" + diaryMap.toString()); //데이터 확인합니다.
        String redgt = (String)diaryMap.get("regdt");
        String content = (String)diaryMap.get("content");
        String title = (String)diaryMap.get("title");
        diaryDetailDate.setText(redgt);
        diaryDetailContent.setText(content);
        diaryDetailTitle.setText(title);
        String resName = (String)diaryMap.get("imgstr");;
        String packName = this.getPackageName(); // 패키지명
        int resID = getResources().getIdentifier(resName, "drawable", packName);
        diaryDetailImage.setImageResource(resID);

        //데이터 확인이되면 화면을 만들고 각각 view 레이아웃에 뿌려줍니다.
        //diaryId, title, content, imgstr, regdt 의 데이터를 뿌려야겠죠?????????????


        }



}


