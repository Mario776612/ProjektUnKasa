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

        backToMenu.setOnClickListener(v -> {
            Intent intent = new Intent(DailyStrikeActivity.this, MainActivity.class);
            intent.putExtra("finished", index);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        DailyStrikeManager manager = new DailyStrikeManager(this);
        int streak = manager.getStreakCount();

        TextView ile = findViewById(R.id.ile);
        ile.setText(String.format("%s %d", ile.getText(), streak));

    }
}