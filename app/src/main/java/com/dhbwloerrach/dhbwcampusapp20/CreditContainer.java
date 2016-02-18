package com.dhbwloerrach.dhbwcampusapp20;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class CreditContainer {
    private double _credit;
    private Date _timestamp;

    public CreditContainer(double credit, long timestamp)
    {
        this._credit=credit;
        this._timestamp= new Date(timestamp);
    }

    public CreditContainer(double credit)
    {
        this._credit=credit;
        this._timestamp= new Date(Calendar.getInstance(TimeZone.getTimeZone("Europe/Berlin")).getTimeInMillis());
    }

    public double GetCredit()
    {
        return _credit;
    }

    public String GetFormatedDate()
    {
        return AddLeadingZeros(_timestamp.getDate(), 2) + "." + AddLeadingZeros((_timestamp.getMonth()+1),2)+ " " + AddLeadingZeros(_timestamp.getHours(),2) + ":" + AddLeadingZeros(_timestamp.getMinutes(),2);
    }

    public long GetUnformatedDate()
    {
        return _timestamp.getTime();
    }

    private String AddLeadingZeros(int value, int length)
    {
        String val=""+value;
        while (val.length()<length)
            val="0" + val;
        return val;
    }
}
