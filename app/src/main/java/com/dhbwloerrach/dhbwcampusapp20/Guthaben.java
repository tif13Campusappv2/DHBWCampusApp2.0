/*
 *      Beschreibung:	Beinhaltet allen Code für die Guthabenseite
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

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

public class Guthaben extends AppCompatActivity implements Updated.Refreshable, View.OnClickListener{

    // 0= Mensa Menü 1,1= Mensa Menü 2, 2= Mensa Menü 3, 3= Mensa Salat, 4= SW Kopie, 5= Farbkopie, 6= Wasser, 7= Cola etc., 8=Kaffee
    // Wichtig: 0-4 werden dynamisch gesetzt
    private double prices[]={2.90,3.20,3.60,0.70, 0.04,0.10,1.10,1.50,1.00};
    private double credit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guthaben);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        credit=0.0;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        findViewById(R.id.guthaben_guthaben_add).setOnClickListener(this);
        findViewById(R.id.guthaben_guthaben_sub).setOnClickListener(this);
    }

    @Override
    public void onStart()
    {
        super.onStart();
        ContentManager.NewContext(this);
        MessageReporting.NewContext(this);
        ContentManager.UpdateActivity();
        // Setzt die Preise in der Preisliste, die nicht abhängige von der Benutzerrolle sind
        ((TextView) findViewById(R.id.guthaben_list_swkopie_desc)).setText(getString(R.string.guthaben_list_kopiesw_desc).replace("%", FormatPrice(prices[4])));
        ((TextView) findViewById(R.id.guthaben_list_clkopie_desc)).setText(getString(R.string.guthaben_list_kopiecl_desc).replace("%", FormatPrice(prices[5])));
        ((TextView) findViewById(R.id.guthaben_list_water_desc)).setText(getString(R.string.guthaben_list_getrwasser_des).replace("%", FormatPrice(prices[6])));
        ((TextView) findViewById(R.id.guthaben_list_cola_desc)).setText(getString(R.string.guthaben_list_getrcola_desc).replace("%", FormatPrice(prices[7])));
        ((TextView) findViewById(R.id.guthaben_list_coffee_desc)).setText(getString(R.string.guthaben_list_coffee_desc).replace("%", FormatPrice(prices[8])));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Goto(Pages.StartScreen);
    }

    @Override
    public void onClick(View v)
    {
        int id= v.getId();
        if(id==R.id.guthaben_guthaben_add)
            CreateDialog(true);
        else if(id==R.id.guthaben_guthaben_sub)
            CreateDialog(false);
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

	// Verwaltet die Navigation innerhalb der App
    public void Goto(Pages page)
    {
        if(page== Pages.StartScreen)
        {
            this.overridePendingTransition(R.anim.scale_in, R.anim.right_out);
        }
    }

	// Wird vom ContentManager aufgerufen, um die Inhalte der Activity zu aktualisieren
    public void Refresh(final Updated update)
    {
        this.runOnUiThread(new Runnable() {
            public void run() {
                if(update.IsUpdated(Updated.Mensa) && update.IsUpdated(Updated.Role))
                {
                    int role= update.GetRole();
                    MensaPlan.Day bestDay= update.GetMensaPlan().GetDay(update.GetMensaPlan().GetBestFittingDay());
                    // Ändert die Preise anhand der aktuellen Benutzerrolle
                    prices[0]= ExtractPrice(bestDay.Menues[MensaPlan.Menues.Menue1].prices[role]);
                    prices[1]= ExtractPrice(bestDay.Menues[MensaPlan.Menues.Menue2].prices[role]);
                    prices[2]= ExtractPrice(bestDay.Menues[MensaPlan.Menues.Menue3].prices[role]);
                    prices[3]= ExtractPrice(bestDay.Menues[MensaPlan.Menues.Buffet].prices[role]);
                    // Ändert die Preise in der Preisliste anhand der aktualisierten Daten
                    ((TextView) findViewById(R.id.guthaben_list_menue1_desc)).setText(getString(R.string.guthaben_list_menue1_desc).replace("%",FormatPrice(prices[0])));
                    ((TextView) findViewById(R.id.guthaben_list_menue2_desc)).setText(getString(R.string.guthaben_list_menue2_desc).replace("%",FormatPrice(prices[1])));
                    ((TextView) findViewById(R.id.guthaben_list_menue3_desc)).setText(getString(R.string.guthaben_list_menue3_desc).replace("%",FormatPrice(prices[2])));
                    ((TextView) findViewById(R.id.guthaben_list_salad_desc)).setText(getString(R.string.guthaben_list_salad_desc).replace("%",FormatPrice(prices[3])));
                }
                if (update.IsUpdated(Updated.Guthaben)) {

                    credit=update.GetCredit().GetCredit();
                    try {
						// Ändert die Mengen in der Preisliste anhand des neuen Guthabens
                        ((TextView) findViewById(R.id.guthaben_guthaben)).setText(FormatPrice(credit));
                        ((TextView) findViewById(R.id.guthaben_list_menue1_amount)).setText(String.valueOf((int) (credit / prices[0])) + "x");
                        ((TextView) findViewById(R.id.guthaben_list_menue2_amount)).setText(String.valueOf((int)(credit/prices[1])) + "x");
                        ((TextView) findViewById(R.id.guthaben_list_menue3_amount)).setText(String.valueOf((int)(credit/prices[2])) + "x");
                        ((TextView) findViewById(R.id.guthaben_list_coffee_amount)).setText(String.valueOf((int)(credit/prices[8])) + "x");
                        ((TextView) findViewById(R.id.guthaben_list_salad_amount)).setText(String.valueOf((int)(credit*100/prices[3])) + "g");
                        ((TextView) findViewById(R.id.guthaben_list_swkopie_amount)).setText(String.valueOf((int)(credit/prices[4])) + "x");
                        ((TextView) findViewById(R.id.guthaben_list_clkopie_amount)).setText(String.valueOf((int)(credit/prices[5])) + "x");
                        ((TextView) findViewById(R.id.guthaben_list_water_amount)).setText(String.valueOf((int)(credit/prices[6])) + "x");
                        ((TextView) findViewById(R.id.guthaben_list_cola_amount)).setText(String.valueOf((int)(credit/prices[7])) + "x");
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
	
	// Extrahiert einen Preis aus einem String
    private static double ExtractPrice(String price)
    {
        price=price.replaceAll("€.*", "").replaceAll("[^0-9,]","").replace(',','.');
        return Double.parseDouble(price);
    }

	// Formatiert die übergebene Zahl zu einem Preis
    private static String FormatPrice(double price)
    {
        return String.format("%.2f", price).replace('.', ',').replaceAll("-","") + "€";
    }

	// Erstellt und öffnet den Dialog zur Guthabenänderung
    public void CreateDialog(final boolean add)
    {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            LayoutInflater inflater = this.getLayoutInflater();
            View theView = inflater.inflate(R.layout.guthaben_dialog, null);
            final double guthaben= ((double)Math.round(credit*100))/100;
            final NumberPicker unit_euro = (NumberPicker) theView.findViewById(R.id.euro_picker);
            final NumberPicker cent1 = (NumberPicker) theView.findViewById(R.id.cent1_picker);
            final NumberPicker cent2 = (NumberPicker) theView.findViewById(R.id.cent2_picker);
            builder.setView(theView).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    double guthabenChange= ((double) unit_euro.getValue()) + ((double)(cent1.getValue()*10+cent2.getValue()))/100;
                    // Prüft ob beim Abbuchen genug Geld vorhanden ist
                    if(!add && guthaben<guthabenChange)
                        MessageReporting.ShowMessage(MessageReporting.Messages.MONEY);
                    else
                        ContentManager.ChangeUserCredit(guthabenChange,add);
                }
            }).setNegativeButton(R.string.abourt, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            final Button btn1=(Button) theView.findViewById(R.id.guthaben_dialog_btn1);
            final Button btn2=(Button) theView.findViewById(R.id.guthaben_dialog_btn2);
            final Button btn3=(Button) theView.findViewById(R.id.guthaben_dialog_btn3);
            final Button btn4=(Button) theView.findViewById(R.id.guthaben_dialog_btn4);
            final Button btn5=(Button) theView.findViewById(R.id.guthaben_dialog_btn5);
            // Setzt die Vorauswahlbuttons und Clickhandler anhand der ausgewählten Option (Zu- oder Abbuchen)
            if(add) {
                btn1.setText(FormatPrice(10.00));
                btn1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SetDialogPrice(unit_euro,cent1,cent2,10.00);
                    }
                });
                btn2.setText(FormatPrice(15.00));
                btn2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SetDialogPrice(unit_euro, cent1,cent2, 15.00);
                    }
                });
                btn3.setText(FormatPrice(20.00));
                btn3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SetDialogPrice(unit_euro,cent1,cent2,20.00);
                    }
                });
                btn4.setText(FormatPrice(50.00));
                btn4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SetDialogPrice(unit_euro,cent1,cent2,50.00);
                    }
                });
                btn5.setText(FormatPrice(0.30));
                btn5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SetDialogPrice(unit_euro,cent1,cent2,0.30);
                    }
                });
            }
            else
            {
                btn1.setText(FormatPrice(prices[0]));
                btn1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SetDialogPrice(unit_euro,cent1,cent2,prices[0]);
                    }
                });
                btn2.setText(FormatPrice(prices[1]));
                btn2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SetDialogPrice(unit_euro, cent1,cent2, prices[1]);
                    }
                });
                btn3.setText(FormatPrice(prices[2]));
                btn3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SetDialogPrice(unit_euro,cent1,cent2,prices[2]);
                    }
                });
                btn4.setText(FormatPrice(prices[6]));
                btn4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SetDialogPrice(unit_euro,cent1,cent2,prices[6]);
                    }
                });
                btn5.setText(FormatPrice(prices[7]));
                btn5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SetDialogPrice(unit_euro,cent1,cent2,prices[7]);
                    }
                });
            }

            // Setzt die Werte, die über die Slider eingestellt werden können
            unit_euro.setMinValue(0);
            unit_euro.setMaxValue(100);
            cent1.setMinValue(0);
            cent1.setMaxValue(9);
        cent1.setValue(0);
            cent2.setMinValue(0);
            cent2.setMaxValue(9);
            cent2.setValue(0);
            builder.show();
    }

	// Setzt den übergebenen Geldwert im Dialogfenster 
    private void SetDialogPrice(NumberPicker euro,NumberPicker cent1,NumberPicker cent2, double value)
    {
        int valueeuro=(int) value;
        int valuecent=(int) ((value*100)%100);
        euro.setValue(valueeuro);
        cent1.setValue(valuecent/10);
        cent2.setValue(valuecent %10);

    }

}
