package com.amit.views;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by AMIT JANGID on 2019-12-10.
 * Copyright (c) 2019 Amit Jangid. All rights reserved.
**/
public class TimePicker extends DialogFragment implements TimePickerDialog.OnTimeSetListener
{
    private static final String TAG = TimePicker.class.getSimpleName();

    private static Calendar mCalendar;

    private static String mTimeFormat;
    private static boolean mIs24HourView;

    private final AppCompatActivity mAppCompatActivity;
    private OnTimeSetListener mOnTimeSetListener;

    public TimePicker(AppCompatActivity appCompatActivity)
    {
        this.mAppCompatActivity = appCompatActivity;
    }

    public TimePicker setTimeFormat(@NonNull String timeFormat)
    {
        mTimeFormat = timeFormat;
        return this;
    }

    public TimePicker set24HoursView(boolean is24HourView)
    {
        mIs24HourView = is24HourView;
        return this;
    }

    public TimePicker setOnTimeSelectedListener(@NonNull OnTimeSetListener onTimeSetListener)
    {
        mOnTimeSetListener = onTimeSetListener;
        return this;
    }

    public void showTimePicker()
    {
        this.show(mAppCompatActivity.getSupportFragmentManager(), "TimePicker");
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState)
    {
        mCalendar = Calendar.getInstance();

        int minute = mCalendar.get(Calendar.MINUTE);
        int hour = mCalendar.get(Calendar.HOUR_OF_DAY);

        return new TimePickerDialog(mAppCompatActivity, this, hour, minute, mIs24HourView);
    }

    @Override
    public void onTimeSet(android.widget.TimePicker timePicker, int hourOfDay, int minute)
    {
        int mYear, mMonth, mDayOfMonth;

        mYear = mCalendar.get(Calendar.YEAR);
        mMonth = mCalendar.get(Calendar.MONTH);
        mDayOfMonth = mCalendar.get(Calendar.DAY_OF_MONTH);

        mCalendar.set(mYear, mMonth, mDayOfMonth, hourOfDay, minute);
        SimpleDateFormat sdf = new SimpleDateFormat(mTimeFormat, Locale.getDefault());

        String selectedTime = sdf.format(mCalendar.getTime());
        Log.e(TAG, "onTimeSet: selected time is: " + selectedTime);

        if (mOnTimeSetListener != null)
        {
            mOnTimeSetListener.onTimeSet(selectedTime);
        }
    }

    public interface OnTimeSetListener
    {
        void onTimeSet(String selectedTime);
    }
}
