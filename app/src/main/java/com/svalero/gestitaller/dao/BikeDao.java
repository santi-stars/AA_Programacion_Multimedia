package com.svalero.gestitaller.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.svalero.gestitaller.domain.Bike;
import com.svalero.gestitaller.domain.Client;

import java.util.List;

@Dao
public interface BikeDao {

    @Query("SELECT * FROM bike")
    List<Bike> getAll();

    @Query("SELECT * FROM bike WHERE id = :id")
    Bike getBikeById(int id);

    @Query("SELECT * FROM bike WHERE clientId = :clientId")
    List<Bike> getBikesByClientId(int clientId);

    @Query("SELECT * FROM bike WHERE brand LIKE :query")
    List<Bike> getByBrandString(String query);

    @Query("SELECT * FROM bike WHERE model LIKE :query")
    List<Bike> getByModelString(String query);

    @Query("SELECT * FROM bike WHERE licensePlate LIKE :query")
    List<Bike> getByLicensePlateString(String query);

    @Insert
    void insert(Bike bike);

    @Update
    void update(Bike bike);

    @Delete
    void delete(Bike bike);
}
