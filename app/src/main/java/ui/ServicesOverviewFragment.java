package ui;

import android.content.Context;
import android.os.Bundle;
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

import java.util.List;

import adapter.ContextProvider;
import adapter.ServiceOverviewAdapter;
import entity.ServiceEntry;
import viewmodel.CarSelectViewModel;
import viewmodel.ServiceEntryViewModel;

public class ServicesOverviewFragment extends Fragment {
    private String pageTitle = "Services Overview";
    private ServiceEntryViewModel serviceEntryViewModel;
    private CarSelectViewModel carSelectViewModel;
    private Spinner serviceOverviewSpinner;
    private String type = "Engine";
    private MutableLiveData<String> liveDataType = new MutableLiveData<String>();

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
        
        liveDataType.setValue("Engine");
        liveDataType.observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                type = liveDataType.getValue();
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
                liveDataType.setValue(view.toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rvServiceOverview);
        serviceEntryViewModel = ViewModelProviders.of(requireActivity()).get(ServiceEntryViewModel.class);
        carSelectViewModel = ViewModelProviders.of(requireActivity()).get(CarSelectViewModel.class);
        serviceEntryViewModel.getAllByType(carSelectViewModel.getSelectedVehicleId(), liveDataType.getValue())
                .observe(getViewLifecycleOwner(), new Observer<List<ServiceEntry>>() {
                    @Override
                    public void onChanged(List<ServiceEntry> serviceEntries) {
                        ServiceOverviewAdapter adapter = new ServiceOverviewAdapter(serviceEntries, new ContextProvider() {
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
