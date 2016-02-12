package com.dhbwloerrach.dhbwcampusapp20;

import java.util.Arrays;
import java.util.Calendar;
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
        for(Day d: tmp2)
            for(int j=0;j<tmp1.length;j++)
                if(tmp1[j]==d.GetTimeStamp())
                    Days[j]=d;
    }

    public Day GetDay(int position)
    {
        return position>=0 && position<Days.length ? Days[position]:null;
    }

    public int GetCountDays()
    {
        return Days.length;
    }

    public int GetBestFittingDay()
    {
        SortDays();
        Calendar now = Calendar.getInstance();
        Date today= new Date(now.get(Calendar.YEAR)-1900,now.get(Calendar.MONTH) ,now.get(Calendar.DAY_OF_MONTH));
        long timestamp=today.getTime();
        int pos=-1;
        for(int i=0;i<Days.length;i++)
            if(Days[i].GetTimeStamp()>=timestamp) {
                pos = i;
                break;
            }
        if(pos==-1)
            pos=Days.length-1;
        return pos;
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

        public String GetUnformatedDate()
        {
            return AddLeadingZeros(Date.getDate(),2) + "." + AddLeadingZeros(Date.getMonth()+1,2) + "." + AddLeadingZeros(Date.getYear()+1900,4);
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

    public static abstract class Prices
    {
        final static int Schueler=0, Studenten=1, Mitarbeiter=2, Gaeste=3;
    }

    public static abstract class Menues
    {
        final static int Menue1=0, Menue2=1, Menue3=2, Buffet=3;
    }

    public static class Menue
    {
        public String zusatz;
        public String Name;
        public String prices[];
        public String Kennzeichnungen;

        public Menue(String zusatz, String Name, String prices[],String kennzeichnungen)
        {
            this.zusatz=zusatz;

            this.Name=Name;
            this.prices=prices;
            this.Kennzeichnungen=kennzeichnungen;
        }

    }

}
