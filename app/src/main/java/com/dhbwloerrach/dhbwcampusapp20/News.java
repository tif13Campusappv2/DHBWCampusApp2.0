package com.dhbwloerrach.dhbwcampusapp20;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class News extends AppCompatActivity implements Updated.Refreshable, news_fragment.OnListFragmentInteractionListener,ViewPager.OnPageChangeListener {

    private ViewPager mViewPager;
    private  AppSectionsPagerAdapter mAppSectionsPagerAdapter;

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
        ErrorReporting.NewContext(this);
        ContentManager.UpdateActivity(this);
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
        else if(id==R.id.mensa_actionbar_refresh)
        {
            ContentManager.UpdateFromRemote(this);
            return true;
        }
        return false;
    }

    public void Goto(Pages page)
    {
        if(page== Pages.StartScreen)
        {
            this.overridePendingTransition(R.anim.scale_in, R.anim.right_out);
        }
    }

    private void InitializeTabView()
    {
        mAppSectionsPagerAdapter= new AppSectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.news_ViewPager);
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

    public static class AppSectionsPagerAdapter extends FragmentPagerAdapter
    {
        private Fragment[] items;
        private final int NumberTabs=5;
        private String[] TabHeaders={"Alle","News","Presse","Mitarbeiter","Dozierende"};

        public AppSectionsPagerAdapter(FragmentManager fm)
        {
            super(fm);
            ReloadFragments();
        }

        public void Update(NewsContainer newsContainer)
        {
            ((news_fragment)items[0]).UpdateNews(newsContainer.GetNewsItemList());
            for(int i=1;i<items.length;i++)
            {
                ((news_fragment)items[i]).UpdateNews(newsContainer.GetNewsItemList(i-1));
            }
            this.notifyDataSetChanged();
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
            items= new Fragment[NumberTabs];
            for(int i=0;i<NumberTabs;i++) {
                items[i] = new news_fragment();
            }
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

    public void Refresh(final Updated updater)
    {
        this.runOnUiThread(new Runnable() {
            public void run() {
                if(updater.IsUpdated(Updated.News))
                {
                    mAppSectionsPagerAdapter.Update(updater.GetNews());
                }
            }
        });
    }

    @Override
    public void onListFragmentInteraction(NewsContainer.NewsItem item){
        ErrorReporting.NewError(ErrorReporting.Errors.Video);
    }

}
