package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carmaintenancetracker.R;

import java.util.List;

import entity.ServiceEntry;
import viewmodel.ServiceEntryViewModel;

public class ServiceOverviewAdapter extends RecyclerView.Adapter<ServiceOverviewAdapter.ViewHolder> {
    private final ContextProvider contextProvider;
    private final List<ServiceEntry> serviceEntries;
    private ServiceEntryViewModel viewModel;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView serviceRvNameText;
        public TextView serviceRvCostText;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            serviceRvNameText = itemView.findViewById(R.id.serviceRvNameText);
            serviceRvCostText = itemView.findViewById(R.id.serviceRvCostText);
        }
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

        View serviceEntryView = inflater.inflate(R.layout.service_entry_rv, parent, false);
        return new ViewHolder(serviceEntryView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ServiceEntry serviceEntry = serviceEntries.get(position);
        TextView textViewName = holder.serviceRvNameText;
        textViewName.setText(serviceEntry.serviceDescription);
        TextView textViewCost = holder.serviceRvCostText;
        String cost = String.valueOf(serviceEntry.cost);
        textViewCost.setText("$" + cost);
    }

    @Override
    public int getItemCount() {
        return serviceEntries.size();
    }

}
