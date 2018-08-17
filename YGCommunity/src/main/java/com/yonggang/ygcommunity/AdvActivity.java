package com.yonggang.ygcommunity;

import android.app.Activity;
import android.os.Bundle;

import butterknife.ButterKnife;

public class AdvActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adv);
        ButterKnife.bind(this);
//        WindowManager windowManager = getWindowManager();
//        Display display = windowManager.getDefaultDisplay();
//        int screenWidth = display.getWidth();
//        int screenHeight = display.getHeight();
//        Log.i("screen123", screenWidth + "-------" + screenHeight);
//        getFirstImg(screenWidth, screenHeight);


    }
}
