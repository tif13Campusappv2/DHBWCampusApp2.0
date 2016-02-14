package com.dhbwloerrach.dhbwcampusapp20;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

public class Guthaben extends AppCompatActivity implements Updated.Refreshable{

    // 0= Mensa Menü 1,1= Mensa Menü 2, 2= Mensa Menü 3, 3= Mensa Salat, 4= SW Kopie, 5= Farbkopie, 6= Wasser, 7= Cola etc.
    // Note 0-4 werden dynamisch gesetzt
    private double prices[]={2.90,3.20,3.60,0.70, 0.04,0.08,1.20,1.50};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guthaben);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onStart()
    {
        super.onStart();
        ContentManager.NewContext(this);
        ErrorReporting.NewContext(this);
        ContentManager.UpdateActivity();
        ((TextView) findViewById(R.id.guthaben_list_swkopie_desc)).setText(getString(R.string.guthaben_list_kopiesw_desc).replace("%", FormatPrice(prices[4]) + "€"));
        ((TextView) findViewById(R.id.guthaben_list_clkopie_desc)).setText(getString(R.string.guthaben_list_kopiecl_desc).replace("%", FormatPrice(prices[5]) + "€"));
        ((TextView) findViewById(R.id.guthaben_list_water_desc)).setText(getString(R.string.guthaben_list_getrwasser_des).replace("%", FormatPrice(prices[6]) + "€"));
        ((TextView) findViewById(R.id.guthaben_list_cola_desc)).setText(getString(R.string.guthaben_list_getrcola_desc).replace("%", FormatPrice(prices[7]) + "€"));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Goto(Pages.StartScreen);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
            Goto(Pages.StartScreen);
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

    public void Refresh(final Updated update)
    {
        this.runOnUiThread(new Runnable() {
            public void run() {
                if(update.IsUpdated(Updated.Mensa) && update.IsUpdated(Updated.Role))
                {
                    int role= update.GetRole();
                    MensaPlan.Day bestDay= update.GetMensaPlan().GetDay(update.GetMensaPlan().GetBestFittingDay());
                    prices[0]= ExtractPrice(bestDay.Menues[MensaPlan.Menues.Menue1].prices[role]);
                    prices[1]= ExtractPrice(bestDay.Menues[MensaPlan.Menues.Menue2].prices[role]);
                    prices[2]= ExtractPrice(bestDay.Menues[MensaPlan.Menues.Menue3].prices[role]);
                    prices[3]= ExtractPrice(bestDay.Menues[MensaPlan.Menues.Buffet].prices[role]);
                    ((TextView) findViewById(R.id.guthaben_list_menue1_desc)).setText(getString(R.string.guthaben_list_menue1_desc).replace("%",FormatPrice(prices[0])+"€"));
                    ((TextView) findViewById(R.id.guthaben_list_menue2_desc)).setText(getString(R.string.guthaben_list_menue2_desc).replace("%",FormatPrice(prices[1])+"€"));
                    ((TextView) findViewById(R.id.guthaben_list_menue3_desc)).setText(getString(R.string.guthaben_list_menue3_desc).replace("%",FormatPrice(prices[2])+"€"));
                    ((TextView) findViewById(R.id.guthaben_list_salad_desc)).setText(getString(R.string.guthaben_list_salad_desc).replace("%",FormatPrice(prices[3]) + "€"));
                }
                if (update.IsUpdated(Updated.Guthaben)) {

                    double guthaben=update.GetCredit();
                    try {
                        ((TextView) findViewById(R.id.guthaben_guthaben)).setText(FormatPrice(guthaben)+"€");
                        ((TextView) findViewById(R.id.guthaben_list_menue1_amount)).setText(String.valueOf((int) (guthaben / prices[0])) + "x");
                        ((TextView) findViewById(R.id.guthaben_list_menue2_amount)).setText(String.valueOf((int)(guthaben/prices[1])) + "x");
                        ((TextView) findViewById(R.id.guthaben_list_menue3_amount)).setText(String.valueOf((int)(guthaben/prices[2])) + "x");
                        ((TextView) findViewById(R.id.guthaben_list_salad_amount)).setText(String.valueOf((int)(guthaben*100/prices[3])) + "g");
                        ((TextView) findViewById(R.id.guthaben_list_swkopie_amount)).setText(String.valueOf((int)(guthaben/prices[4])) + "x");
                        ((TextView) findViewById(R.id.guthaben_list_clkopie_amount)).setText(String.valueOf((int)(guthaben/prices[5])) + "x");
                        ((TextView) findViewById(R.id.guthaben_list_water_amount)).setText(String.valueOf((int)(guthaben/prices[6])) + "x");
                        ((TextView) findViewById(R.id.guthaben_list_cola_amount)).setText(String.valueOf((int)(guthaben/prices[7])) + "x");
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private static double ExtractPrice(String price)
    {
        price=price.replaceAll("€.*", "").replaceAll("[^0-9,]","").replace(',','.');
        return Double.parseDouble(price);
    }

    private static String FormatPrice(double price)
    {
        return String.format("%.2f", price).replace('.',',');
    }

}
