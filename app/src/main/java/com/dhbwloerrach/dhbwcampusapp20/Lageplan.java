/*
 *      Beschreibung:	Beinhaltet allen Code für die Lageplanseite
 *      Autoren: 		Marius Korioth, Daniel Spieker
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

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

public class Lageplan extends AppCompatActivity implements View.OnClickListener{

    //Initialisiere Variablen
    ImageView lageplanImage;
    TextView lageplanTextview;
    TextView lageplanTextview2;
    TextView lageplanTextview3;
    Button lageplanButton;
    EditText lageplanTextfeld;
    String regex;
    String raum;
    String one;
    String two;
    String three;
    String gebauede;
    String stockwerk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lageplan);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Definitionen von später verwendeten Variablen (
        lageplanButton = (Button) findViewById(R.id.navButtonLageplan); //get Button
        lageplanButton.setOnClickListener(this); // Definition Listener zu Button
        lageplanTextfeld = (EditText) findViewById(R.id.lageplanTextfeld); //get Textfield
        regex ="(?i)[ACDES][0-4]\\d\\d"; //regulärer Ausdruck zur Validierung der Eingabe (Ein Buchstabe, nachfolgend 3 Ziffern)
        lageplanTextview = (TextView) findViewById(R.id.lageplanTextview1); //get Textview1
        lageplanTextview2 = (TextView) findViewById(R.id.lageplanTextview2); //get Textview2
        lageplanTextview3 = (TextView) findViewById(R.id.lageplanTextview3); // get Textview3
        lageplanImage = (ImageView) findViewById(R.id.imageViewLageplan); //get ImageView

        //Textfeld 3 ausblenden
        lageplanTextview3.setVisibility(View.GONE);

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

    public void Goto(Pages page) {
        if (page == Pages.StartScreen) {
            this.overridePendingTransition(R.anim.scale_in, R.anim.right_out);
        }
    }

    //Methode die bei Erkennung ungültiger Eingaben/Raumnummern aufgerufen wird und Fehlermeldung etc. einblendet
    public void InvalidInput() {
        lageplanTextview.setText(R.string.lageplan_error); //Fehlermeldung bei invalider Eingabe ausgeben in Textview1
        lageplanTextview2.setText(R.string.lageplan_textview2); //Blende Informationen zur Korrekten Eingabe von Raumnummern (wieder) ein
        lageplanTextview.setTextColor(Color.parseColor("#FF0000")); //Textfarbe anpassen (auf rot) in Textview1
        lageplanTextview3.setVisibility(View.GONE); //Textfeld ausblenden
    }

    //Methode die bei valider Eingabe/Raumnummer aufgerufen wird um Textausgabe einzublenden
    public void Navigate() {

        lageplanTextview.setText("Gebäude "+gebauede+" - Stockwerk "+stockwerk); //Textausgabe
        lageplanTextview2.setText(R.string.lageplan_textview2_alt); //setze Hinweis auf Markierung im Lageplan
        lageplanTextview3.setText("Raum "+raum); //Textausgabe
        lageplanTextview3. setVisibility(View.VISIBLE); //Textfeld3 wieder einblenden
    }

    @Override
    //OnClickListener zu Button
    public void onClick(View v) {
        lageplanTextview.setTextColor(Color.parseColor("#000000")); //Textfarbe in Textview1 zurücksetzen auf schwarz
        raum = lageplanTextfeld.getText().toString().toUpperCase(); //get Feldwert, konvertiere zu Großbuchstabe für spätere Textausgabe

        //Validierung der Eingabe mittels Regulärem Ausdruck:
        if (!raum.matches(regex)) {
            InvalidInput();}
        else{
            //Wenn Raumnummer valide analysiere weiter
            one = String.valueOf(raum.charAt(0)); //erste Stelle der Eingabe als String
            two = String.valueOf(raum.charAt(1)); //zweite Stelle der Eingabe als String

            //Zielgebäude und Zielstockwerk ermitteln:
            switch (one) {
                //Wenn Raum in einem der S-Gebaude liegt:
                case "S":
                    three = String.valueOf(raum.charAt(2)); //dritte Stelle der Eingabe als String
                    switch (three) {
                        //S-Gebaude identifizieren/Einzelunterscheidung und String für Textausgabe setzen
                        case "1":
                            gebauede = "S1"; //String für Textausgabe
                            lageplanImage.setImageResource(R.drawable.plan_hangstrasse_s1); //Ändere Lageplan/Bild
                            break;
                        case "2":
                            gebauede = "S2"; //String für Textausgabe
                            lageplanImage.setImageResource(R.drawable.plan_hangstrasse_s2); //Ändere Lageplan/Bild
                            break;
                        case "3":
                            gebauede = "S3"; //String für Textausgabe
                            lageplanImage.setImageResource(R.drawable.plan_hangstrasse_s3); //Ändere Lageplan/Bild
                            break;
                        case "4":
                            gebauede = "S4"; //String für Textausgabe
                            lageplanImage.setImageResource(R.drawable.plan_hangstrasse_s4); //Ändere Lageplan/Bild
                            break;
                        case "0": case "5": case "6": case "7": case "8": case "9": InvalidInput(); break; //Invalide Eingabe
                    }
                    switch(two) { //Identifiziere Stockwerk
                        case "0": case "1": stockwerk = two; Navigate(); break; //String für Textausgabe
                        case "2": case "3": case "4": case "5": case "6": case "7": case "8": case "9": InvalidInput(); break;} //ungültige Eingabe
                    break;
                //Wenn Raum in Gebäude A
                case "A":
                    gebauede = one; //String für Textausgabe setzen
                    lageplanImage.setImageResource(R.drawable.plan_hangstrasse_a); //Ändere Lageplan/Bild
                    switch(two) { //Identifiziere Stockwerk
                        case "1": case "2": case "3": stockwerk = two;  Navigate(); break; //String für Textausgabe
                        case "0": case "4": case "5": case "6": case "7": case "8": case "9": InvalidInput(); break;} //ungültige Eingabe
                    break;
                //Wenn Raum in Gebäude C
                case "C":
                    gebauede = one; //String für Textausgabe setzen
                    lageplanImage.setImageResource(R.drawable.plan_hangstrasse_c); //Ändere Lageplan/Bild
                    switch(two) { //Identifiziere Stockwerk
                        case "1": stockwerk = two; Navigate(); break;  //String für Textausgabe
                        case "0": case "2": case "3": case "4": case "5": case "6": case "7": case "8": case "9": InvalidInput(); break;} //ungültige Eingabe
                    break;
                //Wenn Raum in Gebäude D
                case "D":
                    gebauede = one; //String für Textausgabe setzen
                    lageplanImage.setImageResource(R.drawable.plan_hangstrasse_d); //Ändere Lageplan/Bild
                    switch(two) { //Identifiziere Stockwerk
                        case "1": case "2": case "3": stockwerk = two; Navigate(); break; //String für Textausgabe
                        case "0": case "4": case "5": case "6": case "7": case "8": case "9": InvalidInput(); break;} //ungültige Eingabe
                    break;
                //Wenn Raum in Gebäude E:
                case "E":
                    gebauede = one; //String für Textausgabe setzen
                    lageplanImage.setImageResource(R.drawable.plan_hangstrasse_e); //Ändere Lageplan/Bild
                    switch(two) { //Identifiziere Stockwerk
                        case "0": stockwerk = two; Navigate(); break; //String für Textausgabe
                        case "1": case "2": case "3": case "4": case "5": case "6": case "7": case "8": case "9": InvalidInput(); break;} //ungültige Eingabe
                    break;
            }
        }
    }
}
