package com.seoul.ddroad.intro;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.seoul.ddroad.MainActivity;
import com.seoul.ddroad.R;

import static com.seoul.ddroad.R.layout.activity_dust;

/**
 * Created by guitarhyo on 2018-08-15.
 */
public class IntroActivity  extends Activity
{
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Intent intent = new Intent(getApplicationContext(),dustActivity_cool.class);
            startActivity(intent);
            finish();
        }
    };
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.postDelayed(runnable, 4000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
    }
}

