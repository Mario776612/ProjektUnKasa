package com.example.myapplication;

import android.view.LayoutInflater;
import android.widget.LinearLayout;

public class Globals {
    public static LayoutInflater mainInflater;
    public static LinearLayout mainContainer;
    public static Lesson currentLesson;

    public static void subtractTask()
    {
        currentLesson.subtractTask();
    }
}
