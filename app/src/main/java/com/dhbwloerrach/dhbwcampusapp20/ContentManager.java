package com.dhbwloerrach.dhbwcampusapp20;

import android.app.Activity;
import android.provider.ContactsContract;

public class ContentManager {
    private static ContentManager manager;

    private MensaUpdater mensaUpdater;
    private MensaPlan mensaPlan;
    private int role;
    private boolean loaded;

    public ContentManager() {
        mensaUpdater= new MensaUpdater();
        role=-1;
        loaded=false;
    }

    public static void Initialize(Activity context)
    {
        if(manager==null) {
            manager = new ContentManager();
            manager._LoadFromDatabase(context);
        }
    }

    public static void UpdateMensaData(Activity context)
    {
        manager._UpdateMensaData(context);
    }

    public static void UpdateUserRole(Activity context, int role)
    {
        manager._UpdateUserRole(context, role);
    }

    public static void UpdateActivity(Activity context)
    {
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
                        ((Updated.Refreshable) context).Refresh(update);
                    }
                }
            }
        }.start();
    }

    private void _UpdateMensaData(final Activity context)
    {
        new Thread()
        {
            public void run()
            {
                MensaPlan newplan= mensaUpdater.LoadMensaData();
                if(newplan!=null) {
                    mensaPlan=newplan;
                    DatabaseSocket dbSocket = new DatabaseSocket(context);
                    dbSocket.SaveMensaData(mensaPlan);
                    if(context instanceof Updated.Refreshable)
                    {
                        Updated update= new Updated();
                        update.InsertMensaPlan(mensaPlan);
                        ((Updated.Refreshable) context).Refresh(update);
                    }
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
                    ((Updated.Refreshable) context).Refresh(update);
                }
            }
        }.start();
    }
}


