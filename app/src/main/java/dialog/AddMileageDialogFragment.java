package dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;

import com.example.carmaintenancetracker.R;

import entity.Vehicle;
import ui.AddGasFragment;
import viewmodel.CarSelectViewModel;

public class AddMileageDialogFragment extends DialogFragment {
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        final EditText input = new EditText(requireContext());
        input.setInputType(InputType.TYPE_CLASS_NUMBER);

        builder.setView(input)
                .setTitle("Add Mileage")
                .setPositiveButton("Set", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CarSelectViewModel carSelectViewModel = ViewModelProviders.of(requireActivity()).get(CarSelectViewModel.class);
                        Vehicle vehicle = carSelectViewModel.getSelectedVehicle();

                        Vehicle updatedVehicle = new Vehicle(
                                vehicle.year,
                                vehicle.make,
                                vehicle.model
                        );
                        updatedVehicle.mileage = Integer.parseInt(input.getText().toString());
                        updatedVehicle.imagePath = vehicle.imagePath;
                        updatedVehicle.carId = carSelectViewModel.getSelectedVehicleId();

                        carSelectViewModel.updateVehicle(updatedVehicle);

                    }
                })
                .setNegativeButton("Cancel", (dialog, which) -> {});
                return builder.create();
    }
}
