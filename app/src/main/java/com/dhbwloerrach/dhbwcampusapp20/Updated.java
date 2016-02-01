package com.dhbwloerrach.dhbwcampusapp20;

public class Updated
{

    public static final int Mensa=0, News=1, Guthaben=2;
    private MensaPlan mensa;
    private boolean values[]={false,false,false};

    public Updated()
    {
    }

    public void InsertMensaPlan(MensaPlan mensaplan)
    {
        mensa=mensaplan;
        values[Updated.Mensa]=true;
    }

    public MensaPlan GetMensaPlan()
    {
        return values[Updated.Mensa] ? mensa:null;
    }

    public boolean IsUpdated(int area)
    {
            return area<values.length && area>=0 && values[area];
    }

    public interface Refreshable {
        void Refresh(Updated areas);
    }

}