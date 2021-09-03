package ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.carmaintenancetracker.R;

import entity.ServiceEntry;
import viewmodel.ServiceEntryViewModel;

public class ServiceEntryOverviewFragment extends Fragment {
    private String pageTitle = "Service Overview";
    private TextView dateText;
    private TextView categoryText;
    private TextView serviceText;
    private TextView locationText;
    private TextView totalCostText;
    private LiveData<ServiceEntry> serviceEntryLiveData;
    private ServiceEntryViewModel viewModel;


    public ServiceEntryOverviewFragment() {
        super(R.layout.fragment_service_entry_overview);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_service_entry_overview, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setTitle(pageTitle);

        dateText = view.findViewById(R.id.serviceEntryOverviewDateText);
        categoryText = view.findViewById(R.id.serviceEntryOverviewCategoryText);
        serviceText = view.findViewById(R.id.serviceEntryOverviewServiceText);
        locationText = view.findViewById(R.id.serviceEntryOverviewLocationText);
        totalCostText = view.findViewById(R.id.serviceEntryOverviewCostText);

        viewModel = ViewModelProviders.of(requireActivity()).get(ServiceEntryViewModel.class);
        serviceEntryLiveData = viewModel.getSelectedServiceEntry();
        serviceEntryLiveData.observe(getViewLifecycleOwner(), new Observer<ServiceEntry>() {
            @Override
            public void onChanged(ServiceEntry serviceEntry) {
                dateText.setText(serviceEntry.date);
                categoryText.setText(serviceEntry.category);
                serviceText.setText(serviceEntry.serviceDescription);
                locationText.setText(serviceEntry.location);
                String cost = String.format("%.2f", serviceEntry.cost);
                totalCostText.setText("$" + cost);
            }
        });
    }
}

