package com.dhbwloerrach.dhbwcampusapp20;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.view.Window;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

public class SplashScreen extends AppCompatActivity {

    AnimationDrawable splashAnim;
    private Intent Weiterleitung;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_splash_screen);

        ImageView animImage = (ImageView) findViewById(R.id.SplashView);
        animImage.setBackgroundResource(R.drawable.splash_anim);
        splashAnim = (AnimationDrawable) animImage.getBackground();
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onWindowFocusChanged(boolean visible) {
        if(visible) {
            splashAnim.start();
            (new Thread()
            {
                public void run()
                {
                    try
                    {
                        sleep(3000);
                    }
                    catch (Exception e){}
                    finally
                    {
                        if(Weiterleitung==null)
                            Weiterleitung = new Intent(SplashScreen.this, StartScreen.class);
                        Weiterleitung.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(Weiterleitung);
                    }
                }
            }).start();
        }
        
    }
}
