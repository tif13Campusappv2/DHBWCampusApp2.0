/*
 *      Beschreibung:	Der CreditContainer stellt einen Container für das aktuelle Guthaben des Nutzers und dem Zeitstempel bereit
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

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class CreditContainer {
    private double _credit;
    private Date _timestamp;

	// Erstellt einen neuen Container mit den übergebenen Werten
    public CreditContainer(double credit, long timestamp)
    {
        this._credit=credit;
        this._timestamp= new Date(timestamp);
    }

	// Erstellt einen neuen Container, desen Zeitstempel auf die aktuelle Zeit gesetzt wird.
    public CreditContainer(double credit)
    {
        this._credit=credit;
        this._timestamp= new Date(Calendar.getInstance(TimeZone.getTimeZone("Europe/Berlin")).getTimeInMillis());
    }

	// Ruft das aktuelle Guthaben der Nutzer ab
    public double GetCredit()
    {
        return _credit;
    }

	// Ruft das gespeicherte Datum formatiert ab
    public String GetFormatedDate()
    {
        return AddLeadingZeros(_timestamp.getDate(), 2) + "." + AddLeadingZeros((_timestamp.getMonth()+1),2)+ " " + AddLeadingZeros(_timestamp.getHours(),2) + ":" + AddLeadingZeros(_timestamp.getMinutes(),2);
    }

	// Ruft den Zeitstempel des gespeicherten Datum ab
    public long GetUnformatedDate()
    {
        return _timestamp.getTime();
    }

	// Fügt die angegebene Zahl an Nullen vor die übergebene Zahl an.
    private String AddLeadingZeros(int value, int length)
    {
        String val=""+value;
        while (val.length()<length)
            val="0" + val;
        return val;
    }
}
