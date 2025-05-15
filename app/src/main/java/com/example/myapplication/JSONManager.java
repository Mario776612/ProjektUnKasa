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

    private static void generateStreak(JSONArray quests, int repetition)
    {
        int streakGoal = (int)(Math.random() * 7) + 1;
        generateN(quests, 0, streakGoal, repetition);
    }

    private static void generateTasks(JSONArray quests, int repetition)
    {
        int tasksGoal = (int)(Math.random() * 10) + 1;

        generateN(quests, 1, tasksGoal, repetition);
    }

    private static void generatePoints(JSONArray quests, int goal, int repetition)
    {
        generateN(quests, 2, goal, repetition);
    }

    private static void generateN(JSONArray quests, int index, int goal, int repetition)
    {
        try {
            if(index == 2)
                quests.put(new JSONObject()
                        .put("type", "points")
                        .put("goal", goal)
                        .put("reward", goal/2)
                        .put("repetition", repetition));
            else if(index == 1)
                quests.put(new JSONObject()
                        .put("type", "tasks")
                        .put("goal", goal)
                        .put("reward", goal*5)
                        .put("repetition", repetition));
            else if(index == 0)
                quests.put(new JSONObject()
                        .put("type", "streak")
                        .put("goal", goal)
                        .put("reward", goal*5)
                        .put("repetition", repetition));
        } catch (JSONException e) {
            Log.e("JSONManager -> generateN", e.toString());
        }
    }
    public static void generateQuests(Context context) {
        try {
            JSONObject root = root(context);
            JSONArray quests = new JSONArray();

            generateStreak(quests, 0);
            generateTasks(quests, 0);
            generatePoints(quests, 100, 0);

            root.put("quests", quests);
            save(context, root);
        } catch (JSONException e) {
            Log.e("JSONManager", "generateQuests error", e);
        }
    }
    public static void init(Context context, int lessonCount)
    {
        try {
            JSONObject root = root(context);
            JSONArray lessonsArray = new JSONArray();

            for (int i = 1; i <= lessonCount; i++) {
                lessonsArray.put(false);
            }

            root.put("lessons", lessonsArray);
            root.put("points", 0);
            root.put("tasksDone", 0);
            root.put("level", 1);
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

    public static JSONArray quests(JSONObject root) {
        try {
            return root.getJSONArray("quests");
        } catch (JSONException e) {
            Log.e("JSONManager -> quests", e.toString());
            return new JSONArray();
        }
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

    public static void add(Context context, JSONObject root, String what, int howMuch)
    {
        try {
            int something = root.getInt(what);
            root.put(what, something+howMuch);
            Log.i("add", (something+howMuch)+"");
            save(context, root);
        } catch (JSONException e) {
            Log.e("JSONManager -> subtractPoints", e.toString());
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
            JSONArray quests = quests(root);
            JSONObject quest = quests.getJSONObject(index);

            add(context, root, "points", quest.getInt("reward"));

            int repetition = quest.getInt("repetition");
            quest.put("repetition", repetition+1);

            if(index == 2)
            {
                int goal = quest.getInt("goal");
                quest.put("goal", goal*2);
            }
            else
            {
                int additionalGoal = (int)(Math.random() * 5) + 1;
                int goal = quest.getInt("goal");
                quest.put("goal", goal*2+additionalGoal);
            }

            save(context, root);
        } catch (JSONException e) {
            Log.e("JSONManager -> completeQuest", e.toString());
        }
    }
}
