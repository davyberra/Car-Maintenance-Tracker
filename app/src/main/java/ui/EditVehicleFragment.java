package ui;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import adapter.CarSelectAdapter;
import entity.Vehicle;
import viewmodel.CarSelectViewModel;

public class EditVehicleFragment extends Fragment {
    private static final String TAG = EditVehicleFragment.class.getSimpleName();
    private String pageTitle = "Edit Vehicle";
    private EditText yearText;
    private EditText makeText;
    private EditText modelText;
    private ImageView vehicleImage;
    private Button saveButton;
    private Button cancelButton;
    private ImageButton addPhotoButton;

    private Vehicle selectedVehicle;

    private CarSelectAdapter adapter;
    private CarSelectViewModel viewModel;

    private ActivityResultLauncher<String> getContent = registerForActivityResult(
            new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {
                    if (uri != null) {
                        vehicleImage.setImageURI(uri);
                        vehicleImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    }
                }
            });

    public EditVehicleFragment() {
        super(R.layout.fragment_edit_vehicle);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_vehicle, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) requireActivity()).hideFabButtons();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setTitle(pageTitle);
        viewModel = ViewModelProviders.of(requireActivity()).get(CarSelectViewModel.class);

        makeText = view.findViewById(R.id.editCarMakeText);
        modelText = view.findViewById(R.id.editCarModelText);
        yearText = view.findViewById(R.id.editCarYearText);
        addPhotoButton = view.findViewById(R.id.buttonEditPhotoAddVehicle);
        vehicleImage = view.findViewById(R.id.editVehicleImageView);
        saveButton = view.findViewById(R.id.buttonSaveEditVehicle);
        cancelButton = view.findViewById(R.id.editVehicleCancelButton);

        selectedVehicle = viewModel.getSelectedEditVehicle();
        Log.d(TAG, String.valueOf(selectedVehicle == null));
        yearText.setText(selectedVehicle.year);
        makeText.setText(selectedVehicle.make);
        modelText.setText(selectedVehicle.model);
        if (selectedVehicle.imagePath != null) {
            ContextWrapper contextWrapper = new ContextWrapper(getContext());
            File directory = contextWrapper.getDir("vehicleImageDir", Context.MODE_PRIVATE);
            File file = new File(directory, selectedVehicle.imagePath);
            vehicleImage.setImageDrawable(Drawable.createFromPath(file.toString()));
            vehicleImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }

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
                ContextWrapper contextWrapper = new ContextWrapper(getContext());
                File directory = contextWrapper.getDir("vehicleImageDir", Context.MODE_PRIVATE);
                BitmapDrawable drawable = (BitmapDrawable) vehicleImage.getDrawable();
                Bitmap vehicleBitmap = drawable.getBitmap();
                String filename = yearText.getText().toString()
                        + makeText.getText().toString()
                        + modelText.getText().toString()
                        + ".jpg";
                File file = new File(directory, filename);
                Log.d("path", file.toString());

                Log.d("path", file.toString());
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(file);
                    vehicleBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    fos.flush();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                vehicle.imagePath = filename;
                vehicle.carId = selectedVehicle.carId;

                viewModel = ViewModelProviders.of(EditVehicleFragment.this).get(CarSelectViewModel.class);
                viewModel.updateVehicle(vehicle);

                NavHostFragment.findNavController(EditVehicleFragment.this)
                        .navigate(R.id.action_global_carSelectFragment);
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(EditVehicleFragment.this)
                        .navigate(R.id.action_add_Vehicle_Fragment_to_carSelectFragment);
            }
        });
    }
}
