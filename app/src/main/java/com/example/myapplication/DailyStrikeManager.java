package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Calendar;

public class DailyStrikeManager {
    private static final String PREF_NAME = "daily_strike_prefs";
    private static final String KEY_LAST_SHOWN = "last_daily_strike_shown";
    private static final String KEY_STREAK_COUNT = "daily_streak_count";

    private final SharedPreferences prefs;

    public DailyStrikeManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public boolean shouldShowStrikeToday() {
        long lastShown = prefs.getLong(KEY_LAST_SHOWN, 0);
        Calendar now = Calendar.getInstance();
        Calendar last = Calendar.getInstance();
        last.setTimeInMillis(lastShown);

        // Jeśli nowa data (dzień) i streak < 7, to pokazujemy
        boolean isNewDay = now.get(Calendar.YEAR) != last.get(Calendar.YEAR)
                || now.get(Calendar.DAY_OF_YEAR) != last.get(Calendar.DAY_OF_YEAR);

        return isNewDay && getStreakCount() < 7;
    }

    public void markStrikeShownToday() {
        prefs.edit()
                .putLong(KEY_LAST_SHOWN, System.currentTimeMillis())
                .putInt(KEY_STREAK_COUNT, Math.min(getStreakCount() + 1, 7))
                .apply();
    }

    public int getStreakCount() {
        return prefs.getInt(KEY_STREAK_COUNT, 0);
    }

    public void resetStreak() {
        prefs.edit()
                .putInt(KEY_STREAK_COUNT, 0)
                .putLong(KEY_LAST_SHOWN, 0)
                .apply();
    }
}
