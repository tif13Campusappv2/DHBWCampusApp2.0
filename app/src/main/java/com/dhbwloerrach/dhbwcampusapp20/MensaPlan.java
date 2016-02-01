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

    public Day GetDay(int position)
    {
        return position>=0 && position<Days.length ? Days[position]:null;
    }

    public static class Day
    {
        private Date Date;
        public Menue Menues[];
        private static final String[] weekdays={"Sonntag","Montag","Dienstag","Mittwoch","Donnerstag","Freitag","Samstag"};
        public Day(String date, Menue menues[])
        {
            Menues=menues;
            try {
                Date = new Date(Integer.parseInt(date.substring(6))-1900,Integer.parseInt(date.substring(3,5))-1,Integer.parseInt(date.substring(0,2)));
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        public String GetFormatedDate()
        {
            return  weekdays[Date.getDay()] + " " + AddLeadingZeros(Date.getDate(), 2) + "." + AddLeadingZeros((Date.getMonth()+1),2)+ "." ; //+ (Date.getYear()+1900);
        }

        private String AddLeadingZeros(int value, int length)
        {
            String val=""+value;
            while (val.length()<length)
                val="0" + val;
            return val;
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
