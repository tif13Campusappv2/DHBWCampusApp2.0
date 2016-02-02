package com.dhbwloerrach.dhbwcampusapp20;

import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

public class SplashScreen extends AppCompatActivity {

    AnimationDrawable splashAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        ImageView animImage = (ImageView) findViewById(R.id.SplashView);
        animImage.setBackgroundResource(R.drawable.splash_anim);
        splashAnim = (AnimationDrawable) animImage.getBackground();
    }

    @Override
    public void onWindowFocusChanged(boolean visible) {
        if(visible)
        splashAnim.start();
    }
}
