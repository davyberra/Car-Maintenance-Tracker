package entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity
public class Vehicle {
    @PrimaryKey(autoGenerate = true)
    public int carId;

    @ColumnInfo
    public int mileage;

    @ColumnInfo
    public String year;

    @ColumnInfo
    public String make;

    @ColumnInfo
    public String model;

    public Vehicle(String year, String make, String model) {
        this.year = year;
        this.make = make;
        this.model = model;
    }
}
