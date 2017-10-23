package assignment.keng.birthdayreminder;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;




public class DatePickerFragment
        extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use current date
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Instantiate DatePickerDialog
        DatePickerDialog dpd = new DatePickerDialog(getActivity(), this, year, month, day);

        // You may configure your DatePicker here before returning it.
        DatePicker dp = dpd.getDatePicker();

        // Example, set minimum Date to tomorrow


        // Example, set maximum Date to 2020-12-31
        calendar.set(Calendar.YEAR, 2020);
        calendar.set(Calendar.MONTH, 11);   // In Java, months are counted from 0 to 11.
        calendar.set(Calendar.DAY_OF_MONTH, 31);
        dp.setMaxDate(calendar.getTimeInMillis());

        // return DatePickerDialog
        return dpd;

    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        EditText etDate = (EditText)getActivity().findViewById(R.id.input_birthdate);
        etDate.setText(year + "-" + String.format("%02d", month + 1) + "-" + String.format("%02d", day));
    }
}
