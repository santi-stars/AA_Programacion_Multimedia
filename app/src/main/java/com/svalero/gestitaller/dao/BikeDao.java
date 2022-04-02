package com.svalero.gestitaller.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.svalero.gestitaller.domain.Bike;

import java.util.List;

@Dao
public interface BikeDao {

    @Query("SELECT * FROM bike")
    List<Bike> getAll();

    @Query("SELECT * FROM bike WHERE id = :id")
    List<Bike> getBikesById(int id);

    @Query("SELECT * FROM bike WHERE clientId = :clientId")
    List<Bike> getBikesByClientId(int clientId);

    @Insert
    void insert(Bike bike);

    @Update
    void update(Bike bike);

    @Delete
    void delete(Bike bike);
}
