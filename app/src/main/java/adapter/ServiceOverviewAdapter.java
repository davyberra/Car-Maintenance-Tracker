package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carmaintenancetracker.R;

import java.util.List;

import entity.ServiceEntry;
import viewmodel.ServiceEntryViewModel;

public class ServiceOverviewAdapter extends RecyclerView.Adapter<ServiceOverviewAdapter.ViewHolder> {
    private final ContextProvider contextProvider;
    private final List<ServiceEntry> serviceEntries;
    private ServiceEntryViewModel viewModel;
    private int currentPos;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        public TextView serviceRvNameText;
        public TextView serviceRvCostText;
        public CardView serviceEntryCardView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            serviceRvNameText = itemView.findViewById(R.id.serviceRvNameText);
            serviceRvCostText = itemView.findViewById(R.id.serviceRvCostText);
            serviceEntryCardView = itemView.findViewById(R.id.service_entry_card_view);

            itemView.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View v) {
            currentPos = getAdapterPosition();
            return false;
        }
    }

    public ServiceEntry getSelectedServiceEntry() {
        if (currentPos >= 0 && serviceEntries != null && currentPos < serviceEntries.size()) {
            return serviceEntries.get(currentPos);
        }
        return null;
    }

    public ServiceOverviewAdapter(List<ServiceEntry> serviceEntries, ContextProvider contextProvider) {
        this.serviceEntries = serviceEntries;
        this.contextProvider = contextProvider;
    }

    @NonNull
    @Override
    public ServiceOverviewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View serviceEntryView = inflater.inflate(R.layout.service_entry_rv_item, parent, false);
        return new ViewHolder(serviceEntryView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ServiceEntry serviceEntry = serviceEntries.get(position);
        TextView textViewName = holder.serviceRvNameText;
        textViewName.setText(serviceEntry.serviceDescription);
        TextView textViewCost = holder.serviceRvCostText;
        String cost = String.format("%.2f", serviceEntry.cost);
        textViewCost.setText("$" + cost);

        viewModel = ViewModelProviders.of((FragmentActivity) contextProvider.getContext()).get(ServiceEntryViewModel.class);
        holder.serviceEntryCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.selectServiceEntry(serviceEntry);
                Navigation.findNavController(v)
                        .navigate(R.id.action_servicesOverviewFragment_to_serviceEntryOverviewFragment);
            }
        });

        holder.serviceEntryCardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                currentPos = holder.getAdapterPosition();
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return serviceEntries.size();
    }

}
