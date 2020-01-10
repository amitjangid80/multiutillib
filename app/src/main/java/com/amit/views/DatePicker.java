package com.amit.views;

import android.app.DatePickerDialog;
import android.app.Dialog;
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
public class DatePicker extends DialogFragment implements DatePickerDialog.OnDateSetListener
{
    private static final String TAG = DatePicker.class.getSimpleName();

    private static Calendar mCalendar;

    private static String mDateFormat;
    private static boolean mIsCurrentDateMin;

    private final AppCompatActivity mAppCompatActivity;
    private OnDateSelectedListener mOnDateSelectedListener;

    public DatePicker(AppCompatActivity appCompatActivity)
    {
        this.mAppCompatActivity = appCompatActivity;
    }

    public DatePicker setCurrentDateMin(boolean isCurrentDateMin)
    {
        mIsCurrentDateMin = isCurrentDateMin;
        return this;
    }

    public DatePicker setDateFormat(@NonNull String dateFormat)
    {
        mDateFormat = dateFormat;
        return this;
    }

    public DatePicker setOnDateSelectedListener(@NonNull OnDateSelectedListener onDateSelectedListener)
    {
        mOnDateSelectedListener = onDateSelectedListener;
        return this;
    }

    public void showDatePicker()
    {
        this.show(mAppCompatActivity.getSupportFragmentManager(), "DatePicker");
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState)
    {
        // Use the current date as the default date in the picker
        mCalendar = Calendar.getInstance();

        int year = mCalendar.get(Calendar.YEAR);
        int month = mCalendar.get(Calendar.MONTH);
        int day = mCalendar.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        DatePickerDialog datePickerDialog = new DatePickerDialog(mAppCompatActivity, this, year, month, day);

        if (mIsCurrentDateMin)
        {
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        }

        return datePickerDialog;
    }

    @Override
    public void onDateSet(android.widget.DatePicker datePicker, int year, int month, int dayOfMonth)
    {
        mCalendar.set(year, month, dayOfMonth);
        SimpleDateFormat sdf = new SimpleDateFormat(mDateFormat, Locale.getDefault());

        String selectedDate = sdf.format(mCalendar.getTime());
        Log.e(TAG, "onDateSet: selected date is: " + selectedDate);

        if (mOnDateSelectedListener != null)
        {
            mOnDateSelectedListener.onDateSelected(selectedDate);
        }
    }

    public interface OnDateSelectedListener
    {
        void onDateSelected(String selectedDate);
    }
}
