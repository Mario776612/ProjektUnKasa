package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
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
import org.w3c.dom.Text;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class TasksFragment extends Fragment {
    private Context context;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tasks, container, false);
        context = getContext();

        if (JSONManager.getQuests(context).length() == 0) {
            JSONManager.generateQuests(context);
        }

        TextView level = view.findViewById(R.id.textView3);
        level.setText(java.text.MessageFormat.format("Poziom: {0}", JSONManager.getInt(context, "level")));

        TextView points = view.findViewById(R.id.points);
        points.setText(MessageFormat.format("Punkty: {0}", JSONManager.getInt(context, "points")));

        LinearLayout questsContainer = view.findViewById(R.id.quests_container);
        JSONArray quests = JSONManager.getQuests(context);

        List<View> questViews = new ArrayList<>();
        updateButtons(quests, inflater, questsContainer, points, questViews);

        return view;
    }

    private void updateButtons(JSONArray quests, LayoutInflater inflater, LinearLayout questsContainer, TextView points, List<View> questViews)
    {
        for (int i = 0; i < quests.length(); i++) {
            final int questIndex = i;
            try {
                JSONObject quest = quests.getJSONObject(i);
                String type = quest.getString("type");
                int goal = quest.getInt("goal");

                String description = type.equals("streak")
                        ? "Ucz się przez " + goal + " dni z rzędu"
                        : type.equals("tasks")
                        ? "Ukończ " + goal + " zadań"
                        : type.equals("points")
                        ? "Zdobądź " + goal + " punktów"
                        : "Nieznane zadanie";

                View questView = inflater.inflate(R.layout.quest_item, questsContainer, false);

                TextView textView = questView.findViewById(R.id.quest_text);
                Button redeemButton = questView.findViewById(R.id.redeem_button);

                textView.setText(description);

                int value = getQuestValue(redeemButton, questIndex, goal);
                redeemButton.setText(MessageFormat.format("{0}/{1}", value, goal));

                redeemButton.setOnClickListener(v -> {
                    JSONManager.completeQuest(context, questIndex);
                    updateAllQuestButtons(questViews);
                    points.setText(MessageFormat.format("Punkty: {0}", JSONManager.getInt(context, "points")));
                    Toast.makeText(context, "Wyzwanie ukończone!", Toast.LENGTH_SHORT).show();
                });

                questsContainer.addView(questView);
                questViews.add(questView);

            } catch (JSONException e) {
                Log.e("TasksFragment -> onCreate", e.toString());
            }
        }
    }

    private void updateAllQuestButtons(List<View> questViews) {
        JSONArray updatedQuests = JSONManager.getQuests(context);

        for (int i = 0; i < questViews.size(); i++) {
            View questView = questViews.get(i);
            Button button = questView.findViewById(R.id.redeem_button);

            try {
                JSONObject updatedQuest = updatedQuests.getJSONObject(i);
                int goal = updatedQuest.getInt("goal");
                int value = getQuestValue(button, i, goal);

                button.setText(MessageFormat.format("{0}/{1}", value, goal));

                if (value >= goal) {
                    button.setBackgroundColor(Color.parseColor("#00FF00"));
                    button.setEnabled(true);
                } else {
                    button.setBackgroundColor(Color.parseColor("#FF0000"));
                    button.setEnabled(false);
                }

            } catch (JSONException e) {
                Log.e("updateAllQuestButtons", e.toString());
            }
        }
    }

    private int getQuestValue(Button redeemButton, int questIdx, int goal)
    {
        int value;
        switch(questIdx)
        {
            case 0:
                value = JSONManager.getInt(context, "streak");
                break;
            case 1:
                value = JSONManager.getInt(context, "tasksDone");
                break;
            case 2:
                value = JSONManager.getInt(context, "points");
                break;
            default:
                value = 0;
        }
        if(value >= goal) {
            redeemButton.setBackgroundColor(Color.parseColor("#00FF00"));
            redeemButton.setEnabled(true);
        }
        else {
            redeemButton.setBackgroundColor(Color.parseColor("#FF0000"));
            redeemButton.setEnabled(false);
        }
        return value;
    }
}
