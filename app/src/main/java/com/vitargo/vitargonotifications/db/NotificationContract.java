package com.vitargo.vitargonotifications.db;

import android.provider.BaseColumns;

public class NotificationContract {
    private NotificationContract() {}

    public static class NotificationList implements BaseColumns {
        public static final String TABLE_NAME = "notifications";
        public static final String COLUMN_TEXT = "text";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_DATESTAMP = "date";
    }
}
