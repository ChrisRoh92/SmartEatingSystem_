package de.rohnert.smarteatingsystem.utils.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ProgressBar;

public class CustomProgressBar extends ProgressBar
{


    public CustomProgressBar(Context context) {
        super(context);
    }

    public CustomProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CustomProgressBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private int realMax = 0;

    @Override
    public synchronized void setMax(int max) {
        realMax = (int) Math.round(0.75f*getMax());
        super.setMax(max);
    }


    @Override
    public synchronized void setProgress(int progress) {
        int realProgress = Math.round(((float)progress/getMax())*realMax);
        if(realProgress > realMax) {
            realProgress = realMax;

        }

        super.setProgress(realProgress);
    }




}
