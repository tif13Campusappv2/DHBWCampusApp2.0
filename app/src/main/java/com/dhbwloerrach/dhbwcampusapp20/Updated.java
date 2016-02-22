/*
 *      Beschreibung:	Stellt ein Objekt bereit, in das alle Daten, die eine Activity zum Updaten des Inhalts braucht speichern und bereitstellen kann
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

public class Updated
{

    public static final int Mensa=0, News=1, Guthaben=2, Role=3;
    private MensaPlan mensa;
    private NewsContainer news;
    private int role;
    private CreditContainer credit;
    private boolean values[]={false,false,false,false};

    // Erstellt ein neues Updateobjekt
    public Updated()
    {
    }

    // Fügt einen Mensaplan in das Updateobjekt ein
    public void InsertMensaPlan(MensaPlan mensaplan)
    {
        if(mensaplan!=null) {
            mensa = mensaplan;
            values[Updated.Mensa] = true;
        }
    }

    // Prüft ob das Updateobjekt einen Mensaplan enthält und ruft diesen ab
    public MensaPlan GetMensaPlan()
    {
        return values[Updated.Mensa] ? mensa:null;
    }

    // Fügt einen Newscontainer in das Updateobjekt ein
    public void InsertNewsContainer(NewsContainer newsContainer)
    {
        if(newsContainer!=null) {
            news= newsContainer;
            values[Updated.News] = true;
        }
    }

    // Prüft ob das Updateobjekt einen Newscontainer enthält und ruft diesen ab
    public NewsContainer GetNews()
    {
        return values[Updated.News] ? news:null;
    }

    // Fügt eine Benuterrolle in das Updateobjekt ein
    public void InsertRole(int role)
    {
        if(role!=-1) {
            this.role = role;
            values[Updated.Role] = true;
        }
    }

    // Prüft ob eine Benutzerrolle im Updateobjekt enthalten ist und ruft diese ab
    public int GetRole()
    {
        return values[Updated.Role] ? role:0;
    }

    // Fügt ein Guthabenobjekt in das Updateobjekt ein
    public void InsertCredit(CreditContainer credit)
    {
        this.credit = credit;
        values[Updated.Guthaben] = true;
    }

    // Prüft ob ein Guthabencontainer im Updateobjekt gespeichert ist und ruft dieses ab
    public CreditContainer GetCredit()
    {
        return values[Updated.Guthaben] ? credit:new CreditContainer(0.0);
    }

    // Prüft ob das übergebene Objekt im Updatecontainer enthalten ist
    public boolean IsUpdated(int area)
    {
            return area<values.length && area>=0 && values[area];
    }

    // Stellt eine Schnittstelle für die Aktualisierung einer Activity bereit
    public interface Refreshable {
        void Refresh(Updated areas);
    }

}