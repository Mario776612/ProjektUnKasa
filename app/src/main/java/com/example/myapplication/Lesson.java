package com.example.myapplication;

import android.view.View;

public class Lesson {
    public String title;
    public View.OnClickListener clickListener;
    public boolean completed;
    public Lesson(String title, View.OnClickListener clickListener, boolean completed)
    {
        this.title = title;
        this.clickListener = clickListener;
        this.completed = completed;
    }
}
