package com.dhbwloerrach.dhbwcampusapp20;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class DatabaseSocket extends SQLiteOpenHelper
{
    public static abstract class DatabaseMensa implements BaseColumns
    {
        public static final String TABLE_NAME = "MensaPlan";
        public static final String _ID = "_ID";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_ADDITIONALS = "additionals";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_KIND = "kind";
        public static final String COLUMN_PRICE_ST = "price_student";
        public static final String COLUMN_PRICE_PL = "price_pupil";
        public static final String COLUMN_PRICE_GU = "price_guest";
        public static final String COLUMN_PRICE_EM = "price_employee";


        private static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                        _ID + " INTEGER PRIMARY KEY," +
                        COLUMN_DATE + " TEXT, " +
                        COLUMN_NAME + " TEXT, " +
                        COLUMN_ADDITIONALS + " TEXT, " +
                        COLUMN_KIND + " INTEGER, " +
                        COLUMN_PRICE_ST + " TEXT, " +
                        COLUMN_PRICE_PL + " TEXT, " +
                        COLUMN_PRICE_GU + " TEXT, " +
                        COLUMN_PRICE_EM + " TEXT);";

        private static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + TABLE_NAME +";";

        private static final String SQL_DELETE_CONTENT =
                "DELETE FROM " + TABLE_NAME +";";
    }

    public static abstract class DataBaseUserRole implements BaseColumns
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

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "CAMPUSAPP";

    public DatabaseSocket(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //SQLiteDatabase db = this.getWritableDatabase();
        //onCreate(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(DatabaseMensa.SQL_CREATE_ENTRIES);
        db.execSQL(DataBaseUserRole.SQL_CREATE_ENTRIES);
        //ToDo: db.execSQL(NewsFeed.SQL_CREATE_ENTRIES);
        //ToDo: db.execSQL(Credit.SQL_CREATE_ENTRIES);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL(DatabaseMensa.SQL_DELETE_ENTRIES);
        db.execSQL(DataBaseUserRole.SQL_DELETE_ENTRIES);
        //ToDo: db.execSQL(NewsFeed.SQL_DELETE_ENTRIES);
        //ToDo: db.execSQL(Credit.SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        onUpgrade(db, oldVersion, newVersion);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

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
                    values.put(DatabaseMensa.COLUMN_DATE, plan.GetDay(i).GetUnformatedDate());
                    values.put(DatabaseMensa.COLUMN_ADDITIONALS, plan.GetDay(i).Menues[j].zusatz);
                    values.put(DatabaseMensa.COLUMN_NAME, plan.GetDay(i).Menues[j].Name);
                    values.put(DatabaseMensa.COLUMN_KIND, j);
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
                    plan.InsertDay(counter++,new MensaPlan.Day(cursor.getString(0),new MensaPlan.Menue[]{null,null,null,null}));
                }while(cursor.moveToNext());  //cursor.isLast()
                cursor.close();
            }
            else // Table empty
            {
                cursor.close();
                db.close();
                return null;
            }

            cursor = db.query(DatabaseMensa.TABLE_NAME,new String[] {DatabaseMensa.COLUMN_DATE, DatabaseMensa.COLUMN_ADDITIONALS,DatabaseMensa.COLUMN_NAME, DatabaseMensa.COLUMN_KIND, DatabaseMensa.COLUMN_PRICE_PL,DatabaseMensa.COLUMN_PRICE_ST,DatabaseMensa.COLUMN_PRICE_EM,DatabaseMensa.COLUMN_PRICE_GU},null,null,null,null,null);
            if(cursor.moveToFirst()) {
                do {
                    for(int i=0;i<plan.GetCountDays();i++)
                    {
                        if(plan.GetDay(i).GetUnformatedDate().equals(cursor.getString(0)))
                        {
                            String[] prices={"0,00€","0,00€","0,00€","0,00€"};
                            prices[MensaPlan.Prices.Schueler]=cursor.getString(4);
                            prices[MensaPlan.Prices.Studenten]=cursor.getString(5);
                            prices[MensaPlan.Prices.Mitarbeiter]=cursor.getString(6);
                            prices[MensaPlan.Prices.Gaeste]=cursor.getString(7);
                            plan.GetDay(i).Menues[cursor.getInt(3)]= new MensaPlan.Menue(cursor.getString(1),cursor.getString(2), prices);
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
        return plan;
    }

    public void SaveUserRole(int role)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.execSQL(DataBaseUserRole.SQL_DELETE_CONTENT);
            ContentValues values = new ContentValues();
            values.put(DataBaseUserRole._ID, 0);
            values.put(DataBaseUserRole.COLUMN_ROLE, role);
            db.insert(DatabaseMensa.TABLE_NAME, null, values);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally {
            db.close();
        }
    }

    public int GetUserRole() {
        SQLiteDatabase db = this.getReadableDatabase();
        int role;
        try {
            Cursor cursor = db.query(DataBaseUserRole.TABLE_NAME, new String[]{DataBaseUserRole.COLUMN_ROLE}, null, null, null, null, null);
            if (cursor.moveToFirst()) {
                role=cursor.getInt(0);
                cursor.close();
            } else // Table empty
            {
                cursor.close();
                db.close();
                return -1;
            }
        }
        catch (Exception e)
        {
            db.close();
            e.printStackTrace();
            return -1;
        }
        db.close();
        return role;
    }

}