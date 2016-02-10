package com.dhbwloerrach.dhbwcampusapp20;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ContentManager {
    private static ContentManager manager;

    private MensaUpdater mensaUpdater;
    private NewsUpdater newsUpdater;
    private MensaPlan mensaPlan;
    private NewsContainer newsContainer;
    private int role;
    private int selectedNewsItem;
    private boolean loaded;

    public ContentManager() {
        mensaUpdater= new MensaUpdater();
        newsUpdater= new NewsUpdater();
        role=-1;
        loaded=false;
        selectedNewsItem=0;
    }

    public static void Initialize(Activity context)
    {
        if(manager==null) {
            manager = new ContentManager();
            manager._LoadFromDatabase(context);
        }
    }

    public static void UpdateFromRemote(Activity context)
    {
        if(manager==null)
            Initialize(context);
        manager._UpdateMensaData(context);
        manager._UpdateNewsData(context);
    }

    public static void UpdateUserRole(Activity context, int role)
    {
        if(manager==null)
            Initialize(context);
        manager._UpdateUserRole(context, role);
    }

    public static void UpdateSelectedNewsItem(int selectedNewsItem)
    {
        manager._UpdateCurrentNewsItem(selectedNewsItem);
    }

    public static void UpdateActivity(Activity context)
    {
        if(manager==null)
            Initialize(context);
        manager._UpdateActivity(context);
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
                        ((Updated.Refreshable) context).Refresh(update);
                    }
                }
            }
        }.start();
    }

    private boolean _IsOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            return true;
        }
        return false;
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
                    ((Updated.Refreshable) context).Refresh(update);
                }
            }
        }.start();
    }

    private void _UpdateCurrentNewsItem(int item)
    {
        this.selectedNewsItem=item;
        newsContainer.SetSelectedItem(selectedNewsItem);
    }
}