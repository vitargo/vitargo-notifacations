package com.vitargo.vitargonotifications;

import android.app.*;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import androidx.fragment.app.DialogFragment;
import com.vitargo.vitargonotifications.db.CustomNotification;

import java.text.ParseException;
import java.util.Calendar;

public class AddNotificationDialog extends DialogFragment {

    private int mYear, mMonth, mDay, mHour, mMinute;

    public interface AddNotificationDialogListener {
        void onDialogPositiveClick(DialogFragment dialog, CustomNotification notification) throws ParseException;
    }

    AddNotificationDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.pop_up_add_notification, null);
        EditText date = view.findViewById(R.id.new_date);
        EditText time = view.findViewById(R.id.new_time);
        builder.setView(view)
                .setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        CustomNotification notification = new CustomNotification();
                        EditText title = view.findViewById(R.id.new_title);
                        notification.setTitle(title.getText().toString());
                        EditText text = view.findViewById(R.id.new_text);
                        notification.setText(text.getText().toString());
                        EditText date = view.findViewById(R.id.new_date);
                        EditText time = view.findViewById(R.id.new_time);
                        notification.setDate(date.getText().toString() + " " + time.getText().toString());
                        try {
                            listener.onDialogPositiveClick(AddNotificationDialog.this, notification);
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                    }
                })
                .setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        AddNotificationDialog.this.getDialog().cancel();
                    }
                });
        date.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(view.getContext(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        time.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(view.getContext(),
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                time.setText(hourOfDay + ":" + minute);
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = (AddNotificationDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(e.getMessage() + "...Must implement AddPlayerDialogListener");
        }
    }
}
