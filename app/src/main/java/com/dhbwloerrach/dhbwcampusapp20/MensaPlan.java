/*
 *      Beschreibung:	Stellt einen Container für Mensa Daten bereit
 *      Autoren: 		Daniel Spieker
 *      Projekt:		Campus App 2.0
 *
 *      ╔══════════════════════════════╗
 *      ║ History                      ║
 *      ╠════════════╦═════════════════╣
 *      ║   Datum    ║    Änderung     ║
 *      ╠════════════╬═════════════════╩═════════════════════════════════════════════════════════════════════════════════════════════════════════╗
 *      ║ 2016-02-24 ║ Abrufen von nicht existierenden Tagen von NULL auf Dummy geändert um Fehler bei zu wenig abgerufenen Daten zu verhindern  ║
 *      ║            ║ Neuen Konstruktor für Tagesplan hinzugefügt um Timestamps verarbeiten zu können                                           ║
 *      ║ 20xx-xx-xx ║
 *      ║ 20xx-xx-xx ║
 *      ╚════════════╩═══════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╝
 *      Wichtig:           Tabelle sollte mit monospace Schriftart dargestellt werden
 */
package com.dhbwloerrach.dhbwcampusapp20;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class MensaPlan
{
    private Day Days[];
    private static final int showNextDayMenueAfter =14;

	// Erstellt einen neuen MensaPlan Container
    public MensaPlan(int numberDays)
    {
        this.Days= new Day[numberDays];
    }
    
    // Fügt einen neuen Tag in den Mensaplan ein
    public void InsertDay(int index, Day day)
    {
        if(index>=0 && index<Days.length)
            Days[index]=day;
    }

	// Sortiert die gespeicherten Tage aufsteigend
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

	// Ruft den Tag an der übergebenen Position ab
    public Day GetDay(int position)
    {
        if(position>=0 && position<Days.length)
            return Days[position];
        else
        {
            Menue menues[]= new Menue[4];
            menues[0]=new Menue("","Riesenburger", new String[]  {"3,60€","3,60€","3,60€","3,60€"},"","");
            menues[1]=new Menue("","Riesenburger", new String[]  {"3,60€","3,60€","3,60€","3,60€"},"","");
            menues[2]=new Menue("","Riesenburger", new String[]  {"3,60€","3,60€","3,60€","3,60€"},"","");
            menues[3]=new Menue("","Riesenburger", new String[]  {"3,60€","3,60€","3,60€","3,60€"},"","");
            return new Day(Calendar.getInstance(TimeZone.getTimeZone("Europe/Berlin")).getTimeInMillis(),menues);
        }
    }

	// Ruft die Anzahl der gespeicherten Tage ab
    public int GetCountDays()
    {
        return Days.length;
    }

	// Ruft die Position des Tages ab, der am Besten für die aktuelle Anzeige geeignet ist
    public int GetBestFittingDay()
    {
        SortDays();
        long timestampLater=((new Date(Calendar.getInstance(TimeZone.getTimeZone("Europe/Berlin")).getTimeInMillis()- showNextDayMenueAfter *3600000+1)).getTime());
        int pos=-1;
        for(int i=0;i<Days.length;i++)
            if(Days[i].GetTimeStamp()>=timestampLater) {
                pos = i;
                break;
            }
        if(pos==-1)
            pos=Days.length-1;
        return pos;
    }

	// Stellt einen Container für die Mensadaten eines Tages bereit
    public static class Day
    {
        private Date Date;
        public Menue Menues[];
        private static final String[] weekdays={"Sonntag","Montag","Dienstag","Mittwoch","Donnerstag","Freitag","Samstag"};
        
        //  Erstellt einen neuen Tag 
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

        //  Erstellt einen neuen Tag
        public Day(long date, Menue menues[])
        {
            Menues=menues;
            Date = new Date(date);
        }

		// Ruft das gespeicherte Datum formatiert ab
        public String GetFormatedDate()
        {
            return  weekdays[Date.getDay()] + " " + AddLeadingZeros(Date.getDate(), 2) + "." + AddLeadingZeros((Date.getMonth()+1),2)+ "." ; //+ (Date.getYear()+1900);
        }

		// Füllt die übergebene Zahl durch führerende Nulle auf die übergebene Länge
        private String AddLeadingZeros(int value, int length)
        {
            String val=""+value;
            while (val.length()<length)
                val="0" + val;
            return val;
        }
		
		// Ruft den gespeicherte Zeitstempel ab
        public long GetTimeStamp()
        {
            return Date.getTime();
        }
    }

	// Stellt eine Enummeration für die Position der Preise bereit
    public static abstract class Prices
    {
        final static int Schueler=0, Studenten=1, Mitarbeiter=2, Gaeste=3;
    }

	// stellt eine Enummeration für die Postion der Menüs bereit
    public static abstract class Menues
    {
        final static int Menue1=0, Menue2=1, Menue3=2, Buffet=3;
    }

	// Stellt einen Container für ein Menü bereit
    public static class Menue
    {
        public String zusatz;
        public String Name;
        public String prices[];
        public String Kennzeichnungen;
        public String Allergene;

		// Erstellt ein neues Menü
        public Menue(String zusatz, String Name, String prices[],String kennzeichnungen, String Allergene)
        {
            this.zusatz=zusatz;

            this.Name=Name;
            this.prices=prices;
            this.Kennzeichnungen=kennzeichnungen;
            this.Allergene=Allergene;
        }

    }

}
