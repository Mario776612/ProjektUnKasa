package com.example.myapplication;

import java.io.Serializable;

public class Task implements Serializable {
    private TaskType type;
    public Task(TaskType type)
    {
        this.type = type;
    }

    public TaskType getType()
    {
        return type;
    }
}
