package ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavHost;
import androidx.navigation.fragment.NavHostFragment;

import com.example.carmaintenancetracker.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import dao.VehicleDao;
import database.MainDatabase;
import entity.Vehicle;

public class Add_Vehicle_Fragment extends Fragment {

    private String pageTitle = "Add Car";
    private Button saveButton;
    private Button cancelButton;
    private EditText makeText;
    private EditText modelText;
    private EditText yearText;

    public Add_Vehicle_Fragment() {
        super(R.layout.fragment_add_vehicle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_vehicle, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        FloatingActionButton fab = requireActivity().findViewById(R.id.fab);
        fab.hide();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setTitle(pageTitle);

        makeText = view.findViewById(R.id.carMakeText);
        modelText = view.findViewById(R.id.carModelText);
        yearText = view.findViewById(R.id.carYearText);
        saveButton = view.findViewById(R.id.buttonSaveAddVehicle);
        cancelButton = view.findViewById(R.id.addVehicleCancelButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Vehicle vehicle = new Vehicle(
                        yearText.getText().toString(),
                        makeText.getText().toString(),
                        modelText.getText().toString()
                );

                MainDatabase db = MainDatabase.getInstance(getContext());
                VehicleDao vehicleDao = db.vehicleDao();
                vehicleDao.insertVehicle(vehicle);

                NavHostFragment.findNavController(Add_Vehicle_Fragment.this)
                        .navigate(R.id.action_global_carSelectFragment);
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(Add_Vehicle_Fragment.this)
                        .navigate(R.id.action_add_Vehicle_Fragment_to_carSelectFragment);
            }
        });
    }
}
