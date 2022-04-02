package com.svalero.gestitaller.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.svalero.gestitaller.domain.Client;

import java.util.List;

@Dao
public interface ClientDao {

    @Query("SELECT * FROM client")
    List<Client> getAll();

    @Query("SELECT * FROM client WHERE id = :id")
    List<Client> getClientsById(int id);

    @Insert
    void insert(Client client);

    @Update
    void update(Client client);

    @Delete
    void delete(Client client);
}
