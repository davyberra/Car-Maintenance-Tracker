package dao;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import entity.Vehicle;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface VehicleDao {
    @Query("SELECT * FROM vehicle")
    public LiveData<List<Vehicle>> getAll();

    @Query("SELECT * FROM vehicle WHERE carId = (:id)")
    public LiveData<Vehicle> getVehicleById(int id);

//    @Query("SELECT MIN(carId) AS carId FROM vehicle")
//    public LiveData<Vehicle> getVehicleWithLowestId();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertVehicle(Vehicle vehicle);

    @Update
    public int updateVehicle(Vehicle vehicle);

    @Delete
    public Completable delete(Vehicle vehicle);
}
