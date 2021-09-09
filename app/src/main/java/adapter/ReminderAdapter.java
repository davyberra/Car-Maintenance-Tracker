package adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.example.carmaintenancetracker.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import entity.Reminder;
import entity.Vehicle;
import viewmodel.CarSelectViewModel;

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ViewHolder> {
    private final ContextProvider contextProvider;
    private final List<Reminder> reminders;
    private final CarSelectViewModel carSelectViewModel;
    private static final String TAG = ReminderAdapter.class.getSimpleName();

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

    public ReminderAdapter(ContextProvider contextProvider, List<Reminder> reminders, CarSelectViewModel carSelectViewModel) {
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
        holder.reminderName.setText(reminder.name);
        holder.progressBar.setMax(1);

        Vehicle vehicle = carSelectViewModel.getSelectedVehicle();
        if (reminder.intervalType.equals("mileage")) {
            float progress = vehicle.mileage - reminder.lastMileage;
            holder.progressBar.setProgress(progress / reminder.interval);
            holder.reminderNextService.setText("Next Service: " + String.valueOf(reminder.lastMileage + reminder.interval) + "mi");
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



    }

    @Override
    public int getItemCount() {
        return reminders.size();
    }
}
