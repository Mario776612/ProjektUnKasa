package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.view.View;

public class INFLesson extends Lesson {
    private String inf;
    public INFLesson(Context context, String title, int index, String inf) {
        super(context, title, index);
        this.inf = inf;
    }

    @Override
    public View.OnClickListener firstClickListener() {
        return v -> {
            Intent intent = new Intent(this.context, MainActivity4.class);
            intent.putExtra("type", inf);
            context.startActivity(intent);
        };
    }
}
