package com.dhbwloerrach.dhbwcampusapp20;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class mensa_fragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private Boolean IsActive;
    private MensaPlan.Day day;
    private int role;

    public mensa_fragment() {
        IsActive=false;
    }

    public static mensa_fragment newInstance() {
        mensa_fragment fragment = new mensa_fragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
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

    public void UpdateData(MensaPlan.Day day, int role)
    {
        this.day=day;
        this.role=role;
        ShowData();
    }

    private void ShowData()
    {
        if(!this.IsActive || day==null)
            return;
        try {
            ((TextView)(this.getView()).findViewById(R.id.mensa_menue_1_price)).setText(day.Menues[MensaPlan.Menues.Menue1].prices[role]);
            ((TextView)(this.getView()).findViewById(R.id.mensa_menue_1_name)).setText(day.Menues[MensaPlan.Menues.Menue1].Name);
            ((ImageView)(this.getView()).findViewById(R.id.mensa_menue_1_additionals)).setImageResource(GetImageRessource(day.Menues[MensaPlan.Menues.Menue1].zusatz));

            ((TextView)(this.getView()).findViewById(R.id.mensa_menue_2_price)).setText(day.Menues[MensaPlan.Menues.Menue2].prices[role]);
            ((TextView)(this.getView()).findViewById(R.id.mensa_menue_2_name)).setText(day.Menues[MensaPlan.Menues.Menue2].Name);
            ((ImageView)(this.getView()).findViewById(R.id.mensa_menue_2_additionals)).setImageResource(GetImageRessource(day.Menues[MensaPlan.Menues.Menue2].zusatz));

            ((TextView)(this.getView()).findViewById(R.id.mensa_menue_3_price)).setText(day.Menues[MensaPlan.Menues.Menue3].prices[role]);
            ((TextView)(this.getView()).findViewById(R.id.mensa_menue_3_name)).setText(day.Menues[MensaPlan.Menues.Menue3].Name);
            ((ImageView)(this.getView()).findViewById(R.id.mensa_menue_3_additionals)).setImageResource(GetImageRessource(day.Menues[MensaPlan.Menues.Menue3].zusatz));

            ((TextView)(this.getView()).findViewById(R.id.mensa_menue_buffet_price)).setText(day.Menues[MensaPlan.Menues.Buffet].prices[role]);
            ((TextView)(this.getView()).findViewById(R.id.mensa_menue_buffet_name)).setText(day.Menues[MensaPlan.Menues.Buffet].Name);
            ((ImageView)(this.getView()).findViewById(R.id.mensa_menue_buffet_additionals)).setImageResource(GetImageRessource(day.Menues[MensaPlan.Menues.Buffet].zusatz));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

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


    public interface OnFragmentInteractionListener {
       // MensaPlan.Day
    }
}
