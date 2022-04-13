package com.svalero.gestitaller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.svalero.gestitaller.adapters.ClientAdapter;
import com.svalero.gestitaller.database.AppDatabase;
import com.svalero.gestitaller.domain.Bike;
import com.svalero.gestitaller.domain.Client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

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
        registerForContextMenu(clientsListView);
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

        int x = 2;
        Collections.sort(clients, new Comparator<Client>() {
            @Override
            public int compare(Client o1, Client o2) {
                switch (x) {
                    case 1:
                        return o1.getName().compareToIgnoreCase(o2.getName());
                    case 2:
                        return o1.getSurname().compareToIgnoreCase(o2.getSurname());
                    default:
                        return new Integer(o1.getId()).compareTo(o2.getId());
                }
            }
        });
        clientArrayAdapter.notifyDataSetChanged();

    }

    /**
     * Método para cuando se crea el menu contextual, infle el menu con las opciones
     *
     * @param menu
     * @param v
     * @param menuInfo
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo
            menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        Log.i("inflate", "NO infla");
        getMenuInflater().inflate(R.menu.listview_menu, menu);
        Log.i("inflate", "SI infla");

    }

    /**
     * Opciones del menu contextual
     *
     * @param item
     * @return
     */
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        Intent intent = new Intent(this, AddClientActivity.class);

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        final int itemSelected = info.position;

        switch (item.getItemId()) {
            case R.id.modify_menu:                      // Modificar cliente
                Client client = clients.get(itemSelected);
                intent.putExtra("modify_client", true);

                Log.i("case_menu_id", String.valueOf(client.getId()));
                intent.putExtra("id", client.getId());

                Log.i("case_menu_imagen", Arrays.toString(client.getClientImage()));
                intent.putExtra("client_image", client.getClientImage());

                Log.i("case_menu_name", String.valueOf(client.getName()));
                intent.putExtra("name", client.getName());

                Log.i("case_menu_surname", String.valueOf(client.getSurname()));
                intent.putExtra("surname", client.getSurname());

                Log.i("case_menu_dni", String.valueOf(client.getDni()));
                intent.putExtra("dni", client.getDni());

                Log.i("case_menu_vip", String.valueOf(client.isVip()));
                intent.putExtra("vip", client.isVip());

                Log.i("case_menu_latitud", String.valueOf(client.getLatitude()));
                intent.putExtra("latitud", client.getLatitude());

                Log.i("case_menu_longitud", String.valueOf(client.getLongitude()));
                intent.putExtra("longitud", client.getLongitude());

                Log.i("client_intent", client.toString());

                startActivity(intent);
                return true;

            case R.id.detail_menu:                      // Detalles del cliente

                // Todo FALTA usar un fragment para mostrar una ficha con los detalles del cliente

                return true;

            case R.id.add_menu:                         // Añadir cliente
                startActivity(intent);
                return true;

            case R.id.delete_menu:                      // Eliminar cliente
                deleteClient(info);
                return true;

            default:
                return super.onContextItemSelected(item);
        }

    }

    private void deleteClient(AdapterView.AdapterContextMenuInfo info) {
        Client client = clients.get(info.position);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("¿Estás seguro de eliminar el cliente para siempre?")
                .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                                AppDatabase.class, "client").allowMainThreadQueries().build();
                        db.clientDao().delete(client);
                        clientList();
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }

    public void addClient(View view) {
        Intent intent = new Intent(this, AddClientActivity.class);
        startActivity(intent);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    }
}