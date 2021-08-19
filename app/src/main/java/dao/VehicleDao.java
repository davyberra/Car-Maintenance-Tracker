package dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import entity.Vehicle;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface VehicleDao {
    @Query("SELECT * FROM vehicle")
    public LiveData<List<Vehicle>> getAll();

    @Query("SELECT * FROM vehicle WHERE carId = (:id)")
    public Single<Vehicle> getVehicleById(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public Completable insertVehicle(Vehicle vehicle);

    @Delete
    public Completable delete(Vehicle vehicle);
}
