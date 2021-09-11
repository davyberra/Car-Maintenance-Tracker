package adapter;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carmaintenancetracker.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import entity.GasEntry;
import viewmodel.GasEntryViewModel;

public class GasEntryAdapter extends RecyclerView.Adapter<GasEntryAdapter.ViewHolder> {
    private final ContextProvider contextProvider;
    private final List<GasEntry> gasEntries;
    private GasEntryViewModel viewModel;
    private int currentPos;
    private static final SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        public TextView gasEntryDateText;
        public TextView gasEntryCostText;
        public CardView gasEntryCardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            gasEntryDateText = itemView.findViewById(R.id.gasEntryDateText);
            gasEntryCostText = itemView.findViewById(R.id.gasEntryCostText);
            gasEntryCardView = itemView.findViewById(R.id.gasEntryCardView);

            itemView.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View v) {
            currentPos = getAdapterPosition();
            return false;
        }
    }

    public GasEntry getSelectedGasEntry() {
        if (currentPos >= 0 && gasEntries != null && currentPos < gasEntries.size()) {
            return gasEntries.get(currentPos);
        }
        return null;
    }

    public GasEntryAdapter(List<GasEntry> gasEntries, ContextProvider contextProvider) {
        this.gasEntries = gasEntries;
        this.contextProvider = contextProvider;
    }

    @Override
    public GasEntryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View gasEntryView = inflater.inflate(R.layout.gas_entry_rv_item, parent, false);
        return new ViewHolder(gasEntryView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GasEntry gasEntry = gasEntries.get(position);
        TextView textViewDate = holder.gasEntryDateText;
        Date date = new Date(gasEntry.date);
        String textDate = sdf.format(date);
        textViewDate.setText(textDate);
        TextView textViewCost = holder.gasEntryCostText;
        String totalPrice = String.format("%.2f", gasEntry.totalPrice);
        textViewCost.setText("$" + totalPrice);

        viewModel = new ViewModelProvider((ViewModelStoreOwner) contextProvider.getContext()).get(GasEntryViewModel.class);

        holder.gasEntryCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.selectGasEntry(gasEntry);
                Navigation.findNavController(v)
                        .navigate(R.id.action_global_gasEntryOverviewFragment);

            }
        });
        holder.gasEntryCardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                currentPos = holder.getAdapterPosition();
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return gasEntries.size();
    }

}
