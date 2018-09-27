package com.seoul.ddroad;

import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.seoul.ddroad.board.BoardFragment;
import com.seoul.ddroad.diary.DiaryFragment;
import com.seoul.ddroad.intro.DustFragment;
import com.seoul.ddroad.map.MapFragment;
import com.seoul.ddroad.map.RestAPI;
import com.seoul.ddroad.setting.SettingFragment;

import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        callFragment(1);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_dust, R.id.btn_map, R.id.btn_diary, R.id.btn_board, R.id.btn_setting})
    void tabClick(View v) {
        int fragment_no = Integer.parseInt(v.getTag().toString());
        callFragment(fragment_no);
    }

    private void callFragment(int fragment_no) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (fragment_no) {
            case 1:
                Fragment fragment1 = new DustFragment();
                transaction.replace(R.id.fragment_container, fragment1);
                break;
            case 2:
                Fragment fragment2 = new MapFragment();
                transaction.replace(R.id.fragment_container, fragment2, "map");
                break;
            case 3:
                Fragment fragment3 = new DiaryFragment();
                transaction.replace(R.id.fragment_container, fragment3);
                break;
            case 4:
                Fragment fragment4 = new BoardFragment();
                transaction.replace(R.id.fragment_container, fragment4);
                break;
            case 5:
                Fragment fragment5 = new SettingFragment();
                transaction.replace(R.id.fragment_container, fragment5);
                break;
        }
        transaction.commit();
    }
}
