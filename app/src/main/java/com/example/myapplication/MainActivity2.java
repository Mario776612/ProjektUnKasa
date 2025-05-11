package com.example.myapplication;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Dictionary;
import java.util.List;

public class MainActivity2 extends AppCompatActivity {

    private CheckableButtonGroup left;
    private CheckableButtonGroup right;
    private int lesson;
    private Dictionary<String, String> answers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        lesson = intent.getIntExtra("lesson", 0);

        left = findViewById(R.id.groupLeft);
        right = findViewById(R.id.groupRight);

        initAnswers();

        setButtonListenerForGroup(left);
        setButtonListenerForGroup(right);
    }

    public boolean isAnyButtonVisible()
    {
        List<CheckableButton> left = ((CheckableButtonGroup)findViewById(R.id.groupLeft)).getButtons();

        for(CheckableButton button : left)
        {
            if(button.getVisibility() == VISIBLE)
                return true;
        }
        return false;
    }


    public void doSelectedButtonsShareSameText() {
        CheckableButtonGroup left = findViewById(R.id.groupLeft);
        CheckableButtonGroup right = findViewById(R.id.groupRight);

        CheckableButton selectedButtonLeft = left.getCheckedButton();
        CheckableButton selectedButtonRight = right.getCheckedButton();

        if (selectedButtonLeft != null && selectedButtonRight != null) {
            String leftText = selectedButtonLeft.getText().toString();
            String rightText = selectedButtonRight.getText().toString();
            if(leftText.equals(rightText))
            {
                selectedButtonLeft.setVisibility(GONE);
                selectedButtonRight.setVisibility(GONE);
                left.clearSelection();
                right.clearSelection();
            }
            else {
                selectedButtonLeft.setBackgroundColor(Color.RED);
                selectedButtonRight.setBackgroundColor(Color.RED);
                new Handler(Looper.getMainLooper()).postDelayed(this::fail, 1000);
            }
            if(!isAnyButtonVisible())
            {
                complete();
            }
        }
    }

    private void setButtonListenerForGroup(CheckableButtonGroup group) {
        for (int i = 0; i < group.getChildCount(); i++) {
            View child = group.getChildAt(i);

            if (child instanceof CheckableButton) {
                CheckableButton button = (CheckableButton) child;
                button.addOnCheckedChangeListener((btn, isChecked) -> {
                    if (isChecked) {
                        doSelectedButtonsShareSameText();
                    }
                });
            }
        }
    }

    private void initAnswers()
    {
        for(int i = 0; i < left.getChildCount(); i++)
        {
            answers.put(left.getButtons().get(i).text, right.getButtons().get(i).text);
        }
    }
    private void complete()
    {
        Intent intent = new Intent(MainActivity2.this, MainActivity.class);
        intent.putExtra("finished", lesson);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void fail()
    {
        Intent intent = new Intent(MainActivity2.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}