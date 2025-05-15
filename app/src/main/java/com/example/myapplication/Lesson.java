package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Random;

public class Lesson {
    public String title;
    public int index;
    private String status;
    private ArrayList<Task> tasks = new ArrayList<Task>();
    Context context;
    public Lesson(Context context, String title, int index)
    {
        this.title = title;
        this.status = "unfinished";
        this.index = index;
        this.context = context;
    }

    public View.OnClickListener firstClickListener()
    {
        return v -> {
            tasks.clear();
            generateTasks();
            Class<? extends AppCompatActivity> activity;
            switch (tasks.get(0).getType()) {
                case CHOOSE:
                    activity = MainActivity4.class;
                    break;
                //case WRITE:      może kiedyś (wpisywanie odpowiedzi do textboxa)
                //    activity = MainActivity3.class;
                //    break;
                //case CONNECT:
                //    activity = MainActivity2.class;
                //    break;
                default:
                    activity = MainActivity.class;
            }
            Intent intent = new Intent(context, activity);
            intent.putExtra("lesson", index);
            context.startActivity(intent);
        };
    }

    private void generateTasks()
    {
        //Random rng = new Random();
        for(int i = 0; i < 10; i++)
        {
            //int taskIdx = rng.nextInt(3);
            TaskType taskType = TaskType.values()[0];
            Task task = new Task(taskType);
            tasks.add(task);
        }
    }

    public ArrayList<Task> getTasks()
    {
        return tasks;
    }

    public void subtractTask()
    {
        tasks.remove(0);
    }
    public boolean isCompleted()
    {
        return status.equals("finished");
    }

    public boolean isTried()
    {
        return status.equals("tried");
    }

    public boolean isNone()
    {
        return status.equals("unfinished");
    }
    public void complete()
    {
        status = "finished";
    }

    public void tried()
    {
        status = "tried";
    }
}