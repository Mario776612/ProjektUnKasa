package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;

public class QuizLesson extends Lesson {

    int taskCount;

    public QuizLesson(Context context, String title, int index, int taskCount) {
        super(context, title, index);
        this.taskCount = taskCount;
    }

    @Override
    public View.OnClickListener firstClickListener() {
        return v -> {
            Intent intent = new Intent(this.context, MainActivity4.class);
            intent.putExtra("type", "lesson");
            intent.putExtra("lessonNumber", index);
            intent.putExtra("taskCount", taskCount);
            context.startActivity(intent);
        };
    }
}
