package com.seoul.ddroad;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.seoul.ddroad.board.BoardFragment;
import com.seoul.ddroad.diary.DiaryFragment;
import com.seoul.ddroad.intro.DustFragment;
import com.seoul.ddroad.map.APIData;
import com.seoul.ddroad.map.MapFragment;
import com.seoul.ddroad.map.RestAPI;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        callFragment(1);
        ButterKnife.bind(this);


        new Thread(new Runnable() {
            @Override
            public void run() {

                RestAPI api = new RestAPI();
                APIData apiData = new APIData();
                apiData = api.getinfo(apiData, "cafe");

//                for(int i=0;i<1000;i++) {
//                    System.out.println(i + " : " + apiData.cafe[i].getTitle());
//                }

            }
        }).start();
    }

    @OnClick({R.id.btn_dust, R.id.btn_map, R.id.btn_diary, R.id.btn_board})
    void tabClick(View v) {
        int fragment_no = Integer.parseInt(v.getTag().toString());
        callFragment(fragment_no);
    }

    private void callFragment(int fragment_no) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (fragment_no) {
            case 1:
                DustFragment fragment1 = new DustFragment();
                transaction.replace(R.id.fragment_container, fragment1);
                break;
            case 2:
                MapFragment fragment2 = new MapFragment();
                transaction.replace(R.id.fragment_container, fragment2);
                break;
            case 3:
                DiaryFragment fragment3 = new DiaryFragment();
                transaction.replace(R.id.fragment_container, fragment3);
                break;
            case 4:
                BoardFragment fragment4 = new BoardFragment();
                transaction.replace(R.id.fragment_container, fragment4);
                break;
        }
        transaction.commit();
    }
}
