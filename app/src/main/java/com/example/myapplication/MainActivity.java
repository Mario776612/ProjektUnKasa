package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private boolean[] isExpanded = new boolean[]{false, false, false};

    private void createExpandable(LinearLayout container, int index, String headerName, Pair<String, View.OnClickListener>[] buttons)
    {
        View sectionView = getLayoutInflater().inflate(R.layout.section_layout, null);
        container.addView(sectionView);

        LinearLayout header = sectionView.findViewById(R.id.headerLayout);
        if(!headerName.isEmpty())
        {
            TextView text = header.findViewById(R.id.headerText);
            text.setText(headerName);
        }
        LinearLayout content = sectionView.findViewById(R.id.expandableContent);
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

        for(Pair<String, View.OnClickListener> button : buttons)
        {
            View row = getLayoutInflater().inflate(R.layout.item_row, content, false);

            TextView rowText = row.findViewById(R.id.textView);
            rowText.setText(button.first);

            Button buton = row.findViewById(R.id.button);
            buton.setOnClickListener(button.second);

            content.addView(row);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DatabaseHelper.init(this);

        LinearLayout container = findViewById(R.id.mainContainer);

        Pair<String, View.OnClickListener>[] pairs = new Pair[]{
              new Pair<String, View.OnClickListener>("XYZ", v -> {
                  Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                  startActivity(intent);
              }),
              new Pair<String, View.OnClickListener>("XYZ", v -> {
                   Intent intent = new Intent(MainActivity.this, MainActivity3.class);
                   startActivity(intent);
              }),
        };

        Pair<String, View.OnClickListener>[] inf_03_pairs = new Pair[]{
                new Pair<String, View.OnClickListener>("Losowe pytania", v -> {
                    Intent intent = new Intent(MainActivity.this, MainActivity4.class);
                    intent.putExtra("query", "SELECT * FROM pytania_inf03 ORDER BY RANDOM() LIMIT 1");
                    startActivity(intent);
                }),
        };

        createExpandable(container, 0, "inf 03", inf_03_pairs);

        for (int i = 1; i < 4; i++) {
            createExpandable(container, i, "", pairs);
        }
    }
}
