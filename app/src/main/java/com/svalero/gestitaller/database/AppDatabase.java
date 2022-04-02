package com.svalero.gestitaller.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.svalero.gestitaller.dao.BikeDao;
import com.svalero.gestitaller.dao.ClientDao;
import com.svalero.gestitaller.dao.OrderDao;
import com.svalero.gestitaller.domain.Bike;
import com.svalero.gestitaller.domain.Client;
import com.svalero.gestitaller.domain.Order;

@Database(entities = {Bike.class, Client.class, Order.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract BikeDao bikeDao();

    public abstract ClientDao clientDao();

    public abstract OrderDao orderDao();

}
