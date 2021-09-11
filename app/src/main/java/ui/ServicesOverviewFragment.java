package ui;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carmaintenancetracker.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
    private Spinner serviceOverviewSpinnerServiceType;
    private Spinner serviceOverviewSpinnerDateRange;
    private String type = "Engine";
    private RecyclerView recyclerView;
    private ServiceOverviewAdapter adapter;
    private double totalCost = 0;
    private TextView totalCostText;
    private static final SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_services_overview, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_app_bar_add:
                NavHostFragment.findNavController(ServicesOverviewFragment.this)
                        .navigate(R.id.action_servicesOverviewFragment_to_addServiceFragment);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

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

        totalCostText = view.findViewById(R.id.serviceOverviewTotalCostText);
        recyclerView = (RecyclerView) view.findViewById(R.id.rvServiceOverview);
        registerForContextMenu(recyclerView);

        serviceEntryViewModel = ViewModelProviders.of(requireActivity()).get(ServiceEntryViewModel.class);
        carSelectViewModel = ViewModelProviders.of(requireActivity()).get(CarSelectViewModel.class);

        serviceEntryViewModel.getAllByCarId(carSelectViewModel.getSelectedVehicleId())
        .observe(getViewLifecycleOwner(), new Observer<List<ServiceEntry>>() {
            @Override
            public void onChanged(List<ServiceEntry> serviceEntries) {
                serviceEntryViewModel.setCurServiceEntries(serviceEntries);
                try {
                    displayServiceEntries(serviceEntries,
                            serviceEntryViewModel.getTypeLiveData().getValue(),
                            serviceEntryViewModel.getDateRangeLiveData().getValue());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        serviceEntryViewModel.getTypeLiveData().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                try {
                    displayServiceEntries(serviceEntryViewModel.getCurServiceEntries(),
                            s,
                            serviceEntryViewModel.getDateRangeLiveData().getValue());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        serviceEntryViewModel.getDateRangeLiveData().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                try {
                    displayServiceEntries(serviceEntryViewModel.getCurServiceEntries(),
                            serviceEntryViewModel.getTypeLiveData().getValue(),
                            s);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
        
        serviceOverviewSpinnerServiceType = view.findViewById(R.id.spinnerServiceOverviewServiceType);
        ArrayAdapter<CharSequence> serviceTypeAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.services_overview_array, android.R.layout.simple_spinner_item);
        serviceTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        serviceOverviewSpinnerServiceType.setAdapter(serviceTypeAdapter);
        serviceOverviewSpinnerServiceType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

        serviceOverviewSpinnerDateRange = view.findViewById(R.id.spinnerServiceOverviewDateRange);
        ArrayAdapter<CharSequence> dateRangeAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.services_overview_date_range_array, android.R.layout.simple_spinner_item);
        dateRangeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        serviceOverviewSpinnerDateRange.setAdapter(dateRangeAdapter);
        serviceOverviewSpinnerDateRange.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = parent.getItemAtPosition(position).toString();
                serviceEntryViewModel.getDateRangeLiveData().setValue(selection);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void displayServiceEntries(List<ServiceEntry> serviceEntries, String type, String dateRange) throws ParseException {
        List<ServiceEntry> filteredServiceEntries = new ArrayList<ServiceEntry>();
        Calendar rangeDate = Calendar.getInstance();
        if (dateRange != null) {
            if (dateRange.equals("7 Days")) {
                rangeDate.add(Calendar.DAY_OF_YEAR, -7);
            } else if (dateRange.equals("30 Days")) {
                rangeDate.add(Calendar.DAY_OF_YEAR, -30);
            } else if (dateRange.equals("Past Year")) {
                rangeDate.add(Calendar.YEAR, -1);
            }
        }

        List<ServiceEntry> filteredByDate = new ArrayList<>();
        if ( dateRange == null || dateRange.equals("All Time")) {
            filteredByDate = serviceEntries;
        } else {
            for (ServiceEntry serviceEntry : serviceEntries) {
                Calendar formattedDate = Calendar.getInstance();
                Date date = new Date(serviceEntry.date);
                formattedDate.setTime(date);
                if (formattedDate.after(rangeDate)) {
                    filteredByDate.add(serviceEntry);
                }
            }
        }
        if (type == null || type.equals("All Services")) {
            filteredServiceEntries = filteredByDate;
        } else {
            for (ServiceEntry serviceEntry : filteredByDate) {
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
        Log.d(TAG, "called onChanged");

        totalCost = 0;
        for (ServiceEntry serviceEntry : filteredServiceEntries) {
            totalCost += serviceEntry.cost;
        }
        totalCostText.setText("$" + String.format("%.2f", totalCost));
    }
}
