package com.example.myapplication;

import android.content.Intent;
import android.icu.text.MessageFormat;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class FinishedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finished);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        int correct_tasks = getIntent().getIntExtra("corrects", 0);
        int total_tasks = getIntent().getIntExtra("all", 0);
        int index = getIntent().getIntExtra("lessonIndex", 0);

        TextView info = findViewById(R.id.info);
        info.setText(MessageFormat.format("{0}/{1}", correct_tasks, total_tasks));

        Button continueButton = findViewById(R.id.continueButton);

        continueButton.setOnClickListener(v -> {
            DailyStrikeManager strikeManager = new DailyStrikeManager(FinishedActivity.this);

            if (strikeManager.shouldShowStrikeToday()) {
                strikeManager.markStrikeShownToday();

                Intent intent = new Intent(FinishedActivity.this, DailyStrikeActivity.class);
                intent.putExtra("lessonIndex", index);
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(FinishedActivity.this, MainActivity.class);
                intent.putExtra("finished", index);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

    }
}