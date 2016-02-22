/*
 *      Beschreibung:	Beinhaltet allen Code für die Newsdetailseite
 *      Autoren: 		Daniel Spieker
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

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class NewsDetail extends AppCompatActivity implements Updated.Refreshable, View.OnClickListener {

    private String link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        findViewById(R.id.newsdetail_website).setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        ContentManager.NewContext(this);
        MessageReporting.NewContext(this);
        ContentManager.UpdateActivity();
    }

    // Wird vom ContentManager aufgerufen um die Activity aktualisiert
    public void Refresh(final Updated update)
    {
        this.runOnUiThread(new Runnable() {
            public void run() {
                if (update.IsUpdated(Updated.News)) {
                    NewsContainer.NewsItem selectedNewsItem = update.GetNews().GetSelectedNewsItem();
                    ((TextView) findViewById(R.id.newsdetail_title)).setText(selectedNewsItem.Title);
                    ((TextView) findViewById(R.id.newsdetail_describtion)).setText(selectedNewsItem.Description);
                    ((TextView) findViewById(R.id.newsdetail_content)).setText(selectedNewsItem.Content);
                    ((TextView) findViewById(R.id.newsdetail_date)).setText(selectedNewsItem.GetFormatedDate());
                    link = selectedNewsItem.Link;
                }
            }
        });
    }

    @Override
    public void onClick(View v)
    {
        int id= v.getId();
        if(id==R.id.newsdetail_website) {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(link));
            startActivity(i);
        }
    }

}
