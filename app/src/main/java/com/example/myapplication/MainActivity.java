package com.example.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.icu.text.MessageFormat;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DatabaseHelper.init(this);

        TextView LevelDisplay = findViewById(R.id.textView3);
        int XpPoints = getResources().getInteger(R.integer.XpPoints);
        LevelDisplay.setText("Level: " + String.valueOf(XpPoints));


        Globals.mainContainer = findViewById(R.id.mainContainer);
        Globals.mainInflater = getLayoutInflater();

        Lesson[] inf03 = new Lesson[] {
                new INFLesson(this, "Losowe pytania", 0),
        };

        Expandable inf03_expandable = new Expandable("inf03", inf03);
        inf03_expandable.createExpandable();

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
                        lessons.add(lesson);
                    } while (cursor2.moveToNext());
                }

                cursor2.close();

                Expandable expandable = new Expandable(jezyk, lessons.toArray(new Lesson[0]));
                expandable.createExpandable();
            } while (cursor.moveToNext());
        }
        cursor.close();

        Intent previousLessonIntent = getIntent();
        int lessonFinished = previousLessonIntent.getIntExtra("finished", -1);
        //if(lessonFinished >= 0)
            //lessons[lessonFinished].complete();


    }

}
