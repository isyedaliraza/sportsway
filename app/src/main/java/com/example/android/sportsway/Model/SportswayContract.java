package com.example.android.sportsway.Model;

import android.provider.BaseColumns;

public final class SportswayContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private SportswayContract() {}

    /* Inner class that defines the table contents */
    public static class UserEntry implements BaseColumns {
        public static final String TABLE_NAME = "user";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_PASSWORD = "password";
    }

    /* Inner class that defines the table contents */
    public static class EventEntry implements BaseColumns {
        public static final String TABLE_NAME = "event";
        public static final String COLUMN_EVENT_NAME = "event_name";
        public static final String COLUMN_EVENT_LOCATION = "event_location";
        public static final String COLUMN_START_TIME = "event_start_time";
        public static final String COLUMN_EVENT_DATE = "event_date";
    }

    /* Inner class that defines the table contents */
    public static class TicketEntry implements BaseColumns {
        public static final String TABLE_NAME = "ticket";
        public static final String COLUMN_TICKET_CATEGORY = "ticket_category";
        public static final String COLUMN_TICKET_AVAILABLE_TICKETS = "available_tickets";
        public static final String COLUMN_TICKET_PRICE = "ticket_price";
        public static final String COLUMN_EVENT_ID = "event_id";
    }

    /* Inner class that defines the table contents */
    public static class ReservationEntry implements BaseColumns {
        public static final String TABLE_NAME = "reservation";
        public static final String COLUMN_USER_ID = "user_id";
        public static final String COLUMN_EVENT_ID = "event_id";
        public static final String COLUMN_TICKET_ID = "ticket_id";
    }
}