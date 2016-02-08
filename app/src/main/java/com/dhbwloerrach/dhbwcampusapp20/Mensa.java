/* ToDo Mensa:
    - Überschriften bei Laden der Daten anpassen
    - Preiseinstellungen für verschiedene Personengruppen importieren (momentan hardcoded auf Studenten)
    - Datenbankanbindung
    - Evtl. andere Symbole für Vegan/Vegetarisch
    - Evtl. Zeilenumbrüche aus API importieren
    - Aufruf zum Aktualisieren an andere Stelle packen
 */

package com.dhbwloerrach.dhbwcampusapp20;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class Mensa extends AppCompatActivity implements ViewPager.OnPageChangeListener, mensa_fragment.OnFragmentInteractionListener, Updated.Refreshable {


    private ViewPager mViewPager;
    private  AppSectionsPagerAdapter mAppSectionsPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensa);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                Goto(Pages.Guthaben);
            }
        });

        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
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
    protected void onResume()
    {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Goto(Pages.StartScreen);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.mensa, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
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
        else if(page == Pages.Guthaben)
        {
            startActivity(new Intent(Mensa.this,Guthaben.class));
            this.overridePendingTransition(R.anim.right_in, R.anim.scale_out);
        }
    }

    private void InitializeTabView()
    {
        mAppSectionsPagerAdapter= new AppSectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.mensa_ViewPager);
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

    public void onFragmentInteraction(Uri uri)
    {

    }

    public static class AppSectionsPagerAdapter extends FragmentPagerAdapter
    {
        private Fragment[] items;
        private final int NumberTabs=5;
        private String[] TabHeaders;

        public AppSectionsPagerAdapter(FragmentManager fm)
        {
            super(fm);
            ReloadFragments();
        }

        public void Update(MensaPlan mensaplan, int Role)
        {
            for(int i=0;i<items.length;i++)
            {
                TabHeaders[i]=mensaplan.GetDay(i).GetFormatedDate();
                ((mensa_fragment)items[i]).UpdateData(mensaplan.GetDay(i),Role);
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
            TabHeaders= new String[NumberTabs];
            for(int i=0;i<NumberTabs;i++) {
                items[i] = new mensa_fragment();
                TabHeaders[i]="Section " + (i+1);
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
                if(updater.IsUpdated(Updated.Mensa) && updater.IsUpdated(Updated.Role))
                {
                    mViewPager.setCurrentItem(updater.GetMensaPlan().GetBestFittingDay(),true);
                    mAppSectionsPagerAdapter.Update(updater.GetMensaPlan(), updater.GetRole());
                }
                if(updater.IsUpdated(Updated.Guthaben))
                {

                }
            }
        });
    }

}