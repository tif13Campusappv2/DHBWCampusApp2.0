package com.dhbwloerrach.dhbwcampusapp20;

import android.view.Menu;

public class MensaPlan
{
    private Day Days[];

    public MensaPlan(int numberDays)
    {
        this.Days= new Day[numberDays];
    }
    public void InsertDay(int index, Day day)
    {
        if(index>=0 && index<Days.length)
            Days[index]=day;
    }


    public static class Day
    {
        public String Date;
        public Menue Menues[];

        public Day(String date, Menue menues[])
        {
            this.Date=date;
            Menues=menues;
        }
    }

    public static class Prices
    {
        final static int Schueler=0, Studenten=1, Mitarbeiter=2, Gaeste=3;
    }
    public static class Menues
    {
        final static int Menue1=0, Menue2=1, Menue3=2, Buffet=3;
    }

    public static class Menue
    {
        public String zusatz;
        public String Name;
        public String prices[];

        public Menue(String zusatz, String Name, String prices[])
        {
            this.zusatz=zusatz;
            this.Name=Name;
            this.prices=prices;
        }
    }

}
