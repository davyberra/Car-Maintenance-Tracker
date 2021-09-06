package ui;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.carmaintenancetracker.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import entity.GasEntry;
import entity.Vehicle;
import listener.EditTextListener;
import viewmodel.CarSelectViewModel;
import viewmodel.GasEntryViewModel;



public class AddGasFragment extends Fragment {
    private static final String PREFS_FILE = "com.davyberra.carmaintenancetracker.preferences";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String KEY_SELECTED_VEHICLE = "key_selected_vehicle";

    private String pageTitle = "Add Gas";
    private Button saveButton;
    private GasEntryViewModel gasEntryViewModel;
    private CarSelectViewModel carSelectViewModel;

    private EditText gallonsText;
    private EditText pricePerGallonText;
    private EditText totalPriceText;
    private EditText totalMileageText;
    private EditText dateText;
    private ImageButton dateButton;

    public AddGasFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).hideFabButtons();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_gas, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ActionBar actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        actionBar.setTitle(pageTitle);

        sharedPreferences = requireActivity().getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        saveButton = view.findViewById(R.id.addGasSaveButton);
        gallonsText = view.findViewById(R.id.gallonsTextView);
        totalPriceText = view.findViewById(R.id.addGasCostText);
        pricePerGallonText = view.findViewById(R.id.addGasPpgText);
        totalMileageText = view.findViewById(R.id.milesTextView);
        dateText = view.findViewById(R.id.addGasDateText);
        dateButton = view.findViewById(R.id.addGasCalendarButton);

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        dateText.setText((month + 1) + "/" + day + "/" + year);

        gallonsText.addTextChangedListener(new EditTextListener<EditText>(gallonsText) {
            @Override
            public void onTextChanged(EditText target, CharSequence s) {
                if (!TextUtils.isEmpty(totalPriceText.getText()) && !TextUtils.isEmpty(s)) {
                    double gallons = Double.parseDouble(s.toString());
                    double totalPrice = Double.parseDouble(String.valueOf(totalPriceText.getText()));
                    double ppg = totalPrice / gallons;
                    pricePerGallonText.setText(String.format("%.2f", ppg));
                }
            }
        });

        totalPriceText.addTextChangedListener(new EditTextListener<EditText>(totalPriceText) {
            @Override
            public void onTextChanged(EditText target, CharSequence s) {
                if (!TextUtils.isEmpty(gallonsText.getText()) && !TextUtils.isEmpty(s)) {
                    double totalPrice = Double.parseDouble(s.toString());
                    double gallons = Double.parseDouble(String.valueOf(gallonsText.getText()));
                    double ppg = totalPrice / gallons;
                    pricePerGallonText.setText(String.valueOf(ppg));
                }
            }
        });

//        pricePerGallonText.addTextChangedListener(new EditTextListener<EditText>(pricePerGallonText) {
//            @Override
//            public void onTextChanged(EditText target, CharSequence s) {
//                if (!TextUtils.isEmpty(s) && !TextUtils.isEmpty(gallonsText.getText())) {
//                    double ppg = Double.parseDouble(s.toString());
//                    double gallons = Double.parseDouble(String.valueOf(gallonsText.getText()));
//                    double totalPrice = gallons * ppg;
//                    totalPriceText.setText(String.valueOf(totalPrice));
//                }
//            }
//        });

        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v);
            }
        });

        dateText.setKeyListener(null);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(gallonsText.getText()) || TextUtils.isEmpty(totalPriceText.getText()) ||
                        TextUtils.isEmpty(pricePerGallonText.getText()) || TextUtils.isEmpty(totalMileageText.getText())) {
                    Toast.makeText(getContext(), "Please fill out each field.", Toast.LENGTH_LONG).show();
                    return;
                }
                addGas();
            }
        });
    }

    private void addGas() {
        gasEntryViewModel = ViewModelProviders.of(requireActivity()).get(GasEntryViewModel.class);
        int vehicleId = sharedPreferences.getInt(KEY_SELECTED_VEHICLE, 0);
        GasEntry gasEntry = new GasEntry(
                dateText.getText().toString(),
                vehicleId,
                Double.parseDouble(gallonsText.getText().toString()),
                Double.parseDouble(totalPriceText.getText().toString()),
                Double.parseDouble(pricePerGallonText.getText().toString()),
                Integer.parseInt(totalMileageText.getText().toString())
        );

        carSelectViewModel = ViewModelProviders.of(requireActivity()).get(CarSelectViewModel.class);
        Vehicle vehicle = carSelectViewModel.getSelectedVehicle();

        if (vehicle.mileage > gasEntry.totalMileage) {
            Toast.makeText(getContext(), "Inputted mileage is lower than total mileage for vehicle", Toast.LENGTH_LONG).show();
        } else {
            Vehicle updatedVehicle = new Vehicle(
                    vehicle.year,
                    vehicle.make,
                    vehicle.model
            );
            updatedVehicle.mileage = gasEntry.totalMileage;
            updatedVehicle.imagePath = vehicle.imagePath;
            updatedVehicle.carId = carSelectViewModel.getSelectedVehicleId();

            carSelectViewModel.updateVehicle(updatedVehicle);
            gasEntryViewModel.insertGasEntry(gasEntry);

            NavHostFragment.findNavController(AddGasFragment.this)
                    .navigate(R.id.action_addGasFragment_to_dashboardFragment);
        }

    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new AddGasFragment.DatePickerFragment(dateText);
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