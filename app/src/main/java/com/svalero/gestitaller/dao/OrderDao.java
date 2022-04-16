package com.svalero.gestitaller.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.svalero.gestitaller.domain.Client;
import com.svalero.gestitaller.domain.Order;

import java.util.List;

@Dao
public interface OrderDao {

    @Query("SELECT * FROM `order`")
    List<Order> getAll();

    @Query("SELECT * FROM `order` WHERE date LIKE :query")
    List<Order> getByDate(String query);

    @Query("SELECT * FROM `order` WHERE description LIKE :query")
    List<Order> getByDescriptionString(String query);

    @Insert
    void insert(Order order);

    @Update
    void update(Order order);

    @Delete
    void delete(Order order);
}
