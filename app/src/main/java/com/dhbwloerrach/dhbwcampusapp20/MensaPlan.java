package com.dhbwloerrach.dhbwcampusapp20;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;

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

    public void SortDays()
    {
        long[] tmp1=new long[Days.length];
        Day[] tmp2=new Day[Days.length];
        for(int i=0;i<Days.length;i++)
        {
            tmp1[i]=Days[i].GetTimeStamp();
            tmp2[i]=Days[i];
        }
        Arrays.sort(tmp1);
        for(int i=0;i<tmp2.length;i++)
            for(int j=0;j<tmp1.length;j++)
                if(tmp1[j]==tmp2[i].GetTimeStamp())
                    Days[j]=tmp2[i];
    }


    public static class Day
    {
        private Date Date;
        public Menue Menues[];
        private static final String[] weekdays={"Sonntag","Montag","Dienstag","Mittwoch","Donnerstag","Freitag","Samstag"};
        public Day(String date, Menue menues[])
        {
            Menues=menues;
            Date = Timestamp.valueOf(date);
        }

        public String GetFormatedDate()
        {
            return  weekdays[Date.getDay()] + " " + Date.getDate() + "." + Date.getMonth() + "." + Date.getYear();
        }

        public long GetTimeStamp()
        {
            return Date.getTime();
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
