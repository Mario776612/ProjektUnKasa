package com.example.myapplication;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Lesson[] lessons;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DatabaseHelper.init(this);

        Globals.mainContainer = findViewById(R.id.mainContainer);
        Globals.mainInflater = getLayoutInflater();

        lessons = new Lesson[]{
              new Lesson("XYZ",
                      v -> {
                              Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                              intent.putExtra("lesson", 0);
                              startActivity(intent);
                            }
                      ),
              new Lesson("XYZ",
                      v -> {
                               Intent intent = new Intent(MainActivity.this, MainActivity3.class);
                               intent.putExtra("lesson", 1);
                               startActivity(intent);
                            }
                      ),
        };

        Intent previousLessonIntent = getIntent();
        int lessonFinished = previousLessonIntent.getIntExtra("finished", -1);
        if(lessonFinished >= 0)
            lessons[lessonFinished].complete();

        Lesson[] inf03 = new Lesson[]
                {
                        new Lesson("Losowe pytania",
                                v -> {
                                    Intent intent = new Intent(MainActivity.this, MainActivity4.class);
                                    intent.putExtra("query", "SELECT * FROM pytania_inf03 ORDER BY RANDOM() LIMIT 1");
                                    startActivity(intent);
                                }),
                };



        Expandable inf03_expandable = new Expandable("inf03", inf03);
        inf03_expandable.createExpandable();

        Expandable lessons_expandable = new Expandable("Lekcje", lessons);
        lessons_expandable.createExpandable();

    }

}
