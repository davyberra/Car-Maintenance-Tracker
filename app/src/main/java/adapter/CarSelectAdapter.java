package adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.LiveData;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carmaintenancetracker.R;

import org.w3c.dom.Text;

import java.util.List;

import entity.Vehicle;
import ui.CarSelectFragment;

public class CarSelectAdapter extends RecyclerView.Adapter<CarSelectAdapter.ViewHolder> {
    private static final String PREFS_FILE = "com.davyberra.carmaintenancetracker.preferences";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String KEY_RADIO_SELECTED_TOGGLE = "key_radio_selected_toggle_";
    private List<Vehicle> vehicles;

    public class ViewHolder extends RecyclerView.ViewHolder {
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
        }
    }

    public CarSelectAdapter(List<Vehicle> vehicles) {
        this.vehicles = vehicles;
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
        String pref_key = KEY_RADIO_SELECTED_TOGGLE + Integer.toString(position);
        holder.carRadioButton.setChecked(sharedPreferences.getBoolean(pref_key, false));
        TextView textView = holder.carTextView;
        String text = String.format("%s %s %s",
                vehicle.year,
                vehicle.make,
                vehicle.model);
        textView.setText(text);

        holder.carImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putBoolean(pref_key, true);
                holder.carRadioButton.setChecked(true);
                Navigation.findNavController(v)
                        .navigate(R.id.action_carSelectFragment_to_dashboardFragment);
            }
        });
    }

    @Override
    public int getItemCount() {
        return vehicles.size();
    }
}
