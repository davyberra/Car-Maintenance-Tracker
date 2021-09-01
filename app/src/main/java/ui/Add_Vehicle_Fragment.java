package ui;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;

import com.example.carmaintenancetracker.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import entity.Vehicle;
import viewmodel.CarSelectViewModel;

public class Add_Vehicle_Fragment extends Fragment {

    private static final String TAG = Add_Vehicle_Fragment.class.getSimpleName();
    private String pageTitle = "Add Car";
    private Button saveButton;
    private Button cancelButton;
    private ImageButton addPhotoButton;
    private EditText makeText;
    private EditText modelText;
    private EditText yearText;
    private ImageView addVehicleImageView;

    private CarSelectViewModel viewModel;
    private Add_Vehicle_Fragment context;

    private ActivityResultLauncher<String> getContent = registerForActivityResult(
            new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {
                    if (uri != null) {
                        addVehicleImageView.setImageURI(uri);
                        addVehicleImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    }
                }
            });

    public Add_Vehicle_Fragment() {
        super(R.layout.fragment_add_vehicle);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_vehicle, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).hideFabButtons();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setTitle(pageTitle);
        context = this;

        makeText = view.findViewById(R.id.carMakeText);
        modelText = view.findViewById(R.id.carModelText);
        yearText = view.findViewById(R.id.carYearText);
        addPhotoButton = view.findViewById(R.id.buttonAddPhotoAddVehicle);
        addVehicleImageView = view.findViewById(R.id.addVehicleImageView);
        saveButton = view.findViewById(R.id.buttonSaveAddVehicle);
        cancelButton = view.findViewById(R.id.addVehicleCancelButton);

        addPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContent.launch("image/*");
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(yearText.getText()) || TextUtils.isEmpty(makeText.getText())
                || TextUtils.isEmpty(modelText.getText())) {
                    Toast.makeText(getContext(), "Please fill out each field.", Toast.LENGTH_LONG).show();
                    return;
                }
                Vehicle vehicle = new Vehicle(
                        yearText.getText().toString(),
                        makeText.getText().toString(),
                        modelText.getText().toString()
                );
                BitmapDrawable drawable = (BitmapDrawable) addVehicleImageView.getDrawable();
                Bitmap vehicleBitmap = drawable.getBitmap();
                String filename = String.valueOf(vehicle.carId);
                File file = new File()

                viewModel = ViewModelProviders.of(context).get(CarSelectViewModel.class);
                viewModel.insertVehicle(vehicle);

                NavHostFragment.findNavController(Add_Vehicle_Fragment.this)
                        .navigate(R.id.action_global_carSelectFragment);
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(Add_Vehicle_Fragment.this)
                        .navigate(R.id.action_add_Vehicle_Fragment_to_carSelectFragment);
            }
        });
    }
}
