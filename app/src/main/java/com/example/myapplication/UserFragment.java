package com.example.myapplication;

import static android.widget.Toast.LENGTH_SHORT;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import org.json.JSONObject;

import java.text.MessageFormat;

public class UserFragment extends Fragment {

    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        context = getContext();

        TextView level = view.findViewById(R.id.textView3);
        int levelNumber = JSONManager.getInt(context, "level");
        level.setText(java.text.MessageFormat.format("Poziom: {0}", levelNumber));

        TextView points = view.findViewById(R.id.points);
        int pointCount = JSONManager.getInt(context, "points");
        points.setText(MessageFormat.format("Punkty: {0}", pointCount));

        Button levelup = view.findViewById(R.id.levelup);
        updateLevelUp(levelup, pointCount, levelNumber*100);
        levelup.setOnClickListener(v -> {
            JSONObject root = JSONManager.root(context);
            JSONManager.add(context, root, "points", levelNumber*(-100));
            JSONManager.add(context, root, "level", 1);
            JSONManager.save(context, root);

            int levelNum = JSONManager.getInt(context, "level");
            int pointCt = JSONManager.getInt(context, "points");
            updateLevelUp(levelup, pointCt, levelNum*100);

            points.setText(MessageFormat.format("Punkty: {0}", pointCt));
            level.setText(MessageFormat.format("Poziom: {0}", levelNum));
            Toast.makeText(context, "Podniesiono poziom o 1!", Toast.LENGTH_SHORT).show();
        });

        Button reset = view.findViewById(R.id.reset);
        reset.setOnClickListener(v -> {
            new AlertDialog.Builder(requireContext())
                    .setTitle("Potwierdzenie")
                    .setMessage("Czy na pewno chcesz zresetować postęp?")
                    .setPositiveButton("Tak", (dialog, which) -> {
                        reset(points, level, levelup);
                    })
                    .setNegativeButton("Nie", null)
                    .show();
        });


        return view;
    }
    private void updateLevelUp(Button button, int value, int goal)
    {
        if (value >= goal) {
            button.setBackgroundColor(Color.parseColor("#00FF00"));
            button.setEnabled(true);
        } else {
            button.setBackgroundColor(Color.parseColor("#FF0000"));
            button.setEnabled(false);
        }
        button.setText(MessageFormat.format("{0}/{1}", value, goal));
    }
    private void reset(TextView points, TextView level, Button button)
    {
        String countQuery = "SELECT COUNT(*) FROM 'lessons'";
        Cursor c = DatabaseHelper.getData(countQuery, DatabaseHelper.DB_NAME_2);
        int lessonCounter = c.moveToFirst() ? c.getInt(0) : 0;
        c.close();

        JSONManager.init(context, lessonCounter);
        DailyStreakManager.resetStreak(context);
        JSONManager.generateQuests(context);

        int levelNum = JSONManager.getInt(context, "level");
        int pointCt = JSONManager.getInt(context, "points");

        points.setText(MessageFormat.format("Punkty: {0}", pointCt));
        level.setText(MessageFormat.format("Poziom: {0}", levelNum));

        updateLevelUp(button, pointCt, levelNum*100);

        Toast.makeText(context, "Zresetowano postęp!", LENGTH_SHORT).show();
    }
}
