package com.ingeniousat.com.attendancetrackerr;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by Andy on 7/31/2016.
 */
public class DBHandler extends SQLiteOpenHelper{
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "reminderDB.db";
    private static final String TABLE_CONTACTS = "reminder";
    public static final String COLUMN_DATE = "_date";
    public static final String COLUMN_CONTEXT = "_context";
    public static final String COLUMN_TIME = "_time";
    public static final String COLUMN_NAME = "_name";
    public static final String COLUMN_ID = "_id";

    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_PRODUCTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "(" + COLUMN_ID + " INTEGER PRIMARY KEY," + COLUMN_DATE + " TEXT," + COLUMN_NAME + " TEXT," + COLUMN_TIME + " TEXT," + COLUMN_CONTEXT + " TEXT)";
        sqLiteDatabase.execSQL(CREATE_PRODUCTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void addReminder(Reminder reminder) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, reminder.getName());
        values.put(COLUMN_DATE, reminder.getDate());
        values.put(COLUMN_TIME, reminder.getTime());
        values.put(COLUMN_CONTEXT, reminder.getContext());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_CONTACTS, null, values);
        db.close();
    }

    public ArrayList<Reminder> allReminders() {
        String query = "SELECT * FROM " + TABLE_CONTACTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        ArrayList<Reminder> allReminders = new ArrayList<>();


        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                int id = Integer.parseInt(cursor.getString(0));
                String date = cursor.getString(1);
                String name = cursor.getString(2);
                String time = cursor.getString(3);
                String context = cursor.getString(4);

                Reminder reminder = new Reminder(id,date,name,time,context);

                allReminders.add(reminder);
                cursor.moveToNext();
            }
        }
        cursor.close();
        return allReminders;
    }
    public boolean deleteContent(int reminderID) {
        boolean result = false;

        String query = "SELECT * FROM " + TABLE_CONTACTS + " WHERE " + COLUMN_ID + " =  \"" + Integer.toString(reminderID) + "\"";
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query,null);

        Reminder reminder = new Reminder();

        if (cursor.moveToFirst()) {
            reminder.setID(Integer.parseInt(cursor.getString(0)));
            db.delete(TABLE_CONTACTS, COLUMN_ID + " = ?", new String[] { String.valueOf(reminder.getID())});
            cursor.close();
            result = true;
        }

        db.close();
        return result;
    }

    public void editReminder(int id, String date, String name, String time, String context) {
        deleteContent(id);
        Reminder newReminder = new Reminder(id, date, name, time, context);
        addReminder(newReminder);
    }
}
