package database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import dao.UserDao;
import dao.VehicleDao;
import entity.User;
import entity.Vehicle;

@Database(entities = {User.class, Vehicle.class}, version = 1)
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
}
