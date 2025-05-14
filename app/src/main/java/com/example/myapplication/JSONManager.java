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
    public static void init(Context context, int lessonCount)
    {
        try {
            JSONObject root = root(context);
            JSONArray lessonsArray = new JSONArray();

            for (int i = 1; i <= lessonCount; i++) {
                lessonsArray.put(false);
            }

            root.put("lessons", lessonsArray);
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


    public static JSONArray array(Context context)
    {
        try {
            return root(context).getJSONArray("lessons");
        } catch (JSONException e) {
            return new JSONArray();
        }
    }
}
