package com.example.myapplication;

import static android.widget.Toast.LENGTH_SHORT;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

public class UserFragment extends Fragment {

    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        context = getContext();

        Button reset = view.findViewById(R.id.reset);
        reset.setOnClickListener(v -> {
            new AlertDialog.Builder(requireContext())
                    .setTitle("Potwierdzenie")
                    .setMessage("Czy na pewno chcesz zresetować postęp?")
                    .setPositiveButton("Tak", (dialog, which) -> {
                        reset();
                    })
                    .setNegativeButton("Nie", null)
                    .show();
        });

        return view;
    }
    private void reset()
    {
        String countQuery = "SELECT COUNT(*) FROM 'lessons'";
        Cursor c = DatabaseHelper.getData(countQuery, DatabaseHelper.DB_NAME_2);
        int lessonCounter = c.moveToFirst() ? c.getInt(0) : 0;
        c.close();

        JSONManager.init(context, lessonCounter);
        DailyStreakManager.resetStreak(context);

        Toast.makeText(context, "Zresetowano postęp!", LENGTH_SHORT).show();
    }
}
