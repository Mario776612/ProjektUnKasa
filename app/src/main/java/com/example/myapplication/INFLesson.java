package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.view.View;

public class INFLesson extends Lesson {
    public INFLesson(Context context, String title, int index) {
        super(context, title, index);
    }

    @Override
    public View.OnClickListener firstClickListener() {
        return v -> {
            Intent intent = new Intent(this.context, MainActivity4.class);
            intent.putExtra("type", "inf03");
            context.startActivity(intent);
        };
    }
}
