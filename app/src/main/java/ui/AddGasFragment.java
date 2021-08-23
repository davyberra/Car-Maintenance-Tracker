package ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.carmaintenancetracker.R;

import java.text.SimpleDateFormat;
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

    public AddGasFragment() {
        // Required empty public constructor
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

        saveButton = view.findViewById(R.id.buttonAddGasSave);
        gallonsText = view.findViewById(R.id.gallonsTextView);
        totalPriceText = view.findViewById(R.id.costTextView);
        pricePerGallonText = view.findViewById(R.id.ppgTextView);
        totalMileageText = view.findViewById(R.id.milesTextView);

        gallonsText.addTextChangedListener(new EditTextListener<EditText>(gallonsText) {
            @Override
            public void onTextChanged(EditText target, CharSequence s) {
                if (!TextUtils.isEmpty(totalPriceText.getText()) && !TextUtils.isEmpty(s)) {
                    double gallons = Double.parseDouble(s.toString());
                    double totalPrice = Double.parseDouble(String.valueOf(totalPriceText.getText()));
                    double ppg = totalPrice / gallons;
                    pricePerGallonText.setText(String.valueOf(ppg));
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
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
        Date date = new Date(System.currentTimeMillis());
        GasEntry gasEntry = new GasEntry(
                formatter.format(date),
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
            updatedVehicle.carId = carSelectViewModel.getSelectedVehicleId();

            carSelectViewModel.updateVehicle(updatedVehicle);
            gasEntryViewModel.insertGasEntry(gasEntry);

            NavHostFragment.findNavController(AddGasFragment.this)
                    .navigate(R.id.action_addGasFragment_to_dashboardFragment);
        }

    }
}