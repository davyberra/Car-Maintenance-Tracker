package ui;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carmaintenancetracker.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import adapter.ContextProvider;
import adapter.ServiceOverviewAdapter;
import entity.ServiceEntry;
import viewmodel.CarSelectViewModel;
import viewmodel.ServiceEntryViewModel;

public class ServicesOverviewFragment extends Fragment {
    private static final String TAG = ServicesOverviewFragment.class.getSimpleName();
    private String pageTitle = "Services";
    private ServiceEntryViewModel serviceEntryViewModel;
    private CarSelectViewModel carSelectViewModel;
    private Spinner serviceOverviewSpinner;
    private String type = "Engine";
    private RecyclerView recyclerView;
    private ServiceOverviewAdapter adapter;

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).hideFabButtons();
        FloatingActionButton fab = requireActivity().findViewById(R.id.fabPlusIcon);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(ServicesOverviewFragment.this)
                        .navigate(R.id.action_servicesOverviewFragment_to_addServiceFragment);
                fab.hide();
            }
        });
        fab.show();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_services_overview, container, false);
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.menu_context_delete, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        ServiceEntry selectedServiceEntry = adapter.getSelectedServiceEntry();
        switch (item.getItemId()) {
            case R.id.action_delete:
                deleteSelectedServiceEntry(selectedServiceEntry);
                break;
        }
        return super.onContextItemSelected(item);
    }

    private void deleteSelectedServiceEntry(ServiceEntry selectedServiceEntry) {
        serviceEntryViewModel.deleteServiceEntry(selectedServiceEntry);
        serviceEntryViewModel.getTypeLiveData().setValue(serviceEntryViewModel.getTypeLiveData().getValue());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ActionBar actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        actionBar.setTitle(pageTitle);

        recyclerView = (RecyclerView) view.findViewById(R.id.rvServiceOverview);
        registerForContextMenu(recyclerView);

        serviceEntryViewModel = ViewModelProviders.of(requireActivity()).get(ServiceEntryViewModel.class);
        carSelectViewModel = ViewModelProviders.of(requireActivity()).get(CarSelectViewModel.class);

        serviceEntryViewModel.getAllByCarId(carSelectViewModel.getSelectedVehicleId())
        .observe(getViewLifecycleOwner(), new Observer<List<ServiceEntry>>() {
            @Override
            public void onChanged(List<ServiceEntry> serviceEntries) {
                serviceEntryViewModel.setCurServiceEntries(serviceEntries);
                List<ServiceEntry> filteredServiceEntries = new ArrayList<>();
                String type = serviceEntryViewModel.getTypeLiveData().getValue();
                if (type == null || type.equals("All Services")) {
                    filteredServiceEntries = serviceEntries;
                } else {
                    for (ServiceEntry serviceEntry : serviceEntries) {
                        if (serviceEntry.category.equals(type)) {
                            filteredServiceEntries.add(serviceEntry);
                        }
                    }
                }
                serviceEntryViewModel.setFilteredServiceEntries(filteredServiceEntries);
                adapter = new ServiceOverviewAdapter(filteredServiceEntries, new ContextProvider() {
                    @Override
                    public Context getContext() {
                        return requireActivity();
                    }
                });
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            }
        });

        serviceEntryViewModel.getTypeLiveData().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                List<ServiceEntry> filteredServiceEntries = new ArrayList<ServiceEntry>();
                if (s.equals("All Services")) {
                    filteredServiceEntries = serviceEntryViewModel.getCurServiceEntries();
                } else {
                    for (ServiceEntry serviceEntry : serviceEntryViewModel.getCurServiceEntries()) {
                        if (serviceEntry.category.equals(s)) {
                            filteredServiceEntries.add(serviceEntry);
                        }
                    }
                }
                serviceEntryViewModel.setFilteredServiceEntries(filteredServiceEntries);
                adapter = new ServiceOverviewAdapter(filteredServiceEntries, new ContextProvider() {
                    @Override
                    public Context getContext() {
                        return requireActivity();
                    }
                });
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                Log.d(TAG, "called onChanged");
            }
        });
        
        serviceOverviewSpinner = view.findViewById(R.id.spinnerServiceOverview);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.services_overview_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        serviceOverviewSpinner.setAdapter(adapter);
        serviceOverviewSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = parent.getItemAtPosition(position).toString();
                serviceEntryViewModel.getTypeLiveData().setValue(selection);
                Log.d(TAG, parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
