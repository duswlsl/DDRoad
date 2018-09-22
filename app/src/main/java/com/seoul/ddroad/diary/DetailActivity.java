package com.seoul.ddroad.diary;


import android.app.ActionBar;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.seoul.ddroad.R;

import java.io.File;
import java.util.HashMap;

public class DetailActivity extends AppCompatActivity {

  //  private TextView comeOn; //상세화면 텍스트뷰
    private TextView diaryDetailDate; //날짜
    private TextView diaryDetailTitle; //title
    private ImageView diaryDetailImage;
    private TextView diaryDetailContent;
    private Button diaryDetailEdit;
    private Button diaryDetailDelete;
    int diaryId;
    private TableLayout diaryDetailImg;
    private  String diaryDetailImgDir;

    HashMap<String,Object> diaryMap = new HashMap<String,Object>();

    private SqlLiteDao sqlLiteDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        sqlLiteDao = new SqlLiteDao(DetailActivity.this);

        Intent intent = getIntent();
        diaryId = intent.getExtras().getInt("diaryId");


        diaryMap = sqlLiteDao.selectDiary(diaryId); //메서드에서 데이터를 가져옵니다.!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!원래 이거 만들었어야함


       // comeOn = (TextView) findViewById(R.id.comeOn); //상세화면 텍스트 뷰 선언
        diaryDetailDate = (TextView) findViewById(R.id.diaryDetailDate);
        diaryDetailTitle = (TextView) findViewById(R.id.diaryDetailTitle);
        diaryDetailImage = (ImageView) findViewById(R.id.diaryDetailImage);
        diaryDetailContent = (TextView) findViewById(R.id.diaryDetailContent);

        diaryDetailEdit = (Button) findViewById(R.id.diaryDetailEdit);
        diaryDetailDelete = (Button) findViewById(R.id.diaryDetailDelete);
        diaryDetailImg = (TableLayout) findViewById(R.id.diaryDetailImg);

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
                        sqlLiteDao.deleteDiary(diaryId);
                        Toast.makeText(getApplicationContext(),"삭제 되었습니다.",Toast.LENGTH_LONG).show();
                        onBackPressed();
                        finish();
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


        //db에서 이미지 경로 가져오기
        HashMap<String,Object> diaryImgMap = new HashMap<String,Object>(); //hashmap 선언
        diaryImgMap = sqlLiteDao.selectDiaryImg(diaryId); //diaryImg db에서 가져옴
        if(diaryImgMap != null) {

            diaryDetailImgDir= (String)diaryImgMap.get("imgDir");

            Toast.makeText(getApplicationContext(), diaryDetailImgDir, Toast.LENGTH_LONG).show();
        }
        //이미지뷰뿌리기
        if(diaryDetailImgDir != "") {
            ImageView iv = new ImageView(this);
            iv.setLayoutParams(new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT));
            //  iv.setBackgroundResource(R.drawable.bichon1);
            File f = new File(diaryDetailImgDir);
            Bitmap d = new BitmapDrawable(getApplicationContext().getResources(), f.getAbsolutePath()).getBitmap();
            //Bitmap scaled = com.fxn.utility.Utility.getScaledBitmap(512, com.fxn.utility.Utility.getExifCorrectedBitmap(f));
            Bitmap scaled = com.fxn.utility.Utility.getScaledBitmap(512, d);
            iv.setImageBitmap(scaled);
            diaryDetailImg.addView(iv);
        }






        //데이터 확인이되면 화면을 만들고 각각 view 레이아웃에 뿌려줍니다.
        //diaryId, title, content, imgstr, regdt 의 데이터를 뿌려야겠죠?????????????


        }




}


