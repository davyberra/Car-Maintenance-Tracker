package adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.example.carmaintenancetracker.R;

import java.util.List;

import entity.Reminder;

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ViewHolder> {
    private final ContextProvider contextProvider;
    private final List<Reminder> reminders;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView reminderName;
        public RoundCornerProgressBar progressBar;
        public TextView reminderNextService;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            reminderName = itemView.findViewById(R.id.reminderRvName);
            progressBar = itemView.findViewById(R.id.reminderRvProgressBar);
            reminderNextService = itemView.findViewById(R.id.reminderRvNextService);
        }
    }

    public ReminderAdapter(ContextProvider contextProvider, List<Reminder> reminders) {
        this.contextProvider = contextProvider;
        this.reminders = reminders;
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
