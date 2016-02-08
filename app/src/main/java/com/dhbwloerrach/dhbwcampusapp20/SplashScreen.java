package com.dhbwloerrach.dhbwcampusapp20;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.VideoView;

public class SplashScreen extends AppCompatActivity {

    AnimationDrawable splashAnim;
    VideoView anim;
    private Intent Weiterleitung;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);



        final Activity context=this;
        (new Thread()
        {
            public void run()
            {
                ErrorReporting.Initialize(context);
                ContentManager.UpdateFromRemote(context);
            }
        }).start();

        anim=(VideoView) findViewById(R.id.splash_anim);
        Uri uri = Uri.parse("android.resource://com.dhbwloerrach.dhbwcampusapp20/" + R.raw.splash_anim);
        anim.setVideoURI(uri);
        /*ImageView animImage = (ImageView) findViewById(R.id.SplashView);
        animImage.setBackgroundResource(R.drawable.splash_anim);
        splashAnim = (AnimationDrawable) animImage.getBackground();*/
    }

    @Override
    public void onWindowFocusChanged(boolean visible) {
        if(visible) {
            //splashAnim.start();

            try {
                anim.start();
            } catch (Exception e) {
                ErrorReporting.NewError(ErrorReporting.Errors.Video);
            }
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
