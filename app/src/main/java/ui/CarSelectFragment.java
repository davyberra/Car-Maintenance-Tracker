package ui;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.carmaintenancetracker.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import adapter.CarSelectAdapter;
import dao.VehicleDao;
import database.MainDatabase;
import entity.Vehicle;
import io.reactivex.rxjava3.core.Single;

public class CarSelectFragment extends Fragment {
    private String pageTitle = "Select Car";
    private ImageView carImage;
    private LiveData<List<Vehicle>> vehicles;

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
        FloatingActionButton fab = requireActivity().findViewById(R.id.fab);
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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MainDatabase db = MainDatabase.getInstance(getContext());
        VehicleDao vehicleDao = db.vehicleDao();
        vehicles = vehicleDao.getAll();

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rvCarSelect);
        CarSelectAdapter adapter = new CarSelectAdapter(vehicles);

//        Iterator iterator = vehicles.iterator();
    }
}