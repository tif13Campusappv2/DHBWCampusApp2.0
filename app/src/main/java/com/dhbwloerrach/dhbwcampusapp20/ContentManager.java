/*
 *      Beschreibung:	Der ContentManager ist für die zentrale Verwaltung der dynamischen Inhalte verantwortlich
 *                      - Er greift auf folgende Komponenten zu:
 *                      - Datenbank
 *                      - News und Mensa Updater
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

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import java.util.Calendar;
import java.util.TimeZone;

public class ContentManager {
    private static ContentManager manager;

    private MensaUpdater mensaUpdater;
    private NewsUpdater newsUpdater;
    private Activity context;
    private MensaPlan mensaPlan;
    private NewsContainer newsContainer;
    private int role;
    private CreditContainer credit;
    private int selectedNewsItem;
    private boolean loaded;

    public ContentManager() {
        mensaUpdater= new MensaUpdater();
        newsUpdater= new NewsUpdater();
        credit= new CreditContainer(0.0);
        role=-1;
        selectedNewsItem=0;
    }

	// Inizialisiert eine neue Instanz des Contentmanager
    public static void Initialize(Activity context)
    {
        manager = new ContentManager();
        manager.context=context;
        manager._LoadFromDatabase(context);
    }

	// Ändert die aktuelle Activity, über die Datenbankaufrufe und Updates gemacht werden. 
    public static void NewContext(Activity context) {
        if(manager==null)
            Initialize(context);
        else
            manager.context=context;
    }

	// Updatet alle Informationen, die online abgerufen werden
    public static void OnlineUpdate()
    {
        if(manager._IsOnline(manager.context)) {
            manager._UpdateMensaData(manager.context);
            manager._UpdateNewsData(manager.context);
        }
        else
        {
            MessageReporting.ShowMessage(MessageReporting.Messages.OFFLINE);
        }

    }

	// Updatet die Rolle des Benutzers
    public static void UpdateUserRole(int role)
    {
        manager._UpdateUserRole(manager.context, role);
    }

	// Updatet das Guthaben des Nutzers. (Der Zeitstempel wird später gesetzt)
    public static void UpdateUserCredit(double credit)
    {
        manager._UpdateUserCredit(manager.context, credit);
    }

	// Ändert das Guthaben um den übergebenden Wert
    public static void ChangeUserCredit(double value,boolean add)
    {

        if(add)
            manager._UpdateUserCredit(manager.context,manager._UserCredit().GetCredit()+value);
        else
            manager._UpdateUserCredit(manager.context,manager._UserCredit().GetCredit()-value);
    }

	// Ändert den zuletzt ausgewählten Artikel
    public static void UpdateSelectedNewsItem(int selectedNewsItem)
    {
        manager._UpdateCurrentNewsItem(selectedNewsItem);
    }

	// Updatet die übergebene Activity
    public static void UpdateActivity(Activity context)
    {
        manager._UpdateActivity(context);
    }

	// Updatet die aktuell gesetzt
    public static void UpdateActivity()
    {
        manager._UpdateActivity(manager.context);
    }

	// Läd alle Daten, die noch nicht geladen wurden aus der Datenbank.
    private void _LoadFromDatabase(final Activity context)
    {
        new Thread()
        {
            public void run()
            {
                DatabaseSocket dbSocket = new DatabaseSocket(context);
                if(mensaPlan==null)
                    mensaPlan = dbSocket.GetMensaData();
                if(role==-1)
                    role= dbSocket.GetUserRole();
                if(newsContainer==null)
                    newsContainer= dbSocket.GetNews();
                if(credit.GetCredit()==0.0)
                    credit= dbSocket.GetCredit();
                loaded=true;
            }
        }.start();
    }

	// Updatet die übergebene Activity
    private void _UpdateActivity(final Activity context)
    {
        new Thread()
        {
            public void run()
            {
                try {
                    while (!loaded)
                        sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    if (context instanceof Updated.Refreshable) {
                        Updated update = new Updated();
                        update.InsertMensaPlan(mensaPlan);
                        update.InsertRole(role);
                        update.InsertNewsContainer(newsContainer);
                        update.InsertCredit(credit);
                        ((Updated.Refreshable) context).Refresh(update);
                    }
                }
            }
        }.start();
    }

	// Prüft ob das Gerät online ist
    private boolean _IsOnline(Context context) {
        NetworkInfo netInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }

	// Versucht aktuelle Mensadaten online abzurufen
    private void _UpdateMensaData(final Activity context)
    {
        new Thread()
        {
            public void run()
            {
                if(_IsOnline(context)) {
                    MensaPlan newplan = mensaUpdater.LoadMensaData();
                    if (newplan != null) {
                        mensaPlan = newplan;
                        DatabaseSocket dbSocket = new DatabaseSocket(context);
                        dbSocket.SaveMensaData(mensaPlan);
                        if (context instanceof Updated.Refreshable) {
                            Updated update = new Updated();
                            update.InsertMensaPlan(mensaPlan);
                            update.InsertCredit(credit);
                            update.InsertRole(role);
                            ((Updated.Refreshable) context).Refresh(update);
                        }
                    }
                }
                else
                {
                    MessageReporting.ShowMessage(MessageReporting.Messages.OFFLINE);
                }
            }
        }.start();
    }

	// Versucht aktuelle Newsdaten online abzurufen
    private void _UpdateNewsData(final Activity context)
    {
        new Thread()
        {
            public void run()
            {
                if(_IsOnline(context)) {
                    NewsContainer newNews = newsUpdater.LoadNewsData();
                    if (newNews != null) {
                        newNews.SetSelectedItem(selectedNewsItem);
                        newsContainer = newNews;
                        DatabaseSocket dbSocket = new DatabaseSocket(context);
                        dbSocket.SaveNews(newsContainer);
                        if (context instanceof Updated.Refreshable) {
                            Updated update = new Updated();
                            update.InsertNewsContainer(newsContainer);
                            ((Updated.Refreshable) context).Refresh(update);
                        }
                    }
                }
                else
                {
                    MessageReporting.ShowMessage(MessageReporting.Messages.OFFLINE);
                }
            }
        }.start();
    }

	// Ändert die Role des Benutzers
    private void _UpdateUserRole(final Activity context, final int role)
    {
        this.role=role;
        new Thread()
        {
            public void run() {
                DatabaseSocket dbSocket = new DatabaseSocket(context);
                dbSocket.SaveUserRole(role);
                if(context instanceof Updated.Refreshable)
                {
                    Updated update= new Updated();
                    update.InsertRole(role);
                    update.InsertMensaPlan(mensaPlan);
                    update.InsertCredit(credit);
                    ((Updated.Refreshable) context).Refresh(update);
                }
            }
        }.start();
    }

	// Ändert das aktuelle Guthaben
    private void _UpdateUserCredit(final Activity context, final double credit)
    {
        long timestamp=Calendar.getInstance(TimeZone.getTimeZone("Europe/Berlin")).getTimeInMillis();
        this.credit= new CreditContainer(credit,timestamp);
        final CreditContainer cr= this.credit;
        new Thread()
        {
            public void run() {
                DatabaseSocket dbSocket = new DatabaseSocket(context);
                dbSocket.SaveCredit(cr);
                if(context instanceof Updated.Refreshable)
                {
                    Updated update= new Updated();
                    update.InsertCredit(cr);
                    ((Updated.Refreshable) context).Refresh(update);
                }
            }
        }.start();
    }

	// Ruft das aktuelle Guthaben ab
    private CreditContainer _UserCredit()
    {
        return this.credit;
    }

	// Ändert den zuletzt gewählte Artikel
    private void _UpdateCurrentNewsItem(int item)
    {
        this.selectedNewsItem=item;
        newsContainer.SetSelectedItem(selectedNewsItem);
    }
}
