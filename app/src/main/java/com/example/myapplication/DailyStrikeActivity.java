package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONArray;

public class DailyStrikeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_strike);

        Button backToMenu = findViewById(R.id.backToMenuButton);
        int index = getIntent().getIntExtra("lessonIndex", 0);
        String status = getIntent().getStringExtra("status");

        backToMenu.setOnClickListener(v -> {
            Intent intent = new Intent(DailyStrikeActivity.this, MainActivity.class);
            intent.putExtra("finished", index);
            intent.putExtra("status", status);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        int streak = DailyStreakManager.getStreakCount(this);

        TextView ile = findViewById(R.id.ile);
        ile.setText(String.format("%s %d", ile.getText(), streak));

    }
}