package ui;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.akexorcist.roundcornerprogressbar.IconRoundCornerProgressBar;
import com.example.carmaintenancetracker.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import adapter.ContextProvider;
import adapter.GasEntryAdapter;
import adapter.ReminderAdapter;
import dialog.AddMileageDialogFragment;
import entity.GasEntry;
import entity.Reminder;
import entity.Vehicle;
import viewmodel.CarSelectViewModel;
import viewmodel.GasEntryViewModel;
import viewmodel.ReminderViewModel;

public class DashboardFragment extends Fragment {
    private static final String TAG = DashboardFragment.class.getSimpleName();
    private String pageTitle = "Dashboard";
    private TextView dashboardText;
    private LiveData<Vehicle> selectedVehicle;

    private FloatingActionButton fabAddMain;
    private FloatingActionButton fabAddGas;
    private FloatingActionButton fabAddService;
    private FloatingActionButton fabAddReminder;
    private FloatingActionButton fabAddMileage;
    private boolean isFabOpen;

    private IconRoundCornerProgressBar progressBar;

    private CarSelectViewModel carSelectViewModel;
    private RecyclerView gasEntryRecyclerView;
    private GasEntryAdapter gasEntryAdapter;
    private GasEntryViewModel gasEntryViewModel;
    private RecyclerView reminderRecyclerView;
    private ReminderAdapter reminderAdapter;
    private ReminderViewModel reminderViewModel;

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

        fabAddMain = requireActivity().findViewById(R.id.fabPlusIcon);
        fabAddGas = requireActivity().findViewById(R.id.fabAddGas);
        fabAddService = requireActivity().findViewById(R.id.fabAddService);
        fabAddReminder = requireActivity().findViewById(R.id.fabAddReminder);
        fabAddMileage = requireActivity().findViewById(R.id.fabAddMileage);

        closeFabMenu();

        fabAddGas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeFabMenu();
                NavHostFragment.findNavController(DashboardFragment.this)
                        .navigate(R.id.action_global_addGasFragment);
            }
        });
        fabAddService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeFabMenu();
                NavHostFragment.findNavController(DashboardFragment.this)
                        .navigate(R.id.action_dashboardFragment_to_addServiceFragment);
            }
        });
        fabAddReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeFabMenu();
                NavHostFragment.findNavController(DashboardFragment.this)
                        .navigate(R.id.action_dashboardFragment_to_addReminderFragment);
            }
        });
        fabAddMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isFabOpen) {
                    showFabMenu();
                }else{
                    closeFabMenu();
                }
            }
        });
        fabAddMileage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddMileageDialogFragment dialogFragment = new AddMileageDialogFragment();
                dialogFragment.show(getParentFragmentManager(), "addMileageDialogFragment");
            }
        });
        fabAddGas.show();
        fabAddService.show();
        fabAddMileage.show();
        fabAddReminder.show();
        fabAddMain.show();
    }

    private void showFabMenu() {
        isFabOpen = true;
        fabAddMileage.animate().translationY(-getResources().getDimension(R.dimen.pos_1));
        fabAddReminder.animate().translationY(-getResources().getDimension(R.dimen.pos_2));
        fabAddService.animate().translationY(-getResources().getDimension(R.dimen.pos_3));
        fabAddGas.animate().translationY(-getResources().getDimension(R.dimen.pos_4));
    }

    private void closeFabMenu() {
        isFabOpen = false;
        fabAddMileage.animate().translationY(0);
        fabAddReminder.animate().translationY(0);
        fabAddService.animate().translationY(0);
        fabAddGas.animate().translationY(0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.menu_context_delete, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        GasEntry selectedGasEntry = gasEntryAdapter.getSelectedGasEntry();
        switch (item.getItemId()) {
            case R.id.action_delete:
                deleteSelectedGasEntry(selectedGasEntry);
        }
        return super.onContextItemSelected(item);
    }

    private void deleteSelectedGasEntry(GasEntry selectedGasEntry) {
        gasEntryViewModel.deleteGasEntry(selectedGasEntry);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        carSelectViewModel = new ViewModelProvider(requireActivity()).get(CarSelectViewModel.class);
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
                vehicleMileage.setText(String.valueOf(vehicle.mileage));;
            } else {
                NavHostFragment.findNavController(DashboardFragment.this)
                        .navigate(R.id.action_global_carSelectFragment);
                Toast.makeText(getContext(), "Please select or create a vehicle to get started.",
                        Toast.LENGTH_LONG).show();
            }
        });

        
        gasEntryRecyclerView = view.findViewById(R.id.rvDashboard);
        registerForContextMenu(gasEntryRecyclerView);

        gasEntryViewModel = new ViewModelProvider(requireActivity()).get(GasEntryViewModel.class);
        int carId = carSelectViewModel.getSelectedVehicleId();
        gasEntryViewModel.getAllByCarId(carId).observe(getViewLifecycleOwner(), new Observer<List<GasEntry>>() {
            @Override
            public void onChanged(List<GasEntry> gasEntries) {
                gasEntryAdapter = new GasEntryAdapter(gasEntries, new ContextProvider() {
                    @Override
                    public Context getContext() {
                        return requireActivity();
                    }
                });
                gasEntryRecyclerView.setAdapter(gasEntryAdapter);
                gasEntryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            }
        });
        gasEntryViewModel.getLatestGasEntry(carId).observe(getViewLifecycleOwner(), new Observer<GasEntry>() {
            @Override
            public void onChanged(GasEntry gasEntry) {
                if (gasEntry != null) {
                    TextView lastFillUpDate = view.findViewById(R.id.dashboardLastFillUpContent);
                    TextView mileage = view.findViewById(R.id.dashboardMileageText);
                    lastFillUpDate.setText(String.valueOf(gasEntry.date));
                    mileage.setText(String.valueOf(gasEntry.totalMileage));
                }
            }
        });

        reminderRecyclerView = view.findViewById(R.id.rvDashboardReminders);
        reminderViewModel = ViewModelProviders.of(requireActivity()).get(ReminderViewModel.class);
        reminderViewModel.getAllByCarId(carId).observe(getViewLifecycleOwner(), new Observer<List<Reminder>>() {
            @Override
            public void onChanged(List<Reminder> reminders) {
                reminderAdapter = new ReminderAdapter(new ContextProvider() {
                    @Override
                    public Context getContext() {
                        return requireActivity();
                    }
                }, reminders, carSelectViewModel);
                reminderRecyclerView.setAdapter(reminderAdapter);
                reminderRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            }
        });

//        progressBar = view.findViewById(R.id.dashboardProgressBar);
//        progressBar.setMax(1f);
//        progressBar.setProgress(.75f);
    }
}