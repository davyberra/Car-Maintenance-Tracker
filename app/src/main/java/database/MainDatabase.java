package database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import dao.GasEntryDao;
import dao.ServiceEntryDao;
import dao.UserDao;
import dao.VehicleDao;
import entity.GasEntry;
import entity.ServiceEntry;
import entity.User;
import entity.Vehicle;

@Database(entities = {User.class, Vehicle.class, GasEntry.class, ServiceEntry.class}, version = 8)
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

    public abstract UserDao userDao();
    public abstract VehicleDao vehicleDao();
    public abstract GasEntryDao gasEntryDao();
    public abstract ServiceEntryDao serviceEntryDao();
}
