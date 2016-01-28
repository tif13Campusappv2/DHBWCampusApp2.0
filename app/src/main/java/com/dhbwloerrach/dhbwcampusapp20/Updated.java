package com.dhbwloerrach.dhbwcampusapp20;

public class Updated
{

    public static final int Mensa=0, News=1, Guthaben=2;
    private boolean values[]={false,false,false};

    public Updated(boolean mensa, boolean news, boolean guthaben)
    {
        values[Updated.Mensa]=mensa;
        values[Updated.News]=news;
        values[Updated.Guthaben]=guthaben;
    }

    public boolean IsUpdated(int area)
    {
        if(area<values.length && area>=0)
            return values[area];
        return false;
    }

    public interface Refreshable {
        void Refresh(Updated areas);
    }

}