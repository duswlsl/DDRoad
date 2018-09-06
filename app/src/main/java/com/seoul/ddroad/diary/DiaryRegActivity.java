package com.seoul.ddroad.diary;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

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
        if( id == R.id.regPost ){

            Intent intent = new Intent(
                    getApplicationContext(), // 현재 화면의 제어권자
                    DiaryActivity.class); // 다음 넘어갈 클래스 지정
            startActivity(intent); // 다음 화면으로 넘어간다
            return true;
        }
        return super.onOptionsItemSelected(item);

    }
}