package viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import dao.VehicleDao;
import database.MainDatabase;
import entity.Vehicle;

public class CarSelectViewModel extends AndroidViewModel {
    private LiveData<List<Vehicle>> vehicleLiveData;
    private MainDatabase db;
    private Executor executor = Executors.newSingleThreadExecutor();


    public CarSelectViewModel(Application application) {
        super(application);
        init();
    }

    public void init() {
        db = MainDatabase.getInstance(getApplication().getApplicationContext());
        VehicleDao vehicleDao = db.vehicleDao();
        vehicleLiveData = vehicleDao.getAll();
    }

    public LiveData<List<Vehicle>> getVehicleLiveData() {
        return vehicleLiveData;
    }

    public void insert(Vehicle vehicle) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                db.vehicleDao().insertVehicle(vehicle);
            }
        });
    }

}
