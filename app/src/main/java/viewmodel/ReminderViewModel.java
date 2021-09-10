package viewmodel;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.carmaintenancetracker.R;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import dao.ReminderDao;
import database.MainDatabase;
import entity.Reminder;

public class ReminderViewModel extends AndroidViewModel {

    private static final String PREFS_FILE = "com.davyberra.carmaintenancetracker.preferences";
    private final MainDatabase db;
    private final ReminderDao reminderDao;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String KEY_SELECTED_REMINDER = "key_selected_service_entry";
    private Executor executor = Executors.newSingleThreadExecutor();
    private Reminder selectedReminder;

    public ReminderViewModel(@NonNull Application application) {
        super(application);
        db = MainDatabase.getInstance(getApplication().getApplicationContext());
        reminderDao = db.reminderDao();
        sharedPreferences = getApplication().getApplicationContext().getSharedPreferences(
                PREFS_FILE, Context.MODE_PRIVATE
        );
        editor = sharedPreferences.edit();
    }

    public LiveData<List<Reminder>> getAllByCarId(int carId) {
        return reminderDao.getAllByCarId(carId);
    }

    public void setSelectedReminder(Reminder reminder) {
        selectedReminder = reminder;
        editor.putInt(KEY_SELECTED_REMINDER, reminder.reminderId);
        editor.commit();
    }

    public Reminder getSelectedReminder() {
        return selectedReminder;
    }

    public LiveData<Reminder> getSelectedReminderLiveData() {
        int id = getSelectedReminderId();
        return db.reminderDao().getReminderById(id);
    }

    private int getSelectedReminderId() {
        return sharedPreferences.getInt(KEY_SELECTED_REMINDER, -1);
    }

    public void deleteReminder(Reminder reminder) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                db.reminderDao().delete(reminder);
            }
        });
    }

    public void insertReminder(Reminder reminder) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                db.reminderDao().insertReminder(reminder);
            }
        });
    }

    public void updateReminder(Reminder reminder) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                db.reminderDao().updateReminder(reminder);
            }
        });
    }
}
