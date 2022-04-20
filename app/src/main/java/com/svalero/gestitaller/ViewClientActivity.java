package com.svalero.gestitaller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.room.Room;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;

import com.svalero.gestitaller.adapters.ClientAdapter;
import com.svalero.gestitaller.database.AppDatabase;
import com.svalero.gestitaller.domain.Client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ViewClientActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, DetailFragment.closeDetails {

    public ArrayList<Client> clients;
    public ClientAdapter clientArrayAdapter;
    private String orderBy;
    private FrameLayout frameLayout;
    public Spinner findSpinner;
    private final String[] FIND_SPINNER_OPTIONS = new String[]{"Nombre", "Apellido", "Dni"};
    private final String DEFAULT_STRING = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_client);

        clients = new ArrayList<>();
        frameLayout = findViewById(R.id.frame_layout_client);
        findSpinner = findViewById(R.id.find_spinner_view_client);
        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, FIND_SPINNER_OPTIONS);
        findSpinner.setAdapter(adapterSpinner);
        orderBy = DEFAULT_STRING;

        clientList();
    }

    @Override
    protected void onResume() {
        super.onResume();

        clientList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.client_actionbar, menu);
        final MenuItem searchItem = menu.findItem(R.id.app_bar_client_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                findBy(query.trim());
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                findBy(newText.trim());
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    public void clientList() {

        ListView clientsListView = findViewById(R.id.client_lisview);
        registerForContextMenu(clientsListView);

        clientArrayAdapter = new ClientAdapter(this, clients);

        findBy(DEFAULT_STRING);

        clientsListView.setAdapter(clientArrayAdapter);
        clientsListView.setOnItemClickListener(this);

    }

    private void findBy(String query) {
        clients.clear();
        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "client").allowMainThreadQueries()
                .fallbackToDestructiveMigration().build();

        if (query.equalsIgnoreCase(DEFAULT_STRING)) {
            clients.addAll(db.clientDao().getAll());
        } else {
            switch (findSpinner.getSelectedItemPosition()) {
                case 0:
                    clients.addAll(db.clientDao().getByNameString("%" + query + "%"));
                    break;
                case 1:
                    clients.addAll(db.clientDao().getBySurnameString("%" + query + "%"));
                    break;
                case 2:
                    clients.addAll(db.clientDao().getByDniString("%" + query + "%"));
                    break;
            }
        }
        orderBy(orderBy);
    }

    private void orderBy(String orderBy) {
        this.orderBy = orderBy;

        Collections.sort(clients, new Comparator<Client>() {
            @Override
            public int compare(Client o1, Client o2) {
                switch (orderBy) {
                    case "name":
                        return o1.getName().compareToIgnoreCase(o2.getName());
                    case "surname":
                        return o1.getSurname().compareToIgnoreCase(o2.getSurname());
                    case "dni":
                        return o1.getDni().compareToIgnoreCase(o2.getDni());
                    default:
                        return String.valueOf(o1.getId()).compareTo(String.valueOf(o2.getId()));
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

        getMenuInflater().inflate(R.menu.listview_menu, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.order_by_default_item:
                orderBy("");
                return true;
            case R.id.order_by_name_item:
                orderBy("name");
                return true;
            case R.id.order_by_surname_item:
                orderBy("surname");
                return true;
            case R.id.order_by_dni_item:
                orderBy("dni");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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

        switch (item.getItemId()) {
            case R.id.modify_menu:                      // Modificar cliente
                Client client = clients.get(info.position);

                intent.putExtra("modify_client", true);
                intent.putExtra("id", client.getId());
                intent.putExtra("client_image", client.getClientImage());
                intent.putExtra("name", client.getName());
                intent.putExtra("surname", client.getSurname());
                intent.putExtra("dni", client.getDni());
                intent.putExtra("vip", client.isVip());
                intent.putExtra("latitud", client.getLatitude());
                intent.putExtra("longitud", client.getLongitude());

                startActivity(intent);
                return true;
            case R.id.detail_menu:                      // Detalles del cliente
                showDetails(info.position);
                return true;
            case R.id.add_menu:                         // Añadir cliente
                startActivity(intent);
                return true;
            case R.id.delete_menu:                      // Eliminar cliente
                deleteClient(info);
                return true;
            default:
                return super.onContextItemSelected(item);
        }   // End switch
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

    private void showDetails(int position) {

        Client client = clients.get(position);

        Bundle datos = new Bundle();
        datos.putByteArray("client_image", client.getClientImage());
        datos.putString("name", client.getName());
        datos.putString("surname", client.getSurname());
        datos.putString("dni", client.getDni());
        datos.putBoolean("vip", client.isVip());
        datos.putFloat("latitude", client.getLatitude());
        datos.putFloat("longitude", client.getLongitude());

        DetailFragment detailFragment = new DetailFragment();
        detailFragment.setArguments(datos);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.client_detail, detailFragment)
                .commit();

        frameLayout.setVisibility(View.VISIBLE);
    }

    public void addClient(View view) {
        Intent intent = new Intent(this, AddClientActivity.class);
        startActivity(intent);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        showDetails(position);
    }

    @Override
    public void hiddeDetails(){
        frameLayout.setVisibility(View.GONE);
    }
}