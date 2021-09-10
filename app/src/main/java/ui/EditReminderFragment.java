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

public class EditReminderFragment extends Fragment {
    private static final String TAG = EditReminderFragment.class.getSimpleName();
    private final String pageTitle = "Edit Reminder";
    private ReminderViewModel reminderViewModel;
    private CarSelectViewModel carSelectViewModel;
    private Reminder originalReminder;

    private EditText reminderNameText;
    private RadioGroup radioGroup;
    private RadioButton checkedButton;
    private EditText customDateEditText;
    private EditText mileageIntervalEditText;
    private EditText lastServiceMileageText;
    private EditText lastServiceDateText;
    private ImageButton calendarButton;
    private Button saveButton;
    private RadioButton monthlyButton;
    private RadioButton yearlyButton;
    private RadioButton everyButton;

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).hideFabButtons();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_reminder, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setTitle(pageTitle);

        reminderNameText = view.findViewById(R.id.editReminderForText);
        radioGroup = view.findViewById(R.id.editReminderRadioGroup);
        customDateEditText = view.findViewById(R.id.editReminderDateEditText);
        mileageIntervalEditText = view.findViewById(R.id.editReminderMileageEditText);
        lastServiceDateText = view.findViewById(R.id.editReminderDateText);
        lastServiceMileageText = view.findViewById(R.id.editReminderLastMileageText);
        calendarButton = view.findViewById(R.id.editReminderCalendarButton);
        saveButton = view.findViewById(R.id.editReminderSaveButton);
        monthlyButton = view.findViewById(R.id.editReminderMonthlyRadio);
        yearlyButton = view.findViewById(R.id.editReminderYearlyRadio);
        everyButton = view.findViewById(R.id.editReminderCustomDateRadio);

        reminderViewModel = ViewModelProviders.of(requireActivity()).get(ReminderViewModel.class);
        carSelectViewModel = ViewModelProviders.of(requireActivity()).get(CarSelectViewModel.class);

        originalReminder = reminderViewModel.getSelectedReminder();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.reminder_interval_type_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        calendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v);
            }
        });

        reminderNameText.setKeyListener(null);
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

        // Initialize fields with originalReminder values
        reminderNameText.setText(originalReminder.name);
        if (originalReminder.intervalType.equals("date")) {
            if (originalReminder.interval == 30) {
                monthlyButton.setChecked(true);
            } else if (originalReminder.interval == 365) {
                yearlyButton.setChecked(true);
            } else {
                everyButton.setChecked(true);
                customDateEditText.setText(String.valueOf(originalReminder.interval));
            }
            lastServiceDateText.setText(originalReminder.lastDate);
        } else if (originalReminder.intervalType.equals("mileage")) {
            mileageIntervalEditText.setText(String.valueOf(originalReminder.interval));
            lastServiceMileageText.setText(String.valueOf(originalReminder.lastMileage));
        }

    }

    private void saveReminder() {
        if (TextUtils.isEmpty(reminderNameText.getText()) ||
                TextUtils.isEmpty(mileageIntervalEditText.getText()) && checkedButton == null ||
                TextUtils.isEmpty(lastServiceMileageText.getText()) && !TextUtils.isEmpty(mileageIntervalEditText.getText())) {
            Toast.makeText(getContext(), "Please fill out each field.", Toast.LENGTH_LONG).show();
            return;
        }

        Reminder reminder = new Reminder(
                originalReminder.carId,
                originalReminder.name
        );
        reminder.reminderId = originalReminder.reminderId;

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
        NavHostFragment.findNavController(EditReminderFragment.this)
                .navigate(R.id.action_editReminderFragment_to_dashboardFragment);
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new EditReminderFragment.DatePickerFragment(lastServiceDateText);
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
