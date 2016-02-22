/*
 *      Beschreibung:	Beinhaltet allen Code für die Newsseite
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
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

public class News extends AppCompatActivity implements Updated.Refreshable, news_fragment.OnListFragmentInteractionListener,ViewPager.OnPageChangeListener{
    private AppSectionsPagerAdapter mAppSectionsPagerAdapter;
    private NewsContainer currentContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        InitializeTabView();
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        ContentManager.NewContext(this);
        MessageReporting.NewContext(this);
        ContentManager.UpdateActivity();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Goto(Pages.StartScreen);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.news, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if (id == android.R.id.home) {
            finish();
            Goto(Pages.StartScreen);
            return true;
        }
        else if(id==R.id.news_actionbar_refresh)
        {
            ContentManager.OnlineUpdate();
            return true;
        }
        return false;
    }

    // Verwaltet die Navigation innerhalb der App
    public void Goto(Pages page)
    {
        if(page== Pages.StartScreen)
        {
            this.overridePendingTransition(R.anim.scale_in, R.anim.right_out);
        }
        else if(page == Pages.NewsDetail)
        {
            startActivity(new Intent(News.this,NewsDetail.class));
            this.overridePendingTransition(R.anim.right_in, R.anim.scale_out);
        }

    }

    // Initialisert die Fragments für die einzelnen Tabs
    private void InitializeTabView()
    {
        mAppSectionsPagerAdapter= new AppSectionsPagerAdapter(getSupportFragmentManager());
        ViewPager  mViewPager = (ViewPager) findViewById(R.id.news_ViewPager);
        mViewPager.setAdapter(mAppSectionsPagerAdapter);
        mViewPager.setOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    // Stellt einen Adapter für die Fragmenttabs der Newsseite bereit
    public static class AppSectionsPagerAdapter extends FragmentPagerAdapter
    {
        private news_fragment[] items;
        private final int NumberTabs=5;
        private String[] TabHeaders={"Alle","News","Presse","Mitarbeiter","Dozierende"};

        public AppSectionsPagerAdapter(FragmentManager fm)
        {
            super(fm);
            ReloadFragments();
        }

        // Updatet den Inhalt eines Newstabs
        public void Update(NewsContainer newsContainer)
        {
            items[0].UpdateNews(newsContainer.GetNewsItemList());
            for(int i=1;i<items.length;i++)
                items[i].UpdateNews(newsContainer.GetNewsItemList(i-1));
            this.notifyDataSetChanged();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            news_fragment fragment = (news_fragment) super.instantiateItem(container, position);
            items[position] = fragment;
            return fragment;
        }

        @Override
        public Fragment getItem(int i)
        {
            if(i<0 || i>=items.length)
                return new Fragment();
            else
                return items[i];
        }

        private void ReloadFragments()
        {
            items= new news_fragment[NumberTabs];
            for(int i=0;i<NumberTabs;i++)
                items[i] = new news_fragment();
        }

        @Override
        public int getCount()
        {
            return items.length;
        }

        @Override
        public CharSequence getPageTitle(int position)
        {
            return TabHeaders[position];
        }
    }

    // Wird vom ContentManager aufgerufen, um die Activity zu aktualisieren
    public void Refresh(final Updated updater)
    {
        this.runOnUiThread(new Runnable() {
            public void run() {
                if(updater.IsUpdated(Updated.News))
                {
                    currentContainer=updater.GetNews();
                    mAppSectionsPagerAdapter.Update(currentContainer);
                }
            }
        });
    }

    // Wird von einem Fragment aufgerufen, sobald auf ein Newsitem gecklickt wird. Leitet auf die Newsdetailseite weiter
    @Override
    public void onListFragmentInteraction(NewsContainer.NewsItem item){
        for(int i=0;i<currentContainer.GetCountNews();i++)
            if(currentContainer.GetNewsItem(i)==item)
            {
                ContentManager.UpdateSelectedNewsItem(i);
                Goto(Pages.NewsDetail);
            }
    }

}
