/*
 *      Beschreibung:	Stellt einen Container für News bereit
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class NewsContainer {
    private NewsItem Items[];
    private int selectedItem;

    // Erstellt einen neuen NewsContainer
    public NewsContainer(int numberOfNews)
    {
        Items =new NewsItem[numberOfNews];
    }

    // Fügt ein Newsitem an die gegebene Position in den Container ein
    public void IncertNews(NewsItem item, int positon)
    {
        if(positon>=0 && positon< Items.length)
            Items[positon]=item;
    }

    // Setzt das ausgewählte Newselement
    public void SetSelectedItem(int selectedItem)
    {
        this.selectedItem =selectedItem;
    }

    // Ruft das ausgewählte Newselement ab
    public NewsItem GetSelectedNewsItem()
    {
        return Items[selectedItem];
    }

    // Ruft das Newselement an der übergebenen Position ab
    public NewsItem GetNewsItem(int position)
    {
        if(position>=0 && position< Items.length)
            return  Items[position];
        else
        {
            return new NewsItem(Calendar.getInstance(TimeZone.getTimeZone("Europe/Berlin")).getTimeInMillis(),"Dummy news","www.dhbw-loerrach.de","Dummy news","Dummy news");

        }
    }

    // Ruft eine Liste aller Newselemente ab
    public List<NewsItem> GetNewsItemList()
    {
        // Wichtig: Konvertierung kann nicht in einem Schritt gemacht werden, da es zu Problemen mit der Verwaltung im Newsfragment kam
        List<NewsItem> list= new ArrayList<>();
        for(NewsItem nItem:Items)
            list.add(nItem);
        return list;
    }

    // Ruft eine Liste aller Newselemente ab, die zur übergebenen Kategorie gehören
    public List<NewsItem> GetNewsItemList(int Category)
    {
        List<NewsItem> list= new ArrayList<>();
        for(NewsItem nItem:Items)
            if(nItem.IsCategory(Category))
            list.add(nItem);
        return list;
    }

    // Ruft die Anzahl der gespeicherten Newselemente ab
    public int GetCountNews()
    {
        return Items.length;
    }

    // Sortiert die Newselemente absteigend nach ihrem Datum
    public void SortNews()
    {
        long[] tmp1=new long[Items.length];
        NewsItem[] tmp2=new NewsItem[Items.length];
        for(int i=0;i<Items.length;i++)
        {
            // +i is done to make timestamp unique for elements that are set to the same time
            tmp1[i]=Items[i].GetTimeStamp()+i;
            tmp2[i]=Items[i];
        }
        Arrays.sort(tmp1);
        for(int i=0;i<tmp2.length;i++)
            for(int j=0;j<tmp1.length;j++)
                if(tmp1[j]==tmp2[i].GetTimeStamp()+i)
                    Items[Items.length-1-j]=tmp2[i];
    }

    // Stellt eine Klasse für die Verwaltung eines Newselements bereit
    public static class NewsItem{
        private Date Date;
        public String Title, Link, Description, Content;
        private boolean Categories[];
        // Stellt die Wochentage in der Reihenfolge bereit, wie sie in Java verwaltet werden.
        private static final String[] weekdays={"Sonntag","Montag","Dienstag","Mittwoch","Donnerstag","Freitag","Samstag"};

        // Erstellt ein neues Newselement mit den übergebenen Werten. Nimmt ein Datum als String in der unten gezeigten Form entgegen
        public NewsItem(String date, String Title, String Link, String Description, String Content) {
            //Tue, 12 Jan 2016 11:33:00 +0100
            try {
                Date = new Date(Integer.parseInt(date.substring(12,16)) - 1900, GetMonth(date.substring(8, 11)), Integer.parseInt(date.substring(5, 7)),Integer.parseInt(date.substring(17,19)),Integer.parseInt(date.substring(20,22)),Integer.parseInt(date.substring(23,25)));
            } catch (Exception e) {
                e.printStackTrace();
            }
            Categories= new boolean[] {false,false,false,false};
            this.Title=Title;
            this.Link=Link;
            this.Description=Description;
            this.Content=Content;
        }

        // Erstellt ein neues Newselement mit den übergebenen Werten. Nimmt ein Datum als Zeitstempel entgegen
        public NewsItem(long date, String Title, String Link, String Description, String Content)
        {
            this.Date=new Date(date);
            Categories= new boolean[] {false,false,false,false};
            this.Title=Title;
            this.Link=Link;
            this.Description=Description;
            this.Content=Content;
        }

        // Ruft das Datum des Newselements formatiert ab
        public String GetFormatedDate()
        {
            return  weekdays[Date.getDay()] + " " + AddLeadingZeros(Date.getDate(), 2) + "." + AddLeadingZeros((Date.getMonth()+1),2)+ "." + (Date.getYear()+1900) + " " + AddLeadingZeros(Date.getHours(),2) + ":" + AddLeadingZeros(Date.getMinutes(),2);
        }

        // Fügt dem Newselement eine Kategorie hinzu
        public void SetCategory(int position)
        {
            if(position>=0 && position<Categories.length)
                Categories[position]=true;
        }

        // Prüft ob das Newselement zur übergebenen Kategorie gehört
        public boolean IsCategory(int position){
                return position>=0 && position<Categories.length && Categories[position];
        }

        // Serialisiert die Kategorien des Newselements zum Speichern in der Datenbank
        public String GetSerializedCategories()
        {
            String categories="";
            for(int i=0;i<Categories.length;i++)
            {
                if(Categories[i])
                    categories+="|"+i;
            }
            if(categories.charAt(0)=='|')
                categories=categories.substring(1);
            return categories;
        }

        // Ruft den Zeitstempel des Datums ab
        public long GetTimeStamp()
        {
            return Date.getTime();
        }

        // Füllt die übergebene Zahl auf die angegebene Länge auf
        private String AddLeadingZeros(int value, int length)
        {
            String val=""+value;
            while (val.length()<length)
                val="0" + val;
            return val;
        }

        // Wandelt die von der API übergebenen Monate in den dazugehörenden Zahlenwert um
        private int GetMonth(String Short)
        {
            switch (Short)
            {
                case "Jan":
                    return 0;
                case "Feb":
                    return 1;
                case "Mar":
                    return 2;
                case "Apr":
                    return 3;
                case "May":
                    return 4;
                case "Jun":
                    return 5;
                case "Jul":
                    return 6;
                case "Aug":
                    return 7;
                case "Sep":
                    return 8;
                case "Oct":
                    return 9;
                case "Nov":
                    return 10;
                default:
                    return 11;
            }
        }
    }

    // Stellt eine Enummeration für die Kategorien bereit
    public static class NewsCategories
    {
        public static final int Presse=0, Aktuelles=1,Mitarbeiter=2,Dozierende=3;
    }
}
