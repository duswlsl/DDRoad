package com.seoul.ddroad.intro;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.seoul.ddroad.MainActivity;
import com.seoul.ddroad.R;
import com.seoul.ddroad.map.RestAPI;

import static com.seoul.ddroad.R.layout.activity_dust;

public class IntroActivity  extends Activity
{
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Intent intent = new Intent(getApplicationContext(),PermissionActivity.class);
            startActivity(intent);
            finish();
        }
    };
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        runThread();
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.postDelayed(runnable, 1000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
    }

    private void runThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                RestAPI restAPI = new RestAPI();
                restAPI.getinfo("trail");
                restAPI.getinfo("hospital");
                restAPI.getinfo("hotel");
                restAPI.getinfo("cafe");
                restAPI.getinfo("salon");
            }
        }).start();
    }
}

