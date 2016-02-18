package com.dhbwloerrach.dhbwcampusapp20;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.Calendar;
import java.util.Date;
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

    public static void Initialize(Activity context)
    {
        manager = new ContentManager();
        manager.context=context;
        manager._LoadFromDatabase(context);
    }

    public static void NewContext(Activity context) {
        if(manager==null)
            Initialize(context);
        else
            manager.context=context;
    }

    public static void OnlineUpdate()
    {
        if(manager._IsOnline(manager.context)) {
            manager._UpdateMensaData(manager.context);
            manager._UpdateNewsData(manager.context);
        }
        else
        {
            ErrorReporting.NewError(ErrorReporting.Errors.OFFLINE);
        }

    }

    public static void UpdateUserRole(int role)
    {
        manager._UpdateUserRole(manager.context, role);
    }

    public static void UpdateUserCredit(double credit)
    {
        manager._UpdateUserCredit(manager.context, credit);
    }

    public static void ChangeUserCredit(double value,boolean add)
    {

        if(add)
            manager._UpdateUserCredit(manager.context,manager._UserCredit().GetCredit()+value);
        else
            manager._UpdateUserCredit(manager.context,manager._UserCredit().GetCredit()-value);
    }

    public static void UpdateSelectedNewsItem(int selectedNewsItem)
    {
        manager._UpdateCurrentNewsItem(selectedNewsItem);
    }

    public static void UpdateActivity(Activity context)
    {
        manager._UpdateActivity(context);
    }

    public static void UpdateActivity()
    {
        manager._UpdateActivity(manager.context);
    }

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

    private boolean _IsOnline(Context context) {
        NetworkInfo netInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }

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
                            update.InsertRole(role);
                            ((Updated.Refreshable) context).Refresh(update);
                        }
                    }
                }
                else
                {
                    ErrorReporting.NewError(ErrorReporting.Errors.OFFLINE);
                }
            }
        }.start();
    }

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
                    ErrorReporting.NewError(ErrorReporting.Errors.OFFLINE);
                }
            }
        }.start();
    }

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

    private CreditContainer _UserCredit()
    {
        return this.credit;
    }

    private void _UpdateCurrentNewsItem(int item)
    {
        this.selectedNewsItem=item;
        newsContainer.SetSelectedItem(selectedNewsItem);
    }
}