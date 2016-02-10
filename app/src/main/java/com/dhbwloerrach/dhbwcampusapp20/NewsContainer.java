package com.dhbwloerrach.dhbwcampusapp20;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class NewsContainer {
    private NewsItem Items[];

    public NewsContainer(int numberOfNews)
    {
        Items =new NewsItem[numberOfNews];
    }

    public void IncertNews(NewsItem item, int positon)
    {
        if(positon>=0 && positon< Items.length)
            Items[positon]=item;
    }

    public NewsItem GetNewsItem(int position)
    {
        return position>=0 && position< Items.length ? Items[position]:null;
    }

    public List<NewsItem> GetNewsItemList()
    {
        return (Arrays.asList(Items));
    }

    public List<NewsItem> GetNewsItemList(int Category)
    {
        List<NewsItem> list= new ArrayList<NewsItem>();
        for(int i=0;i<Items.length;i++)
            if(Items[i].IsCategory(Category))
            list.add(Items[i]);
        return list;
    }

    public int GetCountNews()
    {
        return Items.length;
    }

    public void SortNews()
    {
        long[] tmp1=new long[Items.length];
        NewsItem[] tmp2=new NewsItem[Items.length];
        for(int i=0;i<Items.length;i++)
        {
            tmp1[i]=Items[i].GetTimeStamp();
            tmp2[i]=Items[i];
        }
        Arrays.sort(tmp1);
        for(int i=0;i<tmp2.length;i++)
            for(int j=0;j<tmp1.length;j++)
                if(tmp1[j]==tmp2[i].GetTimeStamp())
                    Items[Items.length-1-j]=tmp2[i];
    }

    public static class NewsItem{
        private Date Date;
        public String Title, Link, Description, Content;
        private boolean Categories[];

        private static final String[] weekdays={"Sonntag","Montag","Dienstag","Mittwoch","Donnerstag","Freitag","Samstag"};

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

        public NewsItem(long date, String Title, String Link, String Description, String Content)
        {
            this.Date=new Date(date);
            Categories= new boolean[] {false,false,false,false};
            this.Title=Title;
            this.Link=Link;
            this.Description=Description;
            this.Content=Content;
        }

        public String GetFormatedDate()
        {
            return  weekdays[Date.getDay()] + " " + AddLeadingZeros(Date.getDate(), 2) + "." + AddLeadingZeros((Date.getMonth()+1),2)+ "." ; //+ (Date.getYear()+1900);
        }

        public void SetCategory(int position)
        {
            if(position>=0 && position<Categories.length)
                Categories[position]=true;
        }

        public boolean IsCategory(int position){
                return position>=0 && position<Categories.length && Categories[position];
        }

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

        public long GetTimeStamp()
        {
            return Date.getTime();
        }

        private String AddLeadingZeros(int value, int length)
        {
            String val=""+value;
            while (val.length()<length)
                val="0" + val;
            return val;
        }

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

    public static class NewsCategories
    {
        public static final int Presse=0, Aktuelles=1,Mitarbeiter=2,Dozierende=3;
    }
}
