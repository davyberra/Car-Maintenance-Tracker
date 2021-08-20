package dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import entity.GasEntry;

@Dao
public interface GasEntryDao {
    @Query("SELECT * FROM gasentry")
    public LiveData<List<GasEntry>> getAll();

    @Query("SELECT * FROM gasentry WHERE gasEntryId = (:id)")
    public LiveData<GasEntry> getGasEntryById(int id);

    @Query("SELECT * FROM gasentry WHERE carId = (:id) ORDER BY gasEntryId DESC")
    public LiveData<List<GasEntry>> getAllByCarId(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertGasEntry(GasEntry gasEntry);

    @Delete
    public int delete(GasEntry gasEntry);
}
