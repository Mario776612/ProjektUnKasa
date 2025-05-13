package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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

        // Możesz tu dynamicznie zaznaczać w XML dni jako completed w zależności od streak count
        DailyStrikeManager manager = new DailyStrikeManager(this);
        int streak = manager.getStreakCount();

        // Przykład: dynamiczne wypełnianie okienek, np. zmiana tła w zależności od dnia (nie pokazane tutaj)
    }
}