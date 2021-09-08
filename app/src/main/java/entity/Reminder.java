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
    public String lastMileage;

    @ColumnInfo
    public String lastDate;

    @ColumnInfo
    public int interval;

    @ColumnInfo
    public String intervalType;

    public Reminder(int carId, String name, String lastMileage, String lastDate, int interval, String intervalType) {
        this.carId = carId;
        this.name = name;
        this.lastMileage = lastMileage;
        this.lastDate = lastDate;
        this.interval = interval;
        this.intervalType = intervalType;
    }
}
