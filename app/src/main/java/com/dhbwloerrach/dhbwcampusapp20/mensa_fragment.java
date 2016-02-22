/*
 *      Beschreibung:	Beinhaltet allen Code für einen Tab der Mensaseite
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

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

public class mensa_fragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private Boolean IsActive;
    private MensaPlan.Day day;
    private double credit;
    private int role;

    public mensa_fragment() {
        IsActive=false;
    }

    public static mensa_fragment newInstance() {
        return new mensa_fragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (OnFragmentInteractionListener) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mensa_fragment, container, false);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        try {
            IsActive=true;
            ShowData();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }



    @Override
    public void onPause()
    {
        super.onPause();
        IsActive=false;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

	// Updatet das Fragment
    public void UpdateData(MensaPlan.Day day, int role, double credit)
    {
        this.day=day;
        this.role=role;
        this.credit=credit;
        ShowData();
    }

	// Zeigt die aktuell gespeicherten Daten an
    private void ShowData()
    {
        if(!this.IsActive || day==null)
            return;
        try {
            ((TextView)(this.getView()).findViewById(R.id.mensa_menue_1_price)).setText(day.Menues[MensaPlan.Menues.Menue1].prices[role]);
            ((TextView)(this.getView()).findViewById(R.id.mensa_menue_1_price)).setTextColor(Color.parseColor(credit >= ExtractPrice(day.Menues[MensaPlan.Menues.Menue1].prices[role]) ? "#626465" : "#FF0000"));
            ((TextView)(this.getView()).findViewById(R.id.mensa_menue_1_name)).setText(day.Menues[MensaPlan.Menues.Menue1].Name);
            ((ImageView)(this.getView()).findViewById(R.id.mensa_menue_1_additionals)).setImageResource(GetImageRessource(day.Menues[MensaPlan.Menues.Menue1].zusatz));
            ((TextView)(this.getView()).findViewById(R.id.mensa_menue_1_Ken)).setText(day.Menues[MensaPlan.Menues.Menue1].Kennzeichnungen);
            ((TextView)(this.getView()).findViewById(R.id.mensa_menue_1_All)).setText(day.Menues[MensaPlan.Menues.Menue1].Allergene);

            ((TextView)(this.getView()).findViewById(R.id.mensa_menue_2_price)).setText(day.Menues[MensaPlan.Menues.Menue2].prices[role]);
            ((TextView)(this.getView()).findViewById(R.id.mensa_menue_2_price)).setTextColor(Color.parseColor(credit >= ExtractPrice(day.Menues[MensaPlan.Menues.Menue2].prices[role]) ? "#626465" : "#FF0000"));
            ((TextView)(this.getView()).findViewById(R.id.mensa_menue_2_name)).setText(day.Menues[MensaPlan.Menues.Menue2].Name);
            ((ImageView)(this.getView()).findViewById(R.id.mensa_menue_2_additionals)).setImageResource(GetImageRessource(day.Menues[MensaPlan.Menues.Menue2].zusatz));
            ((TextView)(this.getView()).findViewById(R.id.mensa_menue_2_Ken)).setText(day.Menues[MensaPlan.Menues.Menue2].Kennzeichnungen);
            ((TextView)(this.getView()).findViewById(R.id.mensa_menue_2_All)).setText(day.Menues[MensaPlan.Menues.Menue2].Allergene);

            ((TextView)(this.getView()).findViewById(R.id.mensa_menue_3_price)).setText(day.Menues[MensaPlan.Menues.Menue3].prices[role]);
            ((TextView)(this.getView()).findViewById(R.id.mensa_menue_3_price)).setTextColor(Color.parseColor(credit >= ExtractPrice(day.Menues[MensaPlan.Menues.Menue3].prices[role]) ? "#626465" : "#FF0000"));
            ((TextView)(this.getView()).findViewById(R.id.mensa_menue_3_name)).setText(day.Menues[MensaPlan.Menues.Menue3].Name);
            ((ImageView)(this.getView()).findViewById(R.id.mensa_menue_3_additionals)).setImageResource(GetImageRessource(day.Menues[MensaPlan.Menues.Menue3].zusatz));
            ((TextView)(this.getView()).findViewById(R.id.mensa_menue_3_Ken)).setText(day.Menues[MensaPlan.Menues.Menue3].Kennzeichnungen);
            ((TextView)(this.getView()).findViewById(R.id.mensa_menue_3_All)).setText(day.Menues[MensaPlan.Menues.Menue3].Allergene);

            ((TextView)(this.getView()).findViewById(R.id.mensa_menue_buffet_price)).setText(day.Menues[MensaPlan.Menues.Buffet].prices[role]);
            ((TextView)(this.getView()).findViewById(R.id.mensa_menue_buffet_name)).setText(day.Menues[MensaPlan.Menues.Buffet].Name);
            ((ImageView)(this.getView()).findViewById(R.id.mensa_menue_buffet_additionals)).setImageResource(GetImageRessource(day.Menues[MensaPlan.Menues.Buffet].zusatz));
            ((TextView)(this.getView()).findViewById(R.id.mensa_menue_buffet_Ken)).setText(day.Menues[MensaPlan.Menues.Buffet].Kennzeichnungen);
            ((TextView)(this.getView()).findViewById(R.id.mensa_menue_buffet_All)).setText(day.Menues[MensaPlan.Menues.Buffet].Allergene);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

	// Löst den Zusatz zu einer Bildressource auf
    private int GetImageRessource(String zusatz)
    {
        switch (zusatz)
        {
            case "vegetarisch":
                return R.drawable.veg;
            case "vegan":
                return  R.drawable.vegan;
            default:
                return R.drawable.leer;
        }
    }

	// Extrahiert einen Preis aus einem String
    private static double ExtractPrice(String price)
    {
        price=price.replaceAll("€.*", "").replaceAll("[^0-9,]","").replace(',','.');
        return Double.parseDouble(price);
    }


    public interface OnFragmentInteractionListener {

    }
}
