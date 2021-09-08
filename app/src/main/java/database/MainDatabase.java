package database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import dao.GasEntryDao;
import dao.ReminderDao;
import dao.ServiceEntryDao;
import dao.VehicleDao;
import entity.GasEntry;
import entity.Reminder;
import entity.ServiceEntry;
import entity.Vehicle;

@Database(entities = {Vehicle.class, GasEntry.class, ServiceEntry.class, Reminder.class}, version = 10)
public abstract class MainDatabase extends RoomDatabase {
    private static MainDatabase instance;

    public static synchronized MainDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), MainDatabase.class,
                    "database_file")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

    public abstract VehicleDao vehicleDao();
    public abstract GasEntryDao gasEntryDao();
    public abstract ServiceEntryDao serviceEntryDao();
    public abstract ReminderDao reminderDao();
}
