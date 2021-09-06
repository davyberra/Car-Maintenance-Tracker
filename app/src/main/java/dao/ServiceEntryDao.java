package dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import entity.ServiceEntry;

@Dao
public interface ServiceEntryDao {
    @Query("SELECT * FROM serviceentry WHERE carId = (:id) ORDER BY date DESC")
    public LiveData<List<ServiceEntry>> getAllByCarId(int id);

    @Query("SELECT * FROM serviceentry WHERE carID = (:id) AND category = (:type) ORDER BY date DESC")
    public LiveData<List<ServiceEntry>> getAllByType(int id, String type);

    @Query("SELECT * FROM serviceentry WHERE serviceId = (:id)")
    public LiveData<ServiceEntry> getServiceEntryById(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertServiceEntry(ServiceEntry serviceEntry);

    @Delete
    public int delete(ServiceEntry serviceEntry);
}
