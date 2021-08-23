package ui;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carmaintenancetracker.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import adapter.ContextProvider;
import adapter.GasEntryAdapter;
import entity.GasEntry;
import entity.Vehicle;
import viewmodel.CarSelectViewModel;
import viewmodel.GasEntryViewModel;

public class DashboardFragment extends Fragment {
    private static final String TAG = DashboardFragment.class.getSimpleName();
    private String pageTitle = "Dashboard";
    private TextView dashboardText;
    private Button addGasButton;
    private Button addServiceButton;
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

        CarSelectViewModel carSelectViewModel = new ViewModelProvider(requireActivity()).get(CarSelectViewModel.class);
        selectedVehicle = carSelectViewModel.getSelectedVehicleLiveData();

        selectedVehicle.observe(getViewLifecycleOwner(), vehicle -> {
            Log.d(TAG, String.valueOf(vehicle == null));
            if (vehicle != null) {
                carSelectViewModel.selectVehicle(vehicle);
                TextView vehicleTitle = view.findViewById(R.id.dashboardVehicleTitle);
                TextView vehicleMileage = view.findViewById(R.id.dashboardMileageText);
                vehicleTitle.setText(String.format("%s %s %s",
                        vehicle.year,
                        vehicle.make,
                        vehicle.model
                ));
                vehicleMileage.setText(String.valueOf(vehicle.mileage));
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
        addServiceButton = view.findViewById(R.id.dashboardAddService);
        addServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(DashboardFragment.this)
                        .navigate(R.id.action_dashboardFragment_to_addServiceFragment);
            }
        });

        RecyclerView recyclerView = view.findViewById(R.id.rvDashboard);
        GasEntryViewModel gasEntryViewModel = new ViewModelProvider(requireActivity()).get(GasEntryViewModel.class);
        int carId = carSelectViewModel.getSelectedVehicleId();
        gasEntryViewModel.getAllByCarId(carId).observe(getViewLifecycleOwner(), new Observer<List<GasEntry>>() {
            @Override
            public void onChanged(List<GasEntry> gasEntries) {
                GasEntryAdapter adapter = new GasEntryAdapter(gasEntries, new ContextProvider() {
                    @Override
                    public Context getContext() {
                        return requireActivity();
                    }
                });
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            }
        });
    }
}