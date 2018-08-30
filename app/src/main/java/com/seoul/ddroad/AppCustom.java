package com.seoul.ddroad;

import android.app.Application;

public class AppCustom extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FontsOverride.setDefaultFont(this, "DEFAULT", "fonts/nanumbrush.ttf");
        FontsOverride.setDefaultFont(this, "MONOSPACE", "fonts/nanumbrush.ttf");
        FontsOverride.setDefaultFont(this, "SERIF", "fonts/nanumbrush.ttf");
        FontsOverride.setDefaultFont(this, "SANS_SERIF", "fonts/nanumbrush.ttf");
    }
}