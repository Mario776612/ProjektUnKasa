package com.example.myapplication;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment {
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        context = getContext();

        DatabaseHelper.init(context);
        initJSON();

        TextView LevelDisplay = view.findViewById(R.id.textView3);
        try {
            LevelDisplay.setText("Poziom: " + JSONManager.root(context).getInt("level"));
        } catch (JSONException e) {
            Log.e("HomeFragment", e.toString());
        }

        Globals.mainContainer = view.findViewById(R.id.mainContainer);
        Globals.mainInflater = getLayoutInflater();

        int lessonFinished = requireActivity().getIntent().getIntExtra("finished", -1);
        if(lessonFinished >= 0)
            finishLesson(lessonFinished);

        createLessons();

        return view;
    }

    private void createLessons() {
        Lesson[] inf03 = new Lesson[] {
                new INFLesson(context, "Losowe pytania", -2, "inf03"),
        };
        new Expandable("inf03", inf03).createExpandable();

        Lesson[] inf04 = new Lesson[] {
                new INFLesson(context, "Losowe pytania", -1, "inf04"),
        };
        new Expandable("inf04", inf04).createExpandable();

        String query = "SELECT DISTINCT jezyk FROM 'lessons'";
        Cursor cursor = DatabaseHelper.getData(query, DatabaseHelper.DB_NAME_2);
        if(cursor.moveToFirst()) {
            do {
                List<Lesson> lessons = new ArrayList<>();
                String jezyk = cursor.getString(0);

                String query2 = "SELECT numer FROM 'lessons' WHERE jezyk == '" + jezyk + "'";
                Cursor cursor2 = DatabaseHelper.getData(query2, DatabaseHelper.DB_NAME_2);
                int i = 1;
                if(cursor2.moveToFirst()) {
                    do {
                        int numer = cursor2.getInt(0);
                        String jezykCountQuery = "SELECT COUNT(lesson_id) FROM 'questions' WHERE lesson_id == "+numer;
                        Cursor jezykCountCursor = DatabaseHelper.getData(jezykCountQuery, DatabaseHelper.DB_NAME_2);
                        int count = jezykCountCursor.moveToFirst() ? jezykCountCursor.getInt(0) : 0;
                        jezykCountCursor.close();

                        QuizLesson lesson = new QuizLesson(context, Integer.toString(i++), numer, count);
                        if(isCompleted(numer)) lesson.complete();
                        lessons.add(lesson);
                    } while (cursor2.moveToNext());
                }
                cursor2.close();

                new Expandable(jezyk, lessons.toArray(new Lesson[0])).createExpandable();
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    private void initJSON() {
        String countQuery = "SELECT COUNT(*) FROM 'lessons'";
        Cursor c = DatabaseHelper.getData(countQuery, DatabaseHelper.DB_NAME_2);
        int lessonCounter = c.moveToFirst() ? c.getInt(0) : 0;
        c.close();

        if(!JSONManager.isInit(context))
            JSONManager.init(context, lessonCounter);
    }

    private boolean isCompleted(int index) {
        try {
            JSONArray lessons = JSONManager.array(context, "lessons");
            return lessons.getBoolean(index - 1);
        } catch (JSONException e) {
            Log.e("HomeFragment -> isCompleted", e.toString());
            return false;
        }
    }

    private void finishLesson(int index) {
        try {
            JSONObject root = JSONManager.root(context);
            JSONArray lessons = root.getJSONArray("lessons");
            lessons.put(index - 1, true);
            JSONManager.save(context, root);
        } catch (JSONException e) {
            Log.e("HomeFragment -> finishLesson", e.toString());
        }
    }
}
