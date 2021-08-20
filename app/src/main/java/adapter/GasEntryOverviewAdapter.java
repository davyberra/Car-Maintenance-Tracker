package adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carmaintenancetracker.R;

import java.util.List;

import entity.GasEntry;

public class GasEntryOverviewAdapter extends RecyclerView.Adapter<GasEntryOverviewAdapter.ViewHolder> {
    private final ContextProvider contextProvider;
    private final List<GasEntry> gasEntries;

    public GasEntryOverviewAdapter(List<GasEntry> gasEntries, ContextProvider contextProvider) {
        this.gasEntries = gasEntries;
        this.contextProvider = contextProvider;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView gasEntryOverviewDateText;
        public TextView gasEntryOverviewMileageText;
        public TextView gasEntryOverviewGallonsText;
        public TextView gasEntryOverviewCostText;
        public TextView gasEntryOverviewPpgText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            gasEntryOverviewDateText = itemView.findViewById(R.id.gasEntryOverviewDateText);
            gasEntryOverviewMileageText = itemView.findViewById(R.id.gasEntryOverviewMileageText);
            gasEntryOverviewGallonsText = itemView.findViewById(R.id.gasEntryOverviewGallonsText);
            gasEntryOverviewCostText = itemView.findViewById(R.id.gasEntryOverviewCostText);
            gasEntryOverviewPpgText = itemView.findViewById(R.id.gasEntryOverviewPpgText);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
