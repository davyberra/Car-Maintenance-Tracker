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
public class GasEntry {
    @PrimaryKey(autoGenerate = true)
    public int gasEntryId;

    @ColumnInfo
    public int carId;

    @ColumnInfo
    public String date;

    @ColumnInfo
    public float gallons;

    @ColumnInfo
    public float totalPrice;

    @ColumnInfo
    public float pricePerGallon;

    @ColumnInfo
    public int totalMileage;

    public GasEntry(String date, int carId, float gallons, float totalPrice, float pricePerGallon, int totalMileage) {
        this.date = date;
        this.carId = carId;
        this.gallons = gallons;
        this.totalPrice = totalPrice;
        this.pricePerGallon = pricePerGallon;
        this.totalMileage = totalMileage;
    }
}
