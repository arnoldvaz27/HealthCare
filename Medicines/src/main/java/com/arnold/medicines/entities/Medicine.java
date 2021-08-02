package com.arnold.medicines.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "medicines")
public class Medicine implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "name")
    private String Name;

    @ColumnInfo(name = "stock")
    private String Stock;

    @ColumnInfo(name = "stock_expiry")
    private String StockExpiry;

    @ColumnInfo(name = "stock_arrived")
    private String StockArrived;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getStock() {
        return Stock;
    }

    public void setStock(String stock) {
        Stock = stock;
    }

    public String getStockExpiry() {
        return StockExpiry;
    }

    public void setStockExpiry(String stockExpiry) {
        StockExpiry = stockExpiry;
    }

    public String getStockArrived() {
        return StockArrived;
    }

    public void setStockArrived(String stockArrived) {
        StockArrived = stockArrived;
    }

    @NonNull
    @Override
    public String toString() {
        return Name + " : " + Stock;
    }
}
