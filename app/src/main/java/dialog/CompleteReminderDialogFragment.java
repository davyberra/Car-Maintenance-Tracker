package dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;

import java.util.Calendar;
import java.util.Date;

import entity.Reminder;
import entity.Vehicle;
import viewmodel.CarSelectViewModel;
import viewmodel.ReminderViewModel;

public class CompleteReminderDialogFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new AlertDialog.Builder(requireContext())
                .setMessage("Mark as Done?")
                .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ReminderViewModel reminderViewModel = ViewModelProviders.of(requireActivity())
                                .get(ReminderViewModel.class);
                        Reminder reminder = reminderViewModel.getSelectedReminder();
                        CarSelectViewModel carSelectViewModel = ViewModelProviders.of(requireActivity())
                                .get(CarSelectViewModel.class);
                        Vehicle vehicle = carSelectViewModel.getSelectedVehicle();
                        Reminder updatedReminder = new Reminder(
                                vehicle.carId,
                                reminder.name
                        );
                        updatedReminder.intervalType = reminder.intervalType;
                        updatedReminder.interval = reminder.interval;
                        updatedReminder.reminderId = reminder.reminderId;

                        if (reminder.intervalType.equals("mileage")) {
                            updatedReminder.lastMileage = vehicle.mileage;
                        } else {
                            final Calendar c = Calendar.getInstance();
                            int year = c.get(Calendar.YEAR);
                            int month = c.get(Calendar.MONTH);
                            int day = c.get(Calendar.DAY_OF_MONTH);
                            updatedReminder.lastDate = ((month + 1) + "/" + day + "/" + year);
                        }

                        reminderViewModel.insertReminder(updatedReminder);
                    }
                })
                .setNegativeButton("Cancel", (dialog, which) -> {})
                .create();
    }
}
