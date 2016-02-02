package com.dhbwloerrach.dhbwcampusapp20;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

class DatabaseSocket extends SQLiteOpenHelper
{
    public static abstract class DatabaseMensa implements BaseColumns
    {
        public static final String TABLE_NAME = "MensaPlan";
        public static final String ID = "ID";
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
                        ID + " INTEGER PRIMARY KEY," +
                        COLUMN_DATE + " TEXT, " +
                        COLUMN_NAME + " TEXT, " +
                        COLUMN_ADDITIONALS + " TEXT, " +
                        COLUMN_KIND + " TEXT, " +
                        COLUMN_PRICE_ST + " TEXT, " +
                        COLUMN_PRICE_PL + " TEXT, " +
                        COLUMN_PRICE_GU + " TEXT, " +
                        COLUMN_PRICE_EM + " TEXT);";

        private static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + TABLE_NAME +";";
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

    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(DatabaseMensa.SQL_CREATE_ENTRIES);
        //db.execSQL(NewsFeed.SQL_CREATE_ENTRIES);

    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL(DatabaseMensa.SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        onUpgrade(db, oldVersion, newVersion);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */
/*
    public void SaveValues(int[] places)
    {
        if(GetContactsCount()==0)
            InsertValues(places);
        else
            UpdateValues(places);
    }

    public int[] GetValues()
    {

        if(GetContactsCount()==0)
        {
            return new int[] {11,4,10};
        }
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                DatabaseTableDefinitions.TABLE_NAME,
                new String[] { DatabaseTableDefinitions.COLUMN_NAME_PLACE1, DatabaseTableDefinitions.COLUMN_NAME_PLACE2, DatabaseTableDefinitions.COLUMN_NAME_PLACE3},
                DatabaseTableDefinitions._ID + "=?",new String[] { String.valueOf(0) },
                null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        int[] selectedChocolates={Integer.parseInt(cursor.getString(0)),Integer.parseInt(cursor.getString(1)),Integer.parseInt(cursor.getString(2))};
        cursor.close();
        return selectedChocolates;
    }

    private void InsertValues(int[] places)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabaseTableDefinitions._ID,0);
        values.put(DatabaseTableDefinitions.COLUMN_NAME_PLACE1,places[0]);
        values.put(DatabaseTableDefinitions.COLUMN_NAME_PLACE2,places[1]);
        values.put(DatabaseTableDefinitions.COLUMN_NAME_PLACE3,places[2]);

        // Inserting Row
        db.insert(DatabaseTableDefinitions.TABLE_NAME, null, values);
        db.close(); // Closing database connection
    }

    private int UpdateValues(int[] places)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabaseTableDefinitions.COLUMN_NAME_PLACE1,places[0]);
        values.put(DatabaseTableDefinitions.COLUMN_NAME_PLACE2,places[1]);
        values.put(DatabaseTableDefinitions.COLUMN_NAME_PLACE3,places[2]);

        // updating row
        return db.update(DatabaseTableDefinitions.TABLE_NAME, values, DatabaseTableDefinitions._ID + " = ?",
                new String[] { String.valueOf(0)});
    }

    private int GetContactsCount()
    {
        String countQuery = "SELECT  * FROM " + DatabaseTableDefinitions.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count=cursor.getCount();
        cursor.close();
        return count;
    }
    */
}