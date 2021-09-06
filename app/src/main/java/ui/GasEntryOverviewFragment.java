package ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;

import com.example.carmaintenancetracker.R;

import entity.GasEntry;
import viewmodel.GasEntryViewModel;

public class GasEntryOverviewFragment extends Fragment {
    private static final String TAG = GasEntryOverviewFragment.class.getSimpleName();
    private String pageTitle = "Fill-Up Overview";
    private GasEntryViewModel viewModel;

    private TextView gasEntryOverviewDateText;
    private TextView gasEntryOverviewMileageText;
    private TextView gasEntryOverviewGallonsText;
    private TextView gasEntryOverviewCostText;
    private TextView gasEntryOverviewPpgText;
    private LiveData<GasEntry> gasEntryLiveData;
    private GasEntry selectedGasEntry;

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) requireActivity()).hideFabButtons();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_gas_entry_overview, menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_app_bar_delete:
                viewModel.deleteGasEntry(selectedGasEntry);
                NavHostFragment.findNavController(GasEntryOverviewFragment.this)
                        .navigate(R.id.action_delete_gasEntryOverviewFragment_to_dashboardFragment);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ActionBar actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        actionBar.setTitle(pageTitle);
        return inflater.inflate(R.layout.fragment_gas_entry_overview, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = ViewModelProviders.of(requireActivity()).get(GasEntryViewModel.class);
        gasEntryLiveData = viewModel.getSelectedGasEntry();

        gasEntryOverviewDateText = view.findViewById(R.id.gasEntryOverviewDateText);
        gasEntryOverviewMileageText = view.findViewById(R.id.gasEntryOverviewMileageText);
        gasEntryOverviewGallonsText = view.findViewById(R.id.gasEntryOverviewGallonsText);
        gasEntryOverviewCostText = view.findViewById(R.id.gasEntryOverviewCostText);
        gasEntryOverviewPpgText = view.findViewById(R.id.gasEntryOverviewPpgText);

        gasEntryLiveData.observe(getViewLifecycleOwner(), new Observer<GasEntry>() {
            @Override
            public void onChanged(GasEntry gasEntry) {
                if (gasEntry != null) {
                    selectedGasEntry = gasEntry;
                    gasEntryOverviewDateText.setText(gasEntry.date);
                    gasEntryOverviewMileageText.setText(String.valueOf(gasEntry.totalMileage));
                    gasEntryOverviewGallonsText.setText(String.format("%.2f", gasEntry.gallons));
                    gasEntryOverviewCostText.setText("$" + String.format("%.2f", gasEntry.totalPrice));
                    gasEntryOverviewPpgText.setText("$" + String.format("%.2f", gasEntry.pricePerGallon));
                }
            }
        });
    }
}
