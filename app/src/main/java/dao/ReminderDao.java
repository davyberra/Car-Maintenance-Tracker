package dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import entity.Reminder;

@Dao
public interface ReminderDao {
    @Query("SELECT * FROM reminder WHERE carId = (:id) ORDER BY reminderId")
    public LiveData<List<Reminder>> getAllByCarId(int id);

    @Query("SELECT * FROM reminder WHERE reminderId = (:id)")
    public LiveData<Reminder> getReminderById(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertReminder(Reminder reminder);

    @Update
    public int updateReminder(Reminder reminder);

    @Delete
    public int delete(Reminder reminder);
}
