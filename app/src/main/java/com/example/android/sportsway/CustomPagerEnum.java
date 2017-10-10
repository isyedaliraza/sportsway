package com.example.android.sportsway;

public enum CustomPagerEnum {

    HOME(R.string.home, R.layout.activity_home),
    EVENTS(R.string.events, R.layout.activity_events),
    TICKETS(R.string.tickets, R.layout.activity_tickets);

    private int mTitleResId;
    private int mLayoutResId;

    CustomPagerEnum(int titleResId, int layoutResId) {
        mTitleResId = titleResId;
        mLayoutResId = layoutResId;
    }

    public int getTitleResId() {
        return mTitleResId;
    }

    public int getLayoutResId() {
        return mLayoutResId;
    }

}
