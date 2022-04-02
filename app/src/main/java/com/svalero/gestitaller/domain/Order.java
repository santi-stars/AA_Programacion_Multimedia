package com.svalero.gestitaller.domain;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.svalero.gestitaller.database.TimestampConverter;

import java.time.LocalDate;

@Entity
public class Order {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo
    @TypeConverters({TimestampConverter.class})
    private LocalDate date;
    @ColumnInfo
    private int clientId;
    @ColumnInfo
    private int bikeId;
    @ColumnInfo
    private String description;

    public Order(int id, LocalDate date, int clientId, int bikeId, String description) {
        this.id = id;
        this.date = date;
        this.clientId = clientId;
        this.bikeId = bikeId;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public int getBikeId() {
        return bikeId;
    }

    public void setBikeId(int bikeId) {
        this.bikeId = bikeId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
