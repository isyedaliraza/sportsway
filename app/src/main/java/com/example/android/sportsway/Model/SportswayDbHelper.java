package com.example.android.sportsway.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.example.android.sportsway.Model.SportswayContract.*;

public class SportswayDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Sportsway.db";

    // Create table queries
    private static final String SQL_CREATE_TABLE_USER = "CREATE TABLE " +
            UserEntry.TABLE_NAME + "(" + UserEntry._ID + " INTEGER PRIMARY KEY NOT NULL, " +
            UserEntry.COLUMN_NAME + " TEXT, " + UserEntry.COLUMN_EMAIL + " TEXT NOT NULL, " +
            UserEntry.COLUMN_PASSWORD + " TEXT NOT NULL)";
    private static final String SQL_CREATE_TABLE_EVENT = "CREATE TABLE " +
            EventEntry.TABLE_NAME + "(" + EventEntry._ID + " INTEGER PRIMARY KEY NOT NULL, " +
            EventEntry.COLUMN_EVENT_NAME + " TEXT NOT NULL, " + EventEntry.COLUMN_EVENT_LOCATION +
            " TEXT NOT NULL, " + EventEntry.COLUMN_START_TIME + " TEXT NOT NULL, " + EventEntry.COLUMN_TICKETS +
            " INTEGER NOT NULL)";
    private static final String SQL_CREATE_TABLE_RESERVATION = "CREATE TABLE " + ReservationEntry.TABLE_NAME +
            "(" + ReservationEntry._ID + " INTEGER PRIMARY KEY NOT NULL, " +
            ReservationEntry.COLUMN_USER_ID + " INTEGER NOT NULL, " + ReservationEntry.COLUMN_EVENT_ID +
            " INTEGER NOT NULL, " + ReservationEntry.COLUMN_TICKET_ID + " INTEGER NOT NULL, FOREIGN KEY(" +
            ReservationEntry.COLUMN_USER_ID + ") REFERENCES " + UserEntry.TABLE_NAME + "(" + UserEntry._ID + "), " +
            "FOREIGN KEY(" + ReservationEntry.COLUMN_EVENT_ID + ") REFERENCES " + EventEntry.TABLE_NAME + "(" +
            EventEntry._ID + "), FOREIGN KEY(" + ReservationEntry.COLUMN_TICKET_ID + ") REFERENCES " +
            TicketEntry.TABLE_NAME + "(" + TicketEntry._ID + "))";
    // Delete table queries
    private static final String SQL_DELETE_TABLE_USER = "DROP TABLE IF EXISTS " + UserEntry.TABLE_NAME;
    private static final String SQL_DELETE_TABLE_EVENT = "DROP TABLE IF EXISTS " + EventEntry.TABLE_NAME;
    private static final String SQL_DELETE_TABLE_TICKET = "DROP TABLE IF EXISTS " + TicketEntry.TABLE_NAME;
    private static final String SQL_DELETE_TABLE_RESERVATION = "DROP TABLE IF EXISTS " + ReservationEntry.TABLE_NAME;

    public SportswayDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_USER);
        db.execSQL(SQL_CREATE_TABLE_EVENT);
        db.execSQL(SQL_CREATE_TABLE_RESERVATION);
        // add dummy data below
        ContentValues values = new ContentValues();
        values.put(UserEntry.COLUMN_NAME, "Hamza Khalid");
        values.put(UserEntry.COLUMN_EMAIL, "hamza.khalid@gmail.com");
        values.put(UserEntry.COLUMN_PASSWORD, "admin");
        db.insert(UserEntry.TABLE_NAME, null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_TABLE_USER);
        db.execSQL(SQL_DELETE_TABLE_EVENT);
        db.execSQL(SQL_DELETE_TABLE_TICKET);
        db.execSQL(SQL_DELETE_TABLE_RESERVATION);
        onCreate(db);
    }
}
