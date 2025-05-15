package com.example.myapplication;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class JSONManager {
    public static final String USER_DATA_NAME = "user_data.json";

    public static void save(Context context, JSONObject root) {
        try {
            FileOutputStream fos = context.openFileOutput(USER_DATA_NAME, Context.MODE_PRIVATE);
            OutputStreamWriter writer = new OutputStreamWriter(fos);
            writer.write(root.toString());
            writer.close();
        } catch (Exception e) {
            Log.e("JSONManager -> save", e.toString());
        }
    }

    public static void generateQuests(Context context) {
        try {
            JSONObject root = root(context);
            JSONArray quests = new JSONArray();

            int streakGoal = (int)(Math.random() * 7) + 1;
            int tasksGoal = (int)(Math.random() * 10) + 1;
            int minutesGoal = (int)(Math.random() * 11) + 10;

            quests.put(new JSONObject()
                    .put("type", "streak")
                    .put("goal", streakGoal)
                    .put("completed", false));

            quests.put(new JSONObject()
                    .put("type", "tasks")
                    .put("goal", tasksGoal)
                    .put("completed", false));

            quests.put(new JSONObject()
                    .put("type", "minutes")
                    .put("goal", minutesGoal)
                    .put("completed", false));

            root.put("quests", quests);
            save(context, root);
        } catch (JSONException e) {
            Log.e("JSONManager", "generateQuests error", e);
        }
    }
    public static void init(Context context, int lessonCount)
    {
        try {
            JSONObject root = new JSONObject();
            JSONArray lessonsArray = new JSONArray();

            for (int i = 1; i <= lessonCount; i++) {
                lessonsArray.put("unfinished");
            }

            root.put("lessons", lessonsArray);
            root.put("points", 0);
            root.put("streak", 0);
            root.put("lastShown", (long)0);
            root.put("level", 0);
            save(context, root);
        }
        catch(JSONException ex)
        {
            Log.e("JSONManager", ex.toString());
        }
    }

    public static boolean isInit(Context context)
    {
        try {
            return getFile(context, USER_DATA_NAME).length() > 0 && root(context).getJSONArray("lessons").length() > 0;
        }
        catch(JSONException ex)
        {
            return false;
        }
    }

    public static File getFile(Context context, String path)
    {
        return new File(context.getFilesDir(), path);
    }

    public static long getLong(Context context, String name)
    {
        try {
            return root(context).getLong(name);
        } catch (JSONException e) {
            return 0;
        }
    }

    public static int getInt(Context context, String name)
    {
        try {
            return root(context).getInt(name);
        } catch (JSONException e) {
            return 0;
        }
    }

    public static JSONObject load(Context context) {
        try {
            File file = new File(context.getFilesDir(), USER_DATA_NAME);
            if (!file.exists()) {
                return new JSONObject();
            }

            InputStream inputStream = context.openFileInput(USER_DATA_NAME);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();

            String jsonString = new String(buffer, StandardCharsets.UTF_8);
            return new JSONObject(jsonString);
        } catch (IOException | JSONException e) {
            Log.e("JSONManager -> load", e.toString());
            return new JSONObject();
        }
    }

    public static JSONObject root(Context context) {
        File file = new File(context.getFilesDir(), USER_DATA_NAME);
        if (file.exists()) {
            return load(context);
        } else {
            try {
                InputStream inputStream = context.getAssets().open(USER_DATA_NAME);
                int size = inputStream.available();
                byte[] buffer = new byte[size];
                inputStream.read(buffer);
                inputStream.close();

                String jsonString = new String(buffer, StandardCharsets.UTF_8);
                return new JSONObject(jsonString);
            } catch (IOException | JSONException e) {
                Log.e("JSONManager -> root fallback", e.toString());
                return new JSONObject();
            }
        }
    }


    public static JSONArray array(Context context, String name)
    {
        try {
            return root(context).getJSONArray(name);
        } catch (JSONException e) {
            return new JSONArray();
        }
    }

    public static JSONArray getQuests(Context context) {
        try {
            return root(context).getJSONArray("quests");
        } catch (JSONException e) {
            return new JSONArray();
        }
    }

    public static void completeQuest(Context context, int index) {
        try {
            JSONObject root = root(context);
            JSONArray quests = root.getJSONArray("quests");
            JSONObject quest = quests.getJSONObject(index);
            quest.put("completed", true);
            save(context, root);
        } catch (JSONException e) {
            Log.e("JSONManager", "completeQuest error", e);
        }
    }
}
