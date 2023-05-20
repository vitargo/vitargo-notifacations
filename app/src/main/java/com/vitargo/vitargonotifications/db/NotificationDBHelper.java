package com.vitargo.vitargonotifications.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class NotificationDBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Notifications.db";

    private List<CustomNotification> notifications;

    private static final String SQL_CREATE_NOTIFICATIONS =
            "CREATE TABLE " + NotificationContract.NotificationList.TABLE_NAME + " (" +
                    NotificationContract.NotificationList._ID + " INTEGER PRIMARY KEY," +
                    NotificationContract.NotificationList.COLUMN_TEXT + " TEXT," +
                    NotificationContract.NotificationList.COLUMN_TITLE + " TEXT," +
                    NotificationContract.NotificationList.COLUMN_DATESTAMP + " TEXT)";

    private static final String SQL_DELETE_NOTIFICATIONS =
            "DROP TABLE IF EXISTS " + NotificationContract.NotificationList.TABLE_NAME;

    public NotificationDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        notifications = new ArrayList<>();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_NOTIFICATIONS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_NOTIFICATIONS);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public List<CustomNotification> getAllNotifications() {
        notifications = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + NotificationContract.NotificationList.TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                CustomNotification not = new CustomNotification();
                not.setId(Integer.parseInt(cursor.getString(0)));
                not.setText(cursor.getString(1));
                not.setTitle(cursor.getString(2));
                not.setDate(cursor.getString(3));
                notifications.add(not);
            } while (cursor.moveToNext());
        }
        return notifications;
    }

    public void deleteNotificationByNameAndDate(String title, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + NotificationContract.NotificationList.TABLE_NAME + " WHERE title LIKE \'" + title +"\'" + "AND date LIKE \'" + date + "\'");
    }

    public void deleteAllNotifications() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + NotificationContract.NotificationList.TABLE_NAME);
    }

    public List<CustomNotification> getNotifications() {
        return notifications;
    }
}