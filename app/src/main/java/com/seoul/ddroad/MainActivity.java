package com.seoul.ddroad;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatCallback;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.seoul.ddroad.board.BoardActivity;
import com.seoul.ddroad.board.BoardFragment;
import com.seoul.ddroad.diary.DiaryActivity;
import com.seoul.ddroad.diary.DiaryFragment;
import com.seoul.ddroad.intro.DustFragment;
import com.seoul.ddroad.intro.IntroActivity;
import com.seoul.ddroad.intro.dustActivity_cool;
import com.seoul.ddroad.map.MapFragment;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        callFragment(1);

        ButterKnife.bind(this);
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
