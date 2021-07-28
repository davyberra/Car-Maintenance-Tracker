package ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.carmaintenancetracker.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class DashboardFragment extends Fragment {
    private String pageTitle = "Dashboard";
    private TextView dashboardText;

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

//        dashboardText = view.findViewById(R.id.dashboardTextView);
//        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
//        String defaultString = "No gas added yet";
//        String newText = sharedPref.getString(getString(R.string.add_gas_test_key), defaultString);
//        dashboardText.setText(newText);
    }
}