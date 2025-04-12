package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private boolean[] isExpanded = new boolean[]{false, false, false};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout container = findViewById(R.id.mainContainer);

        for (int i = 0; i < 3; i++) {
            final int index = i;
            View sectionView = getLayoutInflater().inflate(R.layout.section_layout, null);
            container.addView(sectionView);

            LinearLayout header = sectionView.findViewById(R.id.headerLayout);
            LinearLayout content = sectionView.findViewById(
                    sectionView.getResources().getIdentifier(
                            "expandableContent" + (i + 1),
                            "id",
                            getPackageName()
                    )
            );
            TextView arrow = sectionView.findViewById(R.id.headerArrow);

            header.setOnClickListener(v -> {
                if (isExpanded[index]) {
                    content.setVisibility(View.GONE);
                    arrow.setText("▼");
                } else {
                    content.setVisibility(View.VISIBLE);
                    arrow.setText("▲");
                }
                isExpanded[index] = !isExpanded[index];
            });

            Button button = sectionView.findViewById(R.id.button1);
            button.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                startActivity(intent);
            });
            Button button2 = sectionView.findViewById(R.id.button2);
            button2.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, MainActivity3.class);
                startActivity(intent);
            });
        }
    }
}
