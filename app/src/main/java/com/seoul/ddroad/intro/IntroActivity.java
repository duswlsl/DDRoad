package com.seoul.ddroad.intro;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.seoul.ddroad.MainActivity;
import com.seoul.ddroad.R;
import com.seoul.ddroad.map.RestAPI;

import static com.seoul.ddroad.R.layout.activity_main;

public class IntroActivity  extends Activity implements Animation.AnimationListener {

    ImageView introView;
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
        introView = findViewById(R.id.intro_textBalloon);
        final Animation anim = AnimationUtils.loadAnimation(this, R.anim.bounce);
        cBounceInterpolar interpolar = new cBounceInterpolar(0.4, 10);
        introView.setAnimation(anim);
        anim.setInterpolator(interpolar);
        runThread();
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

    @Override
    protected void onStop() {
        super.onStop();
        introView.setAnimation(null);
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

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}

