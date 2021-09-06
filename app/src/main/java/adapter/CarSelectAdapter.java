package adapter;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carmaintenancetracker.R;

import java.io.File;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import entity.Vehicle;
import ui.MainActivity;
import viewmodel.CarSelectViewModel;

public class CarSelectAdapter extends RecyclerView.Adapter<CarSelectAdapter.ViewHolder> {
    private static final String PREFS_FILE = "com.davyberra.carmaintenancetracker.preferences";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private final ContextProvider contextProvider;
    private String KEY_RADIO_SELECTED_TOGGLE = "key_radio_selected_toggle_";
    private List<Vehicle> vehicles;
    private CarSelectViewModel viewModel;
    private int currentPos;
    private Executor executor = Executors.newSingleThreadExecutor();

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        public CardView carCardView;
        public ImageView carImageView;
        public RadioButton carRadioButton;
        public TextView carTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            carCardView = (CardView) itemView.findViewById(R.id.carCardView);
            carImageView = (ImageView) itemView.findViewById(R.id.carImageView);
            carRadioButton = (RadioButton) itemView.findViewById(R.id.carRadioButton);
            carTextView = (TextView) itemView.findViewById(R.id.carTextView);

            itemView.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View v) {
            currentPos = getAdapterPosition();
            return false;
        }
    }

    public Vehicle getSelectedVehicle() {
        if (currentPos >= 0 && vehicles != null && currentPos < vehicles.size()) {
            return vehicles.get(currentPos);
        }
        return null;
    }

    public CarSelectAdapter(List<Vehicle> vehicles, ContextProvider contextProvider) {
        this.vehicles = vehicles;
        this.contextProvider = contextProvider;
    }


    @Override
    public CarSelectAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        sharedPreferences = context.getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        LayoutInflater inflater = LayoutInflater.from(context);

        View carView = inflater.inflate(R.layout.car_item_view, parent, false);

        ViewHolder viewHolder = new ViewHolder(carView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Vehicle vehicle = vehicles.get(position);
        TextView textView = holder.carTextView;
        String text = String.format("%s %s %s",
                vehicle.year,
                vehicle.make,
                vehicle.model);
        textView.setText(text);


        executor.execute(new Runnable() {
            @Override
            public void run() {
                if (vehicle.imagePath != null) {
                    ContextWrapper contextWrapper = new ContextWrapper(contextProvider.getContext());
                    File directory = contextWrapper.getDir("vehicleImageDir", Context.MODE_PRIVATE);
                    File file = new File(directory, vehicle.imagePath);
                    Log.d("path", file.toString());
                    ((MainActivity) contextProvider.getContext()).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            holder.carImageView.setImageDrawable(Drawable.createFromPath(file.toString()));
                            holder.carImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        }
                    });
                }
            }
        });


        viewModel = new ViewModelProvider((ViewModelStoreOwner) contextProvider.getContext()).get(CarSelectViewModel.class);
        if (vehicle.carId == viewModel.getSelectedVehicleId()) {
            holder.carRadioButton.setChecked(true);
        }
        holder.carImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.selectVehicle(vehicle);
                Navigation.findNavController(v)
                        .navigate(R.id.action_carSelectFragment_to_dashboardFragment);
            }
        });

        holder.carImageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                currentPos = holder.getAdapterPosition();
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return vehicles.size();
    }
}
