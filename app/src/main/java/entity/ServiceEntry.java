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
public class ServiceEntry {
    @PrimaryKey(autoGenerate = true)
    public int serviceId;

    @ColumnInfo
    public int carId;

    @ColumnInfo
    public String category;

    @ColumnInfo
    public String serviceDescription;

    @ColumnInfo
    public long date;

    @ColumnInfo
    public double cost;

    @ColumnInfo
    public String location;

    public ServiceEntry(int carId, String category, String serviceDescription, long date, double cost, String location) {
        this.carId = carId;
        this.category = category;
        this.serviceDescription = serviceDescription;
        this.date = date;
        this.cost = cost;
        this.location = location;
    }
}
