package com.example.myapplication;

import android.view.View;

public class Lesson {
    public String title;
    public View.OnClickListener clickListener;
    private boolean completed;
    public Lesson(String title, View.OnClickListener clickListener)
    {
        this.title = title;
        this.clickListener = clickListener;
        this.completed = false;
    }

    public boolean isCompleted()
    {
        return completed;
    }

    public void complete()
    {
        completed = true;
    }
}
