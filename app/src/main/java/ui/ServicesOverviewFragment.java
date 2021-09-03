package ui;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carmaintenancetracker.R;

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

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).hideFabButtons();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_services_overview, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ActionBar actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        actionBar.setTitle(pageTitle);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rvServiceOverview);
        serviceEntryViewModel = ViewModelProviders.of(requireActivity()).get(ServiceEntryViewModel.class);
        carSelectViewModel = ViewModelProviders.of(requireActivity()).get(CarSelectViewModel.class);

        serviceEntryViewModel.getAllByCarId(carSelectViewModel.getSelectedVehicleId())
        .observe(getViewLifecycleOwner(), new Observer<List<ServiceEntry>>() {
            @Override
            public void onChanged(List<ServiceEntry> newServiceEntries) {
                serviceEntryViewModel.setCurServiceEntries(newServiceEntries);
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
                ServiceOverviewAdapter adapter = new ServiceOverviewAdapter(filteredServiceEntries, new ContextProvider() {
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
