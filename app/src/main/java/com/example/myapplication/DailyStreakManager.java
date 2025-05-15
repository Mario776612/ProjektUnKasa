package com.example.myapplication;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class DailyStreakManager {

    public static boolean shouldShowStreakToday(Context context) {
        long lastShown = JSONManager.getLong(context, "lastShown");
        Calendar now = Calendar.getInstance();
        Calendar last = Calendar.getInstance();
        last.setTimeInMillis(lastShown);

        // Jeśli nowa data (dzień) i streak < 7, to pokazujemy
        boolean isNewDay = now.get(Calendar.YEAR) != last.get(Calendar.YEAR)
                || now.get(Calendar.DAY_OF_YEAR) != last.get(Calendar.DAY_OF_YEAR);

        return isNewDay && getStreakCount(context) < 7;
    }

    public static void markStreakShownToday(Context context) {
        try {
            JSONObject root = JSONManager.root(context);
            root.put("lastShown", System.currentTimeMillis());
            root.put("streak", getStreakCount(context)+1);
            JSONManager.save(context, root);
        } catch (JSONException e) {
            Log.i("DailyStreakManager -> markStrikeShownToday", e.toString());
        }
    }

    public static int getStreakCount(Context context) {
        return JSONManager.getInt(context, "streak");
    }

    public static void resetStreak(Context context) {
        try {
            JSONObject root = JSONManager.root(context);
            root.put("lastShown", 0);
            root.put("streak", 0);
            JSONManager.save(context, root);
        } catch (JSONException e) {
            Log.i("DailyStreakManager -> markStrikeShownToday", e.toString());
        }
    }
}