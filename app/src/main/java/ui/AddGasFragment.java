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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.carmaintenancetracker.R;

import java.text.SimpleDateFormat;
import java.util.Date;

import entity.GasEntry;
import viewmodel.CarSelectViewModel;
import viewmodel.GasEntryViewModel;

public class AddGasFragment extends Fragment {
    private static final String PREFS_FILE = "com.davyberra.carmaintenancetracker.preferences";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String KEY_SELECTED_VEHICLE = "key_selected_vehicle";

    private String pageTitle = "Add Gas";
    private Button saveButton;
    private GasEntryViewModel viewModel;

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
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addGas();
                NavHostFragment.findNavController(AddGasFragment.this)
                        .navigate(R.id.action_addGasFragment_to_dashboardFragment);
            }
        });
    }

    private void addGas() {
        viewModel = ViewModelProviders.of(requireActivity()).get(GasEntryViewModel.class);
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


        viewModel.insertGasEntry(gasEntry);

//        NavHostFragment.findNavController(AddGasFragment.this)
//                .navigate(R.id.action_global_carSelectFragment);
    }
}