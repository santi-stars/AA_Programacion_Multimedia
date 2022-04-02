package com.svalero.gestitaller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.svalero.gestitaller.adapters.ClientAdapter;
import com.svalero.gestitaller.database.AppDatabase;
import com.svalero.gestitaller.domain.Client;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class ViewClientActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    public ArrayList<Client> clients;
    public ClientAdapter clientArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_client);

        clientList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        clientList();
    }

    public void clientList() {

        clients = new ArrayList<>();
        ListView clientsListView = findViewById(R.id.client_lisview);
        clientArrayAdapter = new ClientAdapter(this, clients);
        loadClients();
        clientsListView.setAdapter(clientArrayAdapter);
        clientsListView.setOnItemClickListener(this);

    }

    private void loadClients() {

        clients.clear();

        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "client").allowMainThreadQueries()
                .fallbackToDestructiveMigration().build();

        clients.addAll(db.clientDao().getAll());

        clientArrayAdapter.notifyDataSetChanged();

    }

    public void addClient(View view){
        Intent intent = new Intent(this, AddClientActivity.class);
        startActivity(intent);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(this, "Eres feo y en tu casa no lo saben o no te lo quieren decir", Toast.LENGTH_SHORT).show();
    }
}