package adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.example.carmaintenancetracker.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import dialog.CompleteReminderDialogFragment;
import entity.Reminder;
import entity.Vehicle;
import viewmodel.CarSelectViewModel;
import viewmodel.ReminderViewModel;

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ViewHolder> {
    private final ReminderContextProvider contextProvider;
    private final List<Reminder> reminders;
    private final CarSelectViewModel carSelectViewModel;
    private static final String TAG = ReminderAdapter.class.getSimpleName();
    private ReminderViewModel reminderViewModel;
    private int currentPos;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener{
        public TextView reminderName;
        public RoundCornerProgressBar progressBar;
        public TextView reminderNextService;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            reminderName = itemView.findViewById(R.id.reminderRvName);
            progressBar = itemView.findViewById(R.id.reminderRvProgressBar);
            reminderNextService = itemView.findViewById(R.id.reminderRvNextService);

            itemView.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View v) {
            currentPos = getAdapterPosition();
            return false;
        }
    }

    public Reminder getSelectedReminder() {
        if (currentPos >= 0 && reminders != null && currentPos < reminders.size()) {
            return reminders.get(currentPos);
        }
        return  null;
    }

    public ReminderAdapter(ReminderContextProvider contextProvider, List<Reminder> reminders, CarSelectViewModel carSelectViewModel) {
        this.contextProvider = contextProvider;
        this.reminders = reminders;
        this.carSelectViewModel = carSelectViewModel;
    }

    @NonNull
    @Override
    public ReminderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View reminderView = inflater.inflate(R.layout.reminder_rv_item, parent, false);
        return new ViewHolder(reminderView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Reminder reminder = reminders.get(position);
        reminderViewModel = ViewModelProviders.of(contextProvider.getFragment().requireActivity())
                .get(ReminderViewModel.class);
        holder.reminderName.setText(reminder.name);
        holder.progressBar.setMax(1);

        Vehicle vehicle = carSelectViewModel.getSelectedVehicle();
        if (reminder.intervalType.equals("mileage")) {
            float progress = vehicle.mileage - reminder.lastMileage;
            holder.progressBar.setProgress(progress / reminder.interval);
            holder.reminderNextService.setText("Next Service: " + String.valueOf(reminder.lastMileage + reminder.interval) + " mi");
        } else if (reminder.intervalType.equals("date")) {
            long curDate = System.currentTimeMillis();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
            Date date = null;
            try {
                if (reminder.lastDate != null) {
                    date = simpleDateFormat.parse(reminder.lastDate);
                    long lastDate = date.getTime();
                    long curInterval = curDate - lastDate;
                    long totalInterval = reminder.interval * 24 * 60 * 60 * 1000;
                    double progress = (double) curInterval / totalInterval;
                    holder.progressBar.setProgress((float) progress);
                    long nextDateLong = (long) ((reminder.interval * 24 * 60 * 60 * 1000) + lastDate);
                    Date nextDate = new Date(nextDateLong);
                    String nextDateFormatted = simpleDateFormat.format(nextDate);
                    holder.reminderNextService.setText("Next Service: " + nextDateFormatted);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        holder.progressBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reminderViewModel.setSelectedReminder(reminder);
                CompleteReminderDialogFragment dialogFragment = new CompleteReminderDialogFragment();
                dialogFragment.show(contextProvider.getFragment().getParentFragmentManager(), "completeReminderDialogFragment");
            }
        });

        holder.progressBar.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                currentPos = holder.getAdapterPosition();
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return reminders.size();
    }
}
