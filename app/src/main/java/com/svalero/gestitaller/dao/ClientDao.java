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
    Client getClientById(int id);

    @Query("SELECT * FROM client WHERE name LIKE :query")
    List<Client> getByNameString(String query);

    @Query("SELECT * FROM client WHERE surname LIKE :query")
    List<Client> getBySurnameString(String query);

    @Query("SELECT * FROM client WHERE dni LIKE :query")
    List<Client> getByDniString(String query);

    @Insert
    void insert(Client client);

    @Update
    void update(Client client);

    @Delete
    void delete(Client client);
}
