package com.example.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.icu.text.MessageFormat;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DatabaseHelper.init(this);
        initJSON();

        TextView LevelDisplay = findViewById(R.id.textView3);
        int XpPoints = getResources().getInteger(R.integer.XpPoints);
        try {
            LevelDisplay.setText("Level: " + JSONManager.root(this).getInt("level"));
        } catch (JSONException e) {
            Log.e("MainActivity -> onCreate", e.toString());
        }

        Globals.mainContainer = findViewById(R.id.mainContainer);
        Globals.mainInflater = getLayoutInflater();

        Intent previousLessonIntent = getIntent();
        int lessonFinished = previousLessonIntent.getIntExtra("finished", -1);
        if(lessonFinished >= 0)
            finishLesson(lessonFinished);

        Lesson[] inf03 = new Lesson[] {
                new INFLesson(this, "Losowe pytania", -2, "inf03"),
        };

        Expandable inf03_expandable = new Expandable("inf03", inf03);
        inf03_expandable.createExpandable();

        Lesson[] inf04 = new Lesson[] {
                new INFLesson(this, "Losowe pytania", -1, "inf04"),
        };

        Expandable inf04_expandable = new Expandable("inf04", inf04);
        inf04_expandable.createExpandable();

        String query = "SELECT DISTINCT jezyk FROM 'lessons'";
        Cursor cursor = DatabaseHelper.getData(query, DatabaseHelper.DB_NAME_2);
        if(cursor.moveToFirst()) {
            do {
                List<Lesson> lessons = new ArrayList<Lesson>();
                String jezyk = cursor.getString(0);

                String query2 = "SELECT numer FROM 'lessons' WHERE jezyk == '" + jezyk + "'";
                Cursor cursor2 = DatabaseHelper.getData(query2, DatabaseHelper.DB_NAME_2);
                int i = 1;
                if(cursor2.moveToFirst()) {
                    do {
                        int numer = cursor2.getInt(0);
                        String jezykCountQuery = "SELECT COUNT(lesson_id) FROM 'questions' WHERE lesson_id == "+numer;
                        Cursor jezykCountCursor = DatabaseHelper.getData(jezykCountQuery, DatabaseHelper.DB_NAME_2);
                        int count = 0;
                        if(jezykCountCursor.moveToFirst())
                        {
                            count = jezykCountCursor.getInt(0);
                        }
                        QuizLesson lesson = new QuizLesson(this, Integer.toString(i++), numer, count);
                        if(isCompleted(numer))
                            lesson.complete();
                        lessons.add(lesson);
                    } while (cursor2.moveToNext());
                }

                cursor2.close();
                Expandable expandable = new Expandable(jezyk, lessons.toArray(new Lesson[0]));
                expandable.createExpandable();
            } while (cursor.moveToNext());
        }
        cursor.close();

    }

    private void initJSON() {
        String countQuery = "SELECT COUNT(*) FROM 'lessons'";
        Cursor c = DatabaseHelper.getData(countQuery, DatabaseHelper.DB_NAME_2);
        int lessonCounter = 0;
        if(c.moveToFirst())
        {
            lessonCounter = c.getInt(0);
        }

        if(!JSONManager.isInit(this))
            JSONManager.init(this, lessonCounter);
    }

    private boolean isCompleted(int index) {
        JSONArray lessons = JSONManager.array(this, "lessons");
        try {
            return lessons.getBoolean(index-1);
        } catch (JSONException e) {
            Log.e("MainActivity -> isCompleted", e.toString());
            return false;
        }

    }
    private void finishLesson(int index)
    {
        JSONObject root = JSONManager.root(this);
        try {
            JSONArray lessons = root.getJSONArray("lessons");
            lessons.put( index-1,true);

            JSONManager.save(this, root);
        } catch (JSONException e) {
            Log.e("MainActivity -> finishLesson", e.toString());
        }
    }

}
