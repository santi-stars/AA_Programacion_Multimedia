package com.svalero.gestitaller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.room.Room;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import com.svalero.gestitaller.adapters.BikeAdapter;
import com.svalero.gestitaller.adapters.ClientAdapter;
import com.svalero.gestitaller.database.AppDatabase;
import com.svalero.gestitaller.domain.Bike;
import com.svalero.gestitaller.domain.Client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class ViewBikeActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    public ArrayList<Bike> bikes;
    public BikeAdapter bikeArrayAdapter;
    private String orderBy;
    private final String DEFAULT_STRING="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_bike);
        bikes = new ArrayList<>();
        orderBy = DEFAULT_STRING;

        bikeList();
    }

    @Override
    protected void onResume() {
        super.onResume();

        bikeList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bike_actionbar, menu);
        final MenuItem searchItem = menu.findItem(R.id.app_bar_bike_search);
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

    private void bikeList() {

        ListView bikesListView = findViewById(R.id.bike_lisview);
        registerForContextMenu(bikesListView);

        bikeArrayAdapter = new BikeAdapter(this, bikes);

        findBy(DEFAULT_STRING);

        bikesListView.setAdapter(bikeArrayAdapter);
        bikesListView.setOnItemClickListener(this);

    }

    private void findBy(String query) {
        bikes.clear();
        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "bike").allowMainThreadQueries()
                .fallbackToDestructiveMigration().build();

        if (query.equalsIgnoreCase(DEFAULT_STRING)) {
            bikes.addAll(db.bikeDao().getAll());
        } else {
            // TODO spinner con las opciones para buscar
            int j = 1;
            switch (j) {
                case 1:
                    bikes.addAll(db.bikeDao().getByBrandString("%" + query + "%"));
                    break;
                case 2:
                    bikes.addAll(db.bikeDao().getByModelString("%" + query + "%"));
                    break;
                case 3:
                    bikes.addAll(db.bikeDao().getByLicensePlateString("%" + query + "%"));
                    break;
            }
        }
        orderBy(orderBy);
    }

    private void orderBy(String orderBy) {
        this.orderBy = orderBy;

        Collections.sort(bikes, new Comparator<Bike>() {
            @Override
            public int compare(Bike o1, Bike o2) {
                switch (orderBy) {
                    case "brand":
                        return o1.getBrand().compareToIgnoreCase(o2.getBrand());
                    case "model":
                        return o1.getModel().compareToIgnoreCase(o2.getModel());
                    case "license_plate":
                        return o1.getLicensePlate().compareToIgnoreCase(o2.getLicensePlate());
                    default:
                        return String.valueOf(o1.getId()).compareTo(String.valueOf(o2.getId()));
                }
            }
        });
        bikeArrayAdapter.notifyDataSetChanged();
    }

    /**
     * Método para cuando se crea el menu contextual, infle el menu con las opciones
     *
     * @param menu
     * @param v
     * @param menuInfo
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        getMenuInflater().inflate(R.menu.listview_menu, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.order_by_default_item:
                orderBy("");
                return true;
            case R.id.order_by_brand_item:
                orderBy("brand");
                return true;
            case R.id.order_by_model_item:
                orderBy("model");
                return true;
            case R.id.order_by_license_plate_item:
                orderBy("license_plate");
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

        Intent intent = new Intent(this, AddBikeActivity.class);

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        final int itemSelected = info.position;

        switch (item.getItemId()) {
            case R.id.modify_menu:                      // Modificar moto
                Bike bike = bikes.get(itemSelected);
                intent.putExtra("modify_bike", true);

                Log.i("case_menu", String.valueOf(bike.getId()));
                intent.putExtra("id", bike.getId());

                Log.i("case_menu_imagen", Arrays.toString(bike.getBikeImage()));
                intent.putExtra("bike_image", bike.getBikeImage());

                Log.i("case_menu", String.valueOf(bike.getBrand()));
                intent.putExtra("brand", bike.getBrand());

                Log.i("case_menu", String.valueOf(bike.getModel()));
                intent.putExtra("model", bike.getModel());

                Log.i("case_menu", String.valueOf(bike.getLicensePlate()));
                intent.putExtra("license_plate", bike.getLicensePlate());

                Log.i("case_menu", String.valueOf(bike.getClientId()));
                intent.putExtra("clientId", bike.getClientId());

                Log.i("bike_intent", bike.toString());

                startActivity(intent);
                return true;

            case R.id.detail_menu:                      // Detalles de la moto

                // Todo FALTA usar un fragment para mostrar una ficha con los detalles de la moto

                return true;

            case R.id.add_menu:                         // Añadir moto
                startActivity(intent);
                return true;

            case R.id.delete_menu:                      // Eliminar moto
                deleteBike(info);
                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }

    private void deleteBike(AdapterView.AdapterContextMenuInfo info) {
        Bike bike = bikes.get(info.position);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("¿Estás seguro de eliminar la moto para siempre?")
                .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                                AppDatabase.class, "bike").allowMainThreadQueries().build();
                        db.bikeDao().delete(bike);
                        bikeList();
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

    public void addBike(View view) {
        Intent intent = new Intent(this, AddBikeActivity.class);
        startActivity(intent);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}