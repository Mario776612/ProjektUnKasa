package com.example.myapplication;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Expandable {
    public String headerName;
    public Lesson[] lessons;
    public boolean isExpanded;

    public Expandable(String headerName, Lesson[] lessons)
    {
        this.headerName = headerName;
        this.lessons = lessons;
        this.isExpanded = false;
    }

    public void createExpandable()
    {
        View sectionView = Globals.mainInflater.inflate(R.layout.section_layout, null);
        Globals.mainContainer.addView(sectionView);

        LinearLayout header = sectionView.findViewById(R.id.headerLayout);
        if(!headerName.isEmpty())
        {
            TextView text = header.findViewById(R.id.headerText);
            text.setText(headerName);
        }
        LinearLayout content = sectionView.findViewById(R.id.expandableContent);
        TextView arrow = sectionView.findViewById(R.id.headerArrow);

        header.setOnClickListener(v -> {
            if (isExpanded) {
                content.setVisibility(View.GONE);
                arrow.setText("▼");
            } else {
                content.setVisibility(VISIBLE);
                arrow.setText("▲");
            }
            isExpanded = !isExpanded;
        });

        for(Lesson button : lessons)
        {
            View row = Globals.mainInflater.inflate(R.layout.item_row, content, false);

            TextView rowText = row.findViewById(R.id.textView);
            rowText.setText(button.title);

            Button buton = row.findViewById(R.id.button);
            buton.setOnClickListener(button.firstClickListener());

            TextView checkmark = row.findViewById(R.id.textView2);

            int visibility;
            if(button.isCompleted())
                visibility = VISIBLE;
            else
                visibility = GONE;

            checkmark.setVisibility(visibility);

            content.addView(row);
        }
    }
}
