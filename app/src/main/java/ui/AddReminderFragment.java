package ui;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carmaintenancetracker.R;

import java.util.Calendar;

import adapter.ReminderAdapter;
import entity.Reminder;
import entity.Vehicle;
import viewmodel.CarSelectViewModel;
import viewmodel.ReminderViewModel;

public class AddReminderFragment extends Fragment {
    private static final String TAG = AddReminderFragment.class.getSimpleName();
    private final String pageTitle = "Add Reminder";
    private ReminderViewModel reminderViewModel;
    private CarSelectViewModel carSelectViewModel;

    private EditText reminderNameText;
    private RadioGroup radioGroup;
    private RadioButton checkedButton;
    private EditText customDateEditText;
    private EditText mileageIntervalEditText;
    private EditText lastServiceMileageText;
    private EditText lastServiceDateText;
    private ImageButton calendarButton;
    private Button saveButton;


    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).hideFabButtons();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_reminder, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setTitle(pageTitle);

        reminderNameText = view.findViewById(R.id.addReminderForText);
        radioGroup = view.findViewById(R.id.addReminderRadioGroup);
        customDateEditText = view.findViewById(R.id.addReminderDateEditText);
        mileageIntervalEditText = view.findViewById(R.id.addReminderMileageEditText);
        lastServiceDateText = view.findViewById(R.id.addReminderDateText);
        lastServiceMileageText = view.findViewById(R.id.addReminderLastMileageText);
        calendarButton = view.findViewById(R.id.addReminderCalendarButton);
        saveButton = view.findViewById(R.id.addReminderSaveButton);

        reminderViewModel = ViewModelProviders.of(requireActivity()).get(ReminderViewModel.class);
        carSelectViewModel = ViewModelProviders.of(requireActivity()).get(CarSelectViewModel.class);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.reminder_interval_type_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        calendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v);
            }
        });

        lastServiceDateText.setKeyListener(null);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                checkedButton = group.findViewById(checkedId);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveReminder();
            }
        });
    }

    private void saveReminder() {
        if (TextUtils.isEmpty(reminderNameText.getText()) ||
        TextUtils.isEmpty(mileageIntervalEditText.getText()) && checkedButton == null ||
        TextUtils.isEmpty(lastServiceMileageText.getText()) && !TextUtils.isEmpty(mileageIntervalEditText.getText())) {
            Toast.makeText(getContext(), "Please fill out each field.", Toast.LENGTH_LONG).show();
            return;
        }
        Vehicle vehicle = carSelectViewModel.getSelectedVehicle();

        Reminder reminder = new Reminder(
              vehicle.carId,
              reminderNameText.getText().toString()
        );

        if (TextUtils.isEmpty(mileageIntervalEditText.getText())) {
            reminder.intervalType = "date";
            if (checkedButton.getText().equals("Monthly")) {
                reminder.interval = 30;
            } else if (checkedButton.getText().equals("Yearly")) {
                reminder.interval = 365;
            } else if (checkedButton.getText().equals("Every")) {
                reminder.interval = Integer.parseInt(customDateEditText.getText().toString());
            }
        } else {
            reminder.intervalType = "mileage";
            reminder.interval = Integer.parseInt(mileageIntervalEditText.getText().toString());
        }

        if (!TextUtils.isEmpty(lastServiceMileageText.getText())) {
            reminder.lastMileage = Integer.parseInt(lastServiceMileageText.getText().toString());
        }
        if (!TextUtils.isEmpty(lastServiceDateText.getText())) {

            reminder.lastDate = lastServiceDateText.getText().toString();
        } else {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            reminder.lastDate = ((month + 1) + "/" + day + "/" + year);
        }

        reminderViewModel.insertReminder(reminder);
        NavHostFragment.findNavController(AddReminderFragment.this)
                .navigate(R.id.action_addReminderFragment_to_dashboardFragment);
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new AddGasFragment.DatePickerFragment(lastServiceDateText);
        newFragment.show(getParentFragmentManager(), "datePicker");
    }

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        private final EditText dateText;

        public DatePickerFragment(EditText dateText) {
            this.dateText = dateText;
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            dateText.setText((month + 1) + "/" + day + "/" + year);
        }
    }
}
