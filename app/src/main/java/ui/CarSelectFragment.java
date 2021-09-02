package ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.carmaintenancetracker.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import adapter.CarSelectAdapter;
import adapter.ContextProvider;
import entity.Vehicle;
import viewmodel.CarSelectViewModel;

public class CarSelectFragment extends Fragment {
    private static final String TAG = CarSelectFragment.class.getSimpleName();
    private String pageTitle = "Select Car";
    private ImageView carImage;
    private CarSelectViewModel viewModel;
    private CarSelectAdapter adapter;
    private RecyclerView recyclerView;

    public CarSelectFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_car_select, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setTitle(pageTitle);
        ((MainActivity) getActivity()).hideFabButtons();
        FloatingActionButton fab = requireActivity().findViewById(R.id.fabPlusIcon);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(CarSelectFragment.this)
                        .navigate(R.id.action_global_add_Vehicle_Fragment);
                fab.hide();
            }
        });
        fab.show();
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.context_edit_delete, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        Vehicle selectedVehicle = adapter.getSelectedVehicle();
        switch (item.getItemId()) {
            case R.id.action_edit:
                editSelectedVehicle(selectedVehicle);
                break;
            case R.id.action_delete:
                deleteSelectedVehicle(selectedVehicle);
                break;
        }
        return super.onContextItemSelected(item);
    }

    private void editSelectedVehicle(Vehicle selectedVehicle) {
        viewModel.setSelectedEditVehicle(selectedVehicle);
        Log.d(TAG, String.valueOf(viewModel.getSelectedEditVehicle() == null));
        NavHostFragment.findNavController(CarSelectFragment.this)
                .navigate(R.id.action_carSelectFragment_to_editVehicleFragment);
    }

    private void deleteSelectedVehicle(Vehicle selectedVehicle) {
        viewModel.deleteVehicle(selectedVehicle);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = (RecyclerView) view.findViewById(R.id.rvCarSelect);
        registerForContextMenu(recyclerView);

        viewModel = ViewModelProviders.of(requireActivity()).get(CarSelectViewModel.class);
        viewModel.getVehiclesLiveData().observe(getViewLifecycleOwner(), new Observer<List<Vehicle>>() {
            @Override
            public void onChanged(List<Vehicle> vehicles) {
                adapter = new CarSelectAdapter(vehicles, new ContextProvider() {
                    @Override
                    public FragmentActivity getContext() {
                        return requireActivity();
                    }
                });
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            }
        });

    }
}