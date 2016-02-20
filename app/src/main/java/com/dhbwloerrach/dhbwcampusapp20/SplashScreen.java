/*
 *      Beschreibung:	Beinhaltet allen Code der Splash-Screen Activity
 *      Autoren: 		Philipp Mosch, Daniel Spieker
 *      Projekt:		Campus App 2.0
 *
 *      ╔══════════════════════════════╗
 *      ║ History                      ║
 *      ╠════════════╦═════════════════╣
 *      ║   Datum    ║    Änderung     ║
 *      ╠════════════╬═════════════════╣
 *      ║ 2015-xx-xx ║
 *      ║ 20xx-xx-xx ║
 *      ║ 20xx-xx-xx ║
 *      ╚════════════╩═════════════════╝
 *      Wichtig:           Tabelle sollte mit monospace Schriftart dargestellt werden
 */
package com.dhbwloerrach.dhbwcampusapp20;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.VideoView;

public class SplashScreen extends AppCompatActivity {

    VideoView anim;
    private Intent Weiterleitung;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        // Inizialisiert den Messagereporten und den ContentManager und versucht ein Onlineupdate durchzuführen
        final Activity context=this;
        (new Thread()
        {
            public void run()
            {
                MessageReporting.Initialize(context);
                ContentManager.Initialize(context);
                ContentManager.OnlineUpdate();
            }
        }).start();

        // Bereitet die Lade-Animation auf dem Splash-Screen vor
        anim=(VideoView) findViewById(R.id.splash_anim);
        Uri uri = Uri.parse("android.resource://com.dhbwloerrach.dhbwcampusapp20/" + R.raw.splash_anim);
        anim.setVideoURI(uri);
    }

    @Override
    public void onWindowFocusChanged(boolean visible) {
        if(visible) {
            // Versucht die Animation zu starten
            try {
                anim.start();
            } catch (Exception e) {
                MessageReporting.ShowMessage(MessageReporting.Messages.Video);
            }
            (new Thread()
            {
                // Versucht drei Sekunden zu warten und startet anschließend die Hauptactivity der App
                public void run()
                {
                    try
                    {
                        sleep(3000);
                    }
                    catch (Exception e){e.printStackTrace();}
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
