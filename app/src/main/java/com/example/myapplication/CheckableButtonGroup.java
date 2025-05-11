package com.example.myapplication;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class CheckableButtonGroup extends LinearLayout {

    private List<CheckableButton> buttons = new ArrayList<>();

    public CheckableButtonGroup(Context context) {
        super(context);
        init();
    }

    public CheckableButtonGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setOrientation(VERTICAL);
    }

    @Override
    public void onViewAdded(View child) {
        super.onViewAdded(child);
        if (child instanceof CheckableButton) {
            final CheckableButton button = (CheckableButton) child;

            buttons.add(button);
            button.index = buttons.size();
            button.addOnCheckedChangeListener((btn, isChecked) -> {
                if (isChecked) {
                    for (CheckableButton b : buttons) {
                        if (b != btn && b.isChecked()) {
                            b.setCheckedInternal(false);
                        }
                    }
                }
            });
        }
    }

    public void clearSelection() {
        for (CheckableButton b : buttons) {
            b.setCheckedInternal(false);
        }
    }

    public CheckableButton getCheckedButton() {
        for (CheckableButton b : buttons) {
            if (b.isChecked()) return b;
        }
        return null;
    }

    public List<CheckableButton> getButtons()
    {
        return buttons;
    }
}