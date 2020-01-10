package com.amit.views;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;

import androidx.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by AMIT JANGID on 24-Oct-18.
 *
 * this class will display a date picker dialog
 * it will return selected date in the format defined
**/
@Deprecated
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener
{
    private static final String TAG = DatePickerFragment.class.getSimpleName();

    private Calendar mCalendar;

    @Deprecated
    private SelectedDate mSelectedDate;

    private OnDateSelectedListener mOnDateSelectedListener;

    private static boolean mIsCurrentDateMin;
    private static String mSelectedDateFormat;

    /**
     * 2018 October 24 - Wednesday - 03:34 PM
     * show date picker dialog method
     *
     * this method will show the date picker dialog fragment
     *
     * @param context - context of the application
     * @param selectedDate - interface of date picker fragment for getting the selected date value
     * @param selectedDateFormat - format in which you want the date.
     *                             Example: yyyy-MM-dd hh:mm:ss
     *
     * @param isCurrentDateMin - pass true to set current date as minimum date else pass false.
    **/
    @Deprecated
    public void showDatePickerDialog(@NonNull Context context, @NonNull SelectedDate selectedDate,
                                     @NonNull String selectedDateFormat, boolean isCurrentDateMin)
    {
        mIsCurrentDateMin = isCurrentDateMin;
        mSelectedDateFormat = selectedDateFormat;

        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.initializeInterface(selectedDate);
        datePickerFragment.show(((Activity) context).getFragmentManager(), "DatePicker");
    }

    /**
     * 2018 October 24 - Wednesday - 03:34 PM
     * show date picker dialog method
     *
     * this method will show the date picker dialog fragment
     *
     * @param context - context of the application
     * @param onDateSelectedListener - interface of date picker fragment for getting the selected date value
     * @param selectedDateFormat - format in which you want the date.
     *                             Example: yyyy-MM-dd hh:mm:ss
     *
     * @param isCurrentDateMin - pass true to set current date as minimum date else pass false.
    **/
    public void showDatePickerDialog(@NonNull Context context,
                                     @NonNull OnDateSelectedListener onDateSelectedListener,
                                     @NonNull String selectedDateFormat, boolean isCurrentDateMin)
    {
        mIsCurrentDateMin = isCurrentDateMin;
        mSelectedDateFormat = selectedDateFormat;

        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.initializeInterface(onDateSelectedListener);
        datePickerFragment.show(((Activity) context).getFragmentManager(), "DatePicker");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        // Use the current date as the default date in the picker
        mCalendar = Calendar.getInstance();

        int year = mCalendar.get(Calendar.YEAR);
        int month = mCalendar.get(Calendar.MONTH);
        int day = mCalendar.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, year, month, day);

        if (mIsCurrentDateMin)
        {
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        }

        return datePickerDialog;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth)
    {
        mCalendar.set(year, month, dayOfMonth);
        SimpleDateFormat sdf = new SimpleDateFormat(mSelectedDateFormat, Locale.getDefault());

        String selectedDate = sdf.format(mCalendar.getTime());
        Log.e(TAG, "onDateSet: selected date is: " + selectedDate);

        if (mSelectedDate != null)
        {
            mSelectedDate.selectedDate(selectedDate);
        }

        if (mOnDateSelectedListener != null)
        {
            mOnDateSelectedListener.onDateSelected(selectedDate);
        }
    }

    @Deprecated
    private void initializeInterface(SelectedDate selectedDate)
    {
        this.mSelectedDate = selectedDate;
    }

    private void initializeInterface(OnDateSelectedListener onDateSelectedListener)
    {
        this.mOnDateSelectedListener = onDateSelectedListener;
    }

    @Deprecated
    public interface SelectedDate
    {
        void selectedDate(String selectedDate);
    }

    public interface OnDateSelectedListener
    {
        void onDateSelected(String selectedDate);
    }
}
