package com.example.myapplication;

import static android.icu.text.DisplayContext.LENGTH_SHORT;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.icu.text.MessageFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class MainActivity4 extends AppCompatActivity {

    private boolean clickable = true;
    private boolean isINF;
    private int lessonNumber;
    private int initialTaskNumber;
    private int taskNumber;
    private int taskCount;
    private int correctTasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main4);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        String type = intent.getStringExtra("type");
        correctTasks = getIntent().getIntExtra("corrects", 0);
        taskCount = intent.getIntExtra("taskCount", 0);

        if(type != null && type.equals("inf03")) {
            isINF = true;
            questionsINF("inf03");
        }
        else if(type != null && type.equals("inf04")) {
            isINF = true;
            questionsINF("inf04");
        }
        else if(type != null && type.equals("lesson"))
        {
            isINF = false;
            lessonNumber = intent.getIntExtra("lessonNumber", -1);

            String numberQuery = "SELECT id FROM 'questions' WHERE lesson_id == "+lessonNumber;
            Cursor numberCursor = DatabaseHelper.getData(numberQuery, DatabaseHelper.DB_NAME_2);
            int num=1;
            if(numberCursor.moveToFirst())
            {
                num = numberCursor.getInt(0);
                initialTaskNumber = num;
            }
            taskNumber = intent.getIntExtra("taskNumber", num);
            questionsLesson(taskNumber, lessonNumber);
        }
    }
    private void correct(Button correctButton)
    {
        LinearLayout popup_placeholder = findViewById(R.id.answer_popup);
        View popup = getLayoutInflater().inflate(R.layout.correct_popup, popup_placeholder, false);
        Button button = popup.findViewById(R.id.button);
        button.setOnClickListener(v -> click(true));

        popup_placeholder.addView(popup);

        correctButton.setBackgroundColor(Color.argb(255, 0, 255, 0));
    }

    private void incorrect(Button selectedButton, Button correctButton)
    {
        LinearLayout popup_placeholder = findViewById(R.id.answer_popup);
        View popup = getLayoutInflater().inflate(R.layout.incorrect_popup, popup_placeholder, false);
        Button button = popup.findViewById(R.id.button);
        button.setOnClickListener(v -> click(false));

        correctButton.setBackgroundColor(Color.argb(255, 0, 255, 0));
        selectedButton.setBackgroundColor(Color.argb(255, 255, 0, 0));
        popup_placeholder.addView(popup);


    }

    private void click(boolean addCorrect)
    {
        if(addCorrect)
            correctTasks++;
        if(taskCount != 0 && taskNumber >= taskCount+initialTaskNumber-1)
        {
            Intent intent = new Intent(MainActivity4.this, FinishedActivity.class);

            intent.putExtra("corrects", correctTasks);
            intent.putExtra("all", taskCount);
            intent.putExtra("lessonIndex", lessonNumber);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(intent);
            finish();
            return;
        }
        Intent intent = new Intent(MainActivity4.this, MainActivity4.class);

        String type = getIntent().getStringExtra("type");
        intent.putExtra("type", type);
        if(!isINF) {
            intent.putExtra("lessonNumber", lessonNumber);
            intent.putExtra("taskNumber", taskNumber+1);
            intent.putExtra("taskCount", taskCount);
        }
        intent.putExtra("corrects", correctTasks);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(intent);
        finish();
    }

    private void createQuestion(String question, String correct, String[] all)
    {
        ArrayList<String> answers = new ArrayList<>();
        Collections.addAll(answers, all);
        answers.sort(String::compareToIgnoreCase);

        TextView question_text = findViewById(R.id.textView);
        question_text.setText(question);

        Button button1 = findViewById(R.id.button1);
        Button button2 = findViewById(R.id.button2);
        Button button3 = findViewById(R.id.button3);
        Button button4 = findViewById(R.id.button4);

        button1.setText(answers.get(0));
        button2.setText(answers.get(1));
        button3.setText(answers.get(2));
        button4.setText(answers.get(3));

        Button[] buttons = new Button[]{button1, button2, button3, button4};

        Optional<Button> correctButtonMaybe = Arrays.stream(buttons)
                .filter(button -> button.getText().equals(correct))
                .findFirst();

        Button correctButton = correctButtonMaybe.get();

        for(Button button : buttons)
        {
            button.setOnClickListener(v -> {
                if(!clickable) return;
                clickable = false;
                if(button.equals(correctButton))
                {
                    correct(button);
                }
                else
                {
                    incorrect(button, correctButton);
                }
            });
        }
    }

    private void questionsLesson(int taskNumber, int lessonNumber)
    {

        String query = "SELECT pytanie, odpowiedzi, poprawna_odpowiedz FROM 'questions' WHERE lesson_id == "+lessonNumber+" AND id == "+ taskNumber;
        Cursor cursor = DatabaseHelper.getData(query, DatabaseHelper.DB_NAME_2);

        if (cursor.moveToFirst()) {
            String question = cursor.getString(0);
            String correct = cursor.getString(2);
            String[] all = formatAnswers(cursor.getString(1));

            createQuestion(question, correct, all);
            cursor.close();
        }
    }

    private void questionsINF(String inf)
    {
        String query = "SELECT * FROM pytania_"+inf+" ORDER BY RANDOM() LIMIT 1";
        Cursor cursor = DatabaseHelper.getData(query, DatabaseHelper.DB_NAME_1);

        if (cursor.moveToFirst()) {
            String question = cursor.getString(1);
            String correct = cursor.getString(2);
            String[] incorrect = cursor.getString(3).split("\\|");

            List<String> list = new ArrayList<>(Arrays.asList(incorrect));
            list.add(correct);
            String[] all = list.toArray(new String[0]);

            createQuestion(question, correct, all);
            cursor.close();
        }
    }

    private String[] formatAnswers(String input) {
        try {
            JSONArray jsonArray = new JSONArray(input);
            String[] result = new String[jsonArray.length()];
            for (int i = 0; i < jsonArray.length(); i++) {
                result[i] = jsonArray.getString(i);
            }
            return result;
        } catch (JSONException e) {
            e.printStackTrace();
            return new String[0];
        }
    }
}