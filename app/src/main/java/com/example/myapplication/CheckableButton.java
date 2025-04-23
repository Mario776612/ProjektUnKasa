package com.example.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;

import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class CheckableButton extends AppCompatButton implements View.OnClickListener {

    private boolean isChecked = false;
    private boolean checkedOnce = false;
    private final List<OnCheckedChangeListener> listeners = new ArrayList<>();

    public interface OnCheckedChangeListener {
        void onCheckedChanged(CheckableButton button, boolean isChecked);
    }

    public CheckableButton(Context context) {
        super(context);
        init();
    }

    public CheckableButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CheckableButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOnClickListener(this);
        updateAppearance();
    }

    @Override
    public void onClick(View view) {
        if (!isChecked) {
            setChecked(true);
        }
    }

    public void setChecked(boolean checked) {
        if (this.isChecked != checked) {
            this.isChecked = checked;
            updateAppearance();
            if (!checkedOnce) {
                for (OnCheckedChangeListener listener : listeners) {
                    listener.onCheckedChanged(this, isChecked);
                }
            }
        }
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void addOnCheckedChangeListener(OnCheckedChangeListener listener) {
        listeners.add(listener);
    }

    public void removeOnCheckedChangeListener(OnCheckedChangeListener listener) {
        listeners.remove(listener);
    }

    void setCheckedInternal(boolean checked) {
        checkedOnce = true;
        setChecked(checked);
        checkedOnce = false;
    }

    private void updateAppearance() {
        if (isChecked) {
            setBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.holo_blue_light));
            setTextColor(Color.WHITE);
        } else {
            setBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.darker_gray));
            setTextColor(Color.BLACK);
        }
    }

    @Override
    public int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (isChecked()) {
            mergeDrawableStates(drawableState, new int[]{android.R.attr.state_checked});
        }
        return drawableState;
    }
}