package com.example.myapplication;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TasksFragment extends Fragment {
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tasks, container, false);
        context = getContext();

        if (JSONManager.getQuests(context).length() == 0) {
            JSONManager.generateQuests(context);
        }

        LinearLayout questsContainer = view.findViewById(R.id.quests_container);
        JSONArray quests = JSONManager.getQuests(context);

        for (int i = 0; i < quests.length(); i++) {
            final int questIndex = i;
            try {
                JSONObject quest = quests.getJSONObject(i);
                String type = quest.getString("type");
                int goal = quest.getInt("goal");
                boolean completed = quest.getBoolean("completed");

                String description = type.equals("streak")
                        ? "Ucz się przez " + goal + " dni z rzędu"
                        : type.equals("tasks")
                        ? "Ukończ " + goal + " zadań"
                        : type.equals("minutes")
                        ? "Ucz się przez " + goal + " minut"
                        : "Nieznane zadanie";

                View questView = inflater.inflate(R.layout.quest_item, questsContainer, false);

                TextView textView = questView.findViewById(R.id.quest_text);
                Button redeemButton = questView.findViewById(R.id.redeem_button);

                textView.setText(description);
                redeemButton.setEnabled(!completed);

                redeemButton.setOnClickListener(v -> {
                    // TODO: Add real condition check before completion
                    JSONManager.completeQuest(context, questIndex);
                    redeemButton.setEnabled(false);
                    Toast.makeText(context, "Zadanie ukończone!", Toast.LENGTH_SHORT).show();
                });

                questsContainer.addView(questView);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return view;
    }
}
