package com.dhbwloerrach.dhbwcampusapp20;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class StartScreen extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, Updated.Refreshable {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        LoadClickHandler();


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onClick(View v)
    {
        int id= v.getId();
        if(id==R.id.dash_Mensa)
            Goto(Pages.Mensa);
        else if(id==R.id.dash_Guthaben)
            Goto(Pages.Guthaben);
        else if(id==R.id.dash_News)
            Goto(Pages.News);
        else if(id==R.id.fab)
            ContentManager.UpdateMensaData(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.start_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == R.id.nav_geld) {
            Goto(Pages.Guthaben);
        } else if (id == R.id.nav_lageplan) {
            Goto(Pages.Lageplan);
        } else if (id == R.id.nav_mensa) {
            Goto(Pages.Mensa);
        } else if (id == R.id.nav_news) {
            Goto(Pages.News);
        }else{
            (Toast.makeText(this,R.string.unkown_action,Toast.LENGTH_LONG)).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
        ErrorReporting.NewContext(this);
        ContentManager.UpdateActivity(this);
    }

    @Override
    public void onStop() {
        super.onStop();
    }


    public  void LoadClickHandler()
    {
        findViewById(R.id.dash_Mensa).setOnClickListener(this);
        findViewById(R.id.dash_News).setOnClickListener(this);
        findViewById(R.id.dash_Guthaben).setOnClickListener(this);
        findViewById(R.id.fab).setOnClickListener(this);

        // Actionbar open
        ((NavigationView) findViewById(R.id.nav_view)).setNavigationItemSelectedListener(this);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, (Toolbar) findViewById(R.id.toolbar), R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
    }

    public void Goto(Pages page)
    {
        if(page== Pages.Mensa)
        {
            startActivity(new Intent(StartScreen.this,Mensa.class));
            this.overridePendingTransition(R.anim.right_in, R.anim.scale_out);
        }
        else if(page == Pages.Guthaben)
        {
            startActivity(new Intent(StartScreen.this,Guthaben.class));
            this.overridePendingTransition(R.anim.right_in, R.anim.scale_out);
        }
        else if(page == Pages.Lageplan)
        {
            startActivity(new Intent(StartScreen.this,Lageplan.class));
            this.overridePendingTransition(R.anim.right_in, R.anim.scale_out);
        }
        else if(page == Pages.News)
        {
            startActivity(new Intent(StartScreen.this,News.class));
            this.overridePendingTransition(R.anim.right_in, R.anim.scale_out);
        }
    }

    public void Refresh(Updated areas)
    {
        if(areas.IsUpdated(Updated.Mensa))
        {
            MensaPlan loadedPlan= areas.GetMensaPlan();
        }
        if(areas.IsUpdated(Updated.News))
        {

        }
        if(areas.IsUpdated(Updated.Guthaben))
        {

        }
    }
}
