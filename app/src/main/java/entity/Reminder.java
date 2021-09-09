package entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = @ForeignKey(entity = Vehicle.class,
        parentColumns = "carId",
        childColumns = "carId",
        onDelete = ForeignKey.CASCADE
))
public class Reminder {
    @PrimaryKey(autoGenerate = true)
    public int reminderId;

    @ColumnInfo
    public int carId;

    @ColumnInfo
    public String name;

    @ColumnInfo
    public int lastMileage;

    @ColumnInfo
    public String lastDate;

    @ColumnInfo
    public long interval;

    @ColumnInfo
    public String intervalType;

    public Reminder(int carId, String name) {
        this.carId = carId;
        this.name = name;
    }
}
