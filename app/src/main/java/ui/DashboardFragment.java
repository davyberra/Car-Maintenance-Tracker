package ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.carmaintenancetracker.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import entity.Vehicle;
import viewmodel.CarSelectViewModel;

public class DashboardFragment extends Fragment {
    private static final String TAG = DashboardFragment.class.getSimpleName();
    private String pageTitle = "Dashboard";
    private TextView dashboardText;
    private Button addGasButton;
    private LiveData<Vehicle> selectedVehicle;


    private CarSelectViewModel viewModel;

    public DashboardFragment() {
        // Required empty public constructor
        super(R.layout.fragment_dashboard);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setTitle(pageTitle);
        FloatingActionButton fab = requireActivity().findViewById(R.id.fab);
        fab.hide();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        CarSelectViewModel viewModel = new ViewModelProvider(requireActivity()).get(CarSelectViewModel.class);
        selectedVehicle = viewModel.getSelectedVehicle();

        selectedVehicle.observe(getViewLifecycleOwner(), vehicle -> {
            Log.d(TAG, String.valueOf(vehicle == null));
            if (vehicle != null) {
                TextView vehicleTitle = view.findViewById(R.id.dashboardVehicleTitle);
                vehicleTitle.setText(String.format("%s %s %s",
                        vehicle.year,
                        vehicle.make,
                        vehicle.model
                ));
            }
        });

        addGasButton = view.findViewById(R.id.dashboardAddGasButton);
        addGasButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(DashboardFragment.this)
                        .navigate(R.id.action_global_addGasFragment);
            }
        });


//        dashboardText = view.findViewById(R.id.dashboardTextView);
//        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
//        String defaultString = "No gas added yet";
//        String newText = sharedPref.getString(getString(R.string.add_gas_test_key), defaultString);
//        dashboardText.setText(newText);
    }
}