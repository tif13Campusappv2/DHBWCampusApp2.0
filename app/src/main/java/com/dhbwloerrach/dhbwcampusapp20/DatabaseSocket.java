/*
 *      Beschreibung:	Verwaltet die Datenbankzugriffe zum Speichern und Laden.
 *      Autoren: 		Daniel Spieker
 *      Projekt:		Campus App 2.0
 *
 *      ╔══════════════════════════════╗
 *      ║ History                      ║
 *      ╠════════════╦═════════════════╣
 *      ║   Datum    ║    Änderung     ║
 *      ╠════════════╬═════════════════╩══════════════════════════════════════════╗
 *      ║ 2016-02-24 ║ Mensa Datumswert von String zu Long geändert               ║
 *      ║ 20xx-xx-xx ║
 *      ║ 20xx-xx-xx ║
 *      ╚════════════╩════════════════════════════════════════════════════════════╝
 *      Wichtig:           Tabelle sollte mit monospace Schriftart dargestellt werden
 */
package com.dhbwloerrach.dhbwcampusapp20;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import java.util.Calendar;
import java.util.TimeZone;

public class DatabaseSocket extends SQLiteOpenHelper
{
	
	// Stellt einen Satz an Daten für die Mensatabelle bereit
    public static abstract class DatabaseMensa implements BaseColumns
    {
        public static final String TABLE_NAME = "MensaPlan";
        public static final String _ID = "_ID";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_ADDITIONALS = "additionals";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_KIND = "kind";
        public static final String COLUMN_Ken = "kennzeichnungen";
        public static final String COLUMN_Alg = "allergene";
        public static final String COLUMN_PRICE_ST = "price_student";
        public static final String COLUMN_PRICE_PL = "price_pupil";
        public static final String COLUMN_PRICE_GU = "price_guest";
        public static final String COLUMN_PRICE_EM = "price_employee";


        private static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                        _ID + " INTEGER PRIMARY KEY," +
                        COLUMN_DATE + " INTEGER, " +
                        COLUMN_NAME + " TEXT, " +
                        COLUMN_ADDITIONALS + " TEXT, " +
                        COLUMN_KIND + " INTEGER, " +
                        COLUMN_Ken + " TEXT, " +
                        COLUMN_Alg + " TEXT, " +
                        COLUMN_PRICE_ST + " TEXT, " +
                        COLUMN_PRICE_PL + " TEXT, " +
                        COLUMN_PRICE_GU + " TEXT, " +
                        COLUMN_PRICE_EM + " TEXT);";

        private static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + TABLE_NAME +";";

        private static final String SQL_DELETE_CONTENT =
                "DELETE FROM " + TABLE_NAME +";";
    }

	// Stellt einen Satz an Daten für die Newstabelle bereit
    public static abstract class DatabaseNews implements BaseColumns
    {
        public static final String TABLE_NAME = "News";
        public static final String _ID = "_ID";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_CONTENT = "kind";
        public static final String COLUMN_CATEGORIES = "categories";
        public static final String COLUMN_LINK = "link";


        private static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                        _ID + " INTEGER PRIMARY KEY," +
                        COLUMN_DATE + " INTEGER, " +
                        COLUMN_TITLE + " TEXT, " +
                        COLUMN_DESCRIPTION + " TEXT, " +
                        COLUMN_CONTENT + " INTEGER, " +
                        COLUMN_CATEGORIES + " TEXT, " +
                        COLUMN_LINK + " TEXT);";

        private static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + TABLE_NAME +";";

        private static final String SQL_DELETE_CONTENT =
                "DELETE FROM " + TABLE_NAME +";";
    }

	// Stellt einen Satz an Daten für die Nutzertabelle bereit
    public static abstract class DatabaseUserRole implements BaseColumns
    {
        public static final String TABLE_NAME = "UserRole";
        public static final String _ID = "_ID";
        public static final String COLUMN_ROLE = "ROLE";

        private static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                        _ID + " INTEGER PRIMARY KEY," +
                        COLUMN_ROLE + " INTEGER);";

        private static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + TABLE_NAME +";";

        private static final String SQL_DELETE_CONTENT =
                "DELETE FROM " + TABLE_NAME +";";
    }

	// Stellt einen Satz an Daten für die Guthabentabelle bereit
    public static abstract class DatabaseCredit implements BaseColumns
    {
        public static final String TABLE_NAME = "CREDIT";
        public static final String _ID = "_ID";
        public static final String COLUMN_CREDIT = "USERCREDIT";
        public static final String COLUMN_TIME = "TIMESTAMP";

        private static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                        _ID + " INTEGER PRIMARY KEY," +
                        COLUMN_CREDIT + " TEXT," +
                        COLUMN_TIME + " INTEGER);";

        private static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + TABLE_NAME +";";

        private static final String SQL_DELETE_CONTENT =
                "DELETE FROM " + TABLE_NAME +";";
    }

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "CAMPUSAPP";

	// Erstellt ein neues Datenbankobjekt 
    public DatabaseSocket(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

	// Erstellt beim ersten Start alle Tabellen der App
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(DatabaseMensa.SQL_CREATE_ENTRIES);
        db.execSQL(DatabaseUserRole.SQL_CREATE_ENTRIES);
        db.execSQL(DatabaseNews.SQL_CREATE_ENTRIES);
        db.execSQL(DatabaseCredit.SQL_CREATE_ENTRIES);

    }

	// Updatet die Datenbank
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL(DatabaseMensa.SQL_DELETE_ENTRIES);
        db.execSQL(DatabaseUserRole.SQL_DELETE_ENTRIES);
        db.execSQL(DatabaseNews.SQL_DELETE_ENTRIES);
        db.execSQL(DatabaseCredit.SQL_DELETE_ENTRIES);
        onCreate(db);
    }

	// Downgradet die Datenbank
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        onUpgrade(db, oldVersion, newVersion);
    }
    
	// Ersetzt die Menasdaten in der Datenbank mit den übergebenen Daten
    public void SaveMensaData(MensaPlan plan)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.execSQL(DatabaseMensa.SQL_DELETE_CONTENT);
            int rowcounter = 0;
            for (int i = 0; i < plan.GetCountDays(); i++)
                for (int j = 0; j < plan.GetDay(i).Menues.length; j++) {
                    ContentValues values = new ContentValues();
                    values.put(DatabaseMensa._ID, rowcounter++);
                    values.put(DatabaseMensa.COLUMN_DATE, plan.GetDay(i).GetTimeStamp());
                    values.put(DatabaseMensa.COLUMN_ADDITIONALS, plan.GetDay(i).Menues[j].zusatz);
                    values.put(DatabaseMensa.COLUMN_NAME, plan.GetDay(i).Menues[j].Name);
                    values.put(DatabaseMensa.COLUMN_KIND, j);
                    values.put(DatabaseMensa.COLUMN_Ken, plan.GetDay(i).Menues[j].Kennzeichnungen);
                    values.put(DatabaseMensa.COLUMN_Alg, plan.GetDay(i).Menues[j].Allergene);
                    values.put(DatabaseMensa.COLUMN_PRICE_ST, plan.GetDay(i).Menues[j].prices[MensaPlan.Prices.Studenten]);
                    values.put(DatabaseMensa.COLUMN_PRICE_PL, plan.GetDay(i).Menues[j].prices[MensaPlan.Prices.Schueler]);
                    values.put(DatabaseMensa.COLUMN_PRICE_GU, plan.GetDay(i).Menues[j].prices[MensaPlan.Prices.Gaeste]);
                    values.put(DatabaseMensa.COLUMN_PRICE_EM, plan.GetDay(i).Menues[j].prices[MensaPlan.Prices.Mitarbeiter]);
                    db.insert(DatabaseMensa.TABLE_NAME, null, values);
                }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally {
            db.close();
        }
    }

	// Ruft die Mensadaten aus der Datenbank ab
    public MensaPlan GetMensaData()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        MensaPlan plan;
        try {
            Cursor cursor = db.query(DatabaseMensa.TABLE_NAME,new String[] {DatabaseMensa.COLUMN_DATE},null,null,DatabaseMensa.COLUMN_DATE,null,null);
            if(cursor.moveToFirst()) {
                plan=new MensaPlan(cursor.getCount());
                int counter=0;
                do {
                    plan.InsertDay(counter++,new MensaPlan.Day(cursor.getLong(0),new MensaPlan.Menue[]{null,null,null,null}));
                }while(cursor.moveToNext());  //cursor.isLast()
                cursor.close();
            }
            else // Table empty
            {
                cursor.close();
                db.close();
                return null;
            }

            cursor = db.query(DatabaseMensa.TABLE_NAME,new String[] {DatabaseMensa.COLUMN_DATE, DatabaseMensa.COLUMN_ADDITIONALS,DatabaseMensa.COLUMN_NAME, DatabaseMensa.COLUMN_KIND, DatabaseMensa.COLUMN_PRICE_PL,DatabaseMensa.COLUMN_PRICE_ST,DatabaseMensa.COLUMN_PRICE_EM,DatabaseMensa.COLUMN_PRICE_GU, DatabaseMensa.COLUMN_Ken, DatabaseMensa.COLUMN_Alg},null,null,null,null,null);
            if(cursor.moveToFirst()) {
                do {
                    for(int i=0;i<plan.GetCountDays();i++)
                    {
                        if(plan.GetDay(i).GetTimeStamp()==cursor.getLong(0))
                        {
                            String[] prices={"0,00€","0,00€","0,00€","0,00€"};
                            prices[MensaPlan.Prices.Schueler]=cursor.getString(4);
                            prices[MensaPlan.Prices.Studenten]=cursor.getString(5);
                            prices[MensaPlan.Prices.Mitarbeiter]=cursor.getString(6);
                            prices[MensaPlan.Prices.Gaeste]=cursor.getString(7);
                            plan.GetDay(i).Menues[cursor.getInt(3)]= new MensaPlan.Menue(cursor.getString(1),cursor.getString(2), prices,cursor.getString(8),cursor.getString(9));
                            break;
                        }
                    }
                }while(cursor.moveToNext());  //cursor.isLast()
                cursor.close();
            }
            else // Table empty
            {
                cursor.close();
                db.close();
                return null;
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
            db.close();
            return null;
        }
        db.close();
        plan.SortDays();
        return plan;
    }

	// Ersetzt die Newsdaten in der Datenbank mit den übergebenen Daten
    public void SaveNews(NewsContainer newsContainer)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.execSQL(DatabaseNews.SQL_DELETE_CONTENT);
            for (int i = 0; i < newsContainer.GetCountNews(); i++) {
                ContentValues values = new ContentValues();
                values.put(DatabaseNews._ID, i);
                values.put(DatabaseNews.COLUMN_DATE, newsContainer.GetNewsItem(i).GetTimeStamp());
                values.put(DatabaseNews.COLUMN_TITLE, newsContainer.GetNewsItem(i).Title);
                values.put(DatabaseNews.COLUMN_CONTENT, newsContainer.GetNewsItem(i).Content);
                values.put(DatabaseNews.COLUMN_DESCRIPTION, newsContainer.GetNewsItem(i).Description);
                values.put(DatabaseNews.COLUMN_LINK, newsContainer.GetNewsItem(i).Link);
                values.put(DatabaseNews.COLUMN_CATEGORIES, newsContainer.GetNewsItem(i).GetSerializedCategories());
                db.insert(DatabaseNews.TABLE_NAME, null, values);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally {
            db.close();
        }
    }

	// Ruft die Newsdaten aus der Datenbank ab
    public NewsContainer GetNews()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        NewsContainer news;
        try {
            Cursor cursor = db.query(DatabaseNews.TABLE_NAME,new String[] {DatabaseNews.COLUMN_LINK,DatabaseNews.COLUMN_CATEGORIES,DatabaseNews.COLUMN_CONTENT,DatabaseNews.COLUMN_DESCRIPTION,DatabaseNews.COLUMN_DATE,DatabaseNews.COLUMN_TITLE},null,null,null,null,null);
            if(cursor.moveToFirst()) {
                news=new NewsContainer(cursor.getCount());
                int counter=0;
                do {
                    NewsContainer.NewsItem item= new NewsContainer.NewsItem(cursor.getLong(4),cursor.getString(5),cursor.getString(0),cursor.getString(3),cursor.getString(2));
                    String tmp[]=cursor.getString(1).split("\\|");
                    for(String tmpitem:tmp)
                        item.SetCategory(Integer.parseInt(tmpitem));
                    news.IncertNews(item,counter++);
                }while(cursor.moveToNext());  //cursor.isLast()
                cursor.close();
            }
            else // Table empty
            {
                cursor.close();
                db.close();
                return null;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            db.close();
            return null;
        }
        db.close();
        news.SortNews();
        return news;
    }

	// Ersetzt die Benutzerrolle in der Datenbank mit den übergebenen Daten
    public void SaveUserRole(int role)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.execSQL(DatabaseUserRole.SQL_DELETE_CONTENT);
            ContentValues values = new ContentValues();
            values.put(DatabaseUserRole._ID, 0);
            values.put(DatabaseUserRole.COLUMN_ROLE, role);
            db.insert(DatabaseUserRole.TABLE_NAME, null, values);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally {
            db.close();
        }
    }

	// Ruft die Benutzerrolle aus der Datenbank ab
    public int GetUserRole() {
        SQLiteDatabase db = this.getReadableDatabase();
        int role;
        try {
            Cursor cursor = db.query(DatabaseUserRole.TABLE_NAME, new String[]{DatabaseUserRole.COLUMN_ROLE}, null, null, null, null, null);
            if (cursor.moveToFirst()) {
                role=cursor.getInt(0);
                cursor.close();
            } else // Table empty
            {
                cursor.close();
                db.close();
                return 0;
            }
        }
        catch (Exception e)
        {
            db.close();
            e.printStackTrace();
            return 0;
        }
        db.close();
        return role;
    }

	// Ersetzt die Guthabendaten in der Datenbank mit den übergebenen Daten
    public void SaveCredit(CreditContainer container)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.execSQL(DatabaseCredit.SQL_DELETE_CONTENT);
            ContentValues values = new ContentValues();
            values.put(DatabaseCredit._ID, 0);
            values.put(DatabaseCredit.COLUMN_CREDIT, String.valueOf(container.GetCredit()));
            values.put(DatabaseCredit.COLUMN_TIME, container.GetUnformatedDate());
            db.insert(DatabaseCredit.TABLE_NAME, null, values);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally {
            db.close();
        }
    }

	// Ruft die Guthabendaten aus der Datenbank ab
    public CreditContainer GetCredit() {
        SQLiteDatabase db = this.getReadableDatabase();
        CreditContainer container;
        try {
            Cursor cursor = db.query(DatabaseCredit.TABLE_NAME, new String[]{DatabaseCredit.COLUMN_CREDIT, DatabaseCredit.COLUMN_TIME}, null, null, null, null, null);
            if (cursor.moveToFirst()) {
                container=new CreditContainer(Double.parseDouble(cursor.getString(0)), cursor.getLong(1));
                cursor.close();
            } else // Table empty
            {
                cursor.close();
                db.close();
                return new CreditContainer(0.0,Calendar.getInstance(TimeZone.getTimeZone("Europe/Berlin")).getTimeInMillis());
            }
        }
        catch (Exception e)
        {
            db.close();
            e.printStackTrace();
            return new CreditContainer(0.0,Calendar.getInstance(TimeZone.getTimeZone("Europe/Berlin")).getTimeInMillis());
        }
        db.close();
        return container;
    }

}
