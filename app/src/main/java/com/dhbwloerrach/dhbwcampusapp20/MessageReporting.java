/*
 *      Beschreibung:	Stellt eine Funktion bereit, die zur Nachrichtenausgabe dient.
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
import android.widget.Toast;

public class MessageReporting {
    private static MessageReporting messageReporting;
    private Activity context;

	// Inizialisiert einen neue Instanz des MessageReporters
    public static void Initialize(Activity context)
    {
        messageReporting = new MessageReporting(context);
    }

    // Ändert den Context, in dem Nachrichten angezeigt werden
    public static void NewContext(Activity context)
    {
        if(messageReporting ==null)
            Initialize(context);
        messageReporting._NewContext(context);
    }

    // Zeigt die übergebene Nachricht an
    public static void ShowMessage(int Message)
    {
        messageReporting._ShowMessage(Message);
    }

	// Erstellt eine neue Instanz des Message Reporters
    public MessageReporting(Activity context)
    {
        this.context=context;
    }

	// Ändert den Context, in dem Nachrichten angezeigt werden
    private void _NewContext(Activity context)
    {
        this.context=context;
    }

	// Zeigt die übergebene Nachricht an
    private void _ShowMessage(final int Message)
    {
        if(context!=null)
        context.runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(context, Messages.Messages[Message],Toast.LENGTH_SHORT).show();
            }
        });
    }

	// Stellt eine Enummeration bereit, über die die Nachrichten abegerufen werden können
    public static abstract class Messages
    {
        public static final int NETWORK=0, XML=1, OFFLINE=2,Video=3, MONEY =4;
        private static final int[] Messages ={R.string.ErrorR_Network,R.string.ErrorR_XML,R.string.ErrorR_OFFLINE,R.string.ErrorR_Video,R.string.ErrorR_Money};
    }
}
