package ui;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
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
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;

import com.example.carmaintenancetracker.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import entity.ServiceEntry;
import viewmodel.CarSelectViewModel;
import viewmodel.ServiceEntryViewModel;

public class AddServiceFragment extends Fragment {
    private String pageTitle = "Add New Service";

    private static final String PREFS_FILE = "com.davyberra.carmaintenancetracker.preferences";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String KEY_SELECTED_VEHICLE = "key_selected_vehicle";

    private Spinner categorySpinner;
    private EditText serviceText;
    private EditText costText;
    private EditText locationText;
    private EditText dateText;
    private Button saveButton;
    private ImageButton chooseButton;

    private CarSelectViewModel carSelectViewModel;
    private ServiceEntryViewModel serviceEntryViewModel;

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).hideFabButtons();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_service, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setTitle(pageTitle);

        sharedPreferences = requireActivity().getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        categorySpinner = view.findViewById(R.id.serviceCategorySpinner);
        serviceText = view.findViewById(R.id.serviceServiceText);
        costText = view.findViewById(R.id.serviceCostText);
        locationText = view.findViewById(R.id.serviceLocationText);
        dateText = view.findViewById(R.id.serviceDateText);
        saveButton = view.findViewById(R.id.serviceSaveButton);

        carSelectViewModel = ViewModelProviders.of(requireActivity()).get(CarSelectViewModel.class);
        serviceEntryViewModel = ViewModelProviders.of(requireActivity()).get(ServiceEntryViewModel.class);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.services_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        chooseButton = view.findViewById(R.id.serviceDateChooseButton);
        chooseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(view);
            }
        });

        dateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(dateText);
            }
        });




        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(serviceText.getText()) || TextUtils.isEmpty(costText.getText())
                || TextUtils.isEmpty(dateText.getText()) || TextUtils.isEmpty(locationText.getText())) {
                    Toast.makeText(getContext(), "Please fill out each field.", Toast.LENGTH_LONG).show();
                    return;
                }
                int vehicleId = sharedPreferences.getInt(KEY_SELECTED_VEHICLE, 0);
                SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
                Date date = new Date(System.currentTimeMillis());
                ServiceEntry serviceEntry = new ServiceEntry(
                        vehicleId,
                        categorySpinner.getSelectedItem().toString(),
                        serviceText.getText().toString(),
                        dateText.getText().toString(),
                        Double.parseDouble(costText.getText().toString()),
                        locationText.getText().toString()
                );

                serviceEntryViewModel.insertServiceEntry(serviceEntry);
                NavHostFragment.findNavController(AddServiceFragment.this)
                        .navigate(R.id.action_global_servicesOverviewFragment);
            }
        });

    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment(dateText);
        newFragment.show(getParentFragmentManager(), "datePicker");
    }

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        private EditText dateText;

        public DatePickerFragment(EditText dateText) {
            this.dateText = dateText;
        }

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


