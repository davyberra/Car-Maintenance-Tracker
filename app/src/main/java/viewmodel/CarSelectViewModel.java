package viewmodel;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import dao.VehicleDao;
import database.MainDatabase;
import entity.GasEntry;
import entity.Vehicle;
import io.reactivex.rxjava3.core.Single;

public class CarSelectViewModel extends AndroidViewModel {
    private static final String TAG = CarSelectViewModel.class.getSimpleName();
    private static final String PREFS_FILE = "com.davyberra.carmaintenancetracker.preferences";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String KEY_SELECTED_VEHICLE = "key_selected_vehicle";
    private LiveData<List<Vehicle>> vehicleLiveData;
    private VehicleDao vehicleDao;
    private LiveData<Vehicle> selectedVehicle = new MutableLiveData<Vehicle>();
    private MainDatabase db;
    private Executor executor = Executors.newSingleThreadExecutor();
    private int selectedVehicleId;


    public CarSelectViewModel(Application application) {
        super(application);
        init();
    }

    public void init() {
        db = MainDatabase.getInstance(getApplication().getApplicationContext());
        vehicleDao = db.vehicleDao();
        vehicleLiveData = vehicleDao.getAll();

        sharedPreferences = getApplication().getApplicationContext().getSharedPreferences(
                PREFS_FILE, Context.MODE_PRIVATE
        );
        editor = sharedPreferences.edit();

        editor.putInt(KEY_SELECTED_VEHICLE, 1);
    }

    public LiveData<List<Vehicle>> getVehiclesLiveData() {
        return vehicleLiveData;
    }

    public LiveData<Vehicle> getVehicleLiveData(int id) {
        return vehicleDao.getVehicleById(id);
    }

    public void insertVehicle(Vehicle vehicle) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                db.vehicleDao().insertVehicle(vehicle);
            }
        });
    }


    public void selectVehicle(Vehicle vehicle) {
        editor.putInt(KEY_SELECTED_VEHICLE, vehicle.carId);
        editor.commit();
    }

    public LiveData<Vehicle> getSelectedVehicle() {
        selectedVehicleId = sharedPreferences.getInt(KEY_SELECTED_VEHICLE, 2);
        selectedVehicle = vehicleDao.getVehicleById(selectedVehicleId);
        return selectedVehicle;
    }

    public int getSelectedVehicleId() {
        return selectedVehicleId;
    }
}
