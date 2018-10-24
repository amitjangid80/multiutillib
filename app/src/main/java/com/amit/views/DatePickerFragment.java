package com.amit.views;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.DatePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by AMIT JANGID on 24-Oct-18.
 *
 * this class will display a date picker dialog
 * it will return selected date in the format defined
**/
@SuppressWarnings("unused")
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener
{
    private static final String TAG = DatePickerFragment.class.getSimpleName();

    private Calendar calendar;
    private SelectedDate mSelectedDate;

    private static boolean mIsCurrentDateMin;
    private static String mSelectedDateFormat;

    public void showDatePickerDialog(@NonNull Context context, @NonNull SelectedDate selectedDate,
                                     @NonNull String selectedDateFormat, boolean isCurrentDateMin)
    {
        mIsCurrentDateMin = isCurrentDateMin;
        mSelectedDateFormat = selectedDateFormat;

        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.initializeInterface(selectedDate);
        datePickerFragment.show(((Activity) context).getFragmentManager(), "DatePicker");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        // Use the current date as the default date in the picker
        calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

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
        calendar.set(year, month, dayOfMonth);
        SimpleDateFormat sdf = new SimpleDateFormat(mSelectedDateFormat, Locale.getDefault());

        String selectedDate = sdf.format(calendar.getTime());
        Log.e(TAG, "onDateSet: selected date is: " + selectedDate);

        if (mSelectedDate != null)
        {
            mSelectedDate.selectedDate(selectedDate);
        }
    }

    private void initializeInterface(SelectedDate selectedDate)
    {
        this.mSelectedDate = selectedDate;
    }

    public interface SelectedDate
    {
        void selectedDate(String selectedDate);
    }

    private class InvalidDateFormatException extends RuntimeException
    {
        String message;

        private InvalidDateFormatException(String message)
        {
            super(message);
            this.message = message;
        }
    }
}
