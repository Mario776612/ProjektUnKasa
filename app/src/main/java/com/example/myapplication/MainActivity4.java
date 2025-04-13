package com.example.myapplication;

import static android.icu.text.DisplayContext.LENGTH_SHORT;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

public class MainActivity4 extends AppCompatActivity {

    private boolean clickable = true;
    private String query;
    private void correct(Button correctButton)
    {
        LinearLayout popup_placeholder = findViewById(R.id.answer_popup);
        View popup = getLayoutInflater().inflate(R.layout.correct_popup, popup_placeholder, false);
        Button button = popup.findViewById(R.id.button);
        button.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity4.this, MainActivity4.class);

            String query = getIntent().getStringExtra("query");
            intent.putExtra("query", query);

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(intent);
            finish();
        });
        popup_placeholder.addView(popup);

        correctButton.setBackgroundColor(Color.argb(255, 0, 255, 0));
    }

    private void incorrect(Button selectedButton, Button correctButton)
    {
        LinearLayout popup_placeholder = findViewById(R.id.answer_popup);
        View popup = getLayoutInflater().inflate(R.layout.incorrect_popup, popup_placeholder, false);
        Button button = popup.findViewById(R.id.button);
        button.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity4.this, MainActivity4.class);

            String query = getIntent().getStringExtra("query");
            intent.putExtra("query", query);

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(intent);
            finish();
        });
        correctButton.setBackgroundColor(Color.argb(255, 0, 255, 0));
        selectedButton.setBackgroundColor(Color.argb(255, 255, 0, 0));
        popup_placeholder.addView(popup);


    }

    private void createQuestion(String question, String correct, String[] incorrects)
    {
        ArrayList<String> answers = new ArrayList<>();
        Collections.addAll(answers, incorrects);
        answers.add(correct);
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
        String query = intent.getStringExtra("query");
        this.query = query;

        Cursor cursor = DatabaseHelper.getData(query);

        if (cursor.moveToFirst()) {
            String question = cursor.getString(1);
            String correct = cursor.getString(2);
            String[] incorrects = cursor.getString(3).split("\\|");

            createQuestion(question, correct, incorrects);
            cursor.close();
        }
    }
}