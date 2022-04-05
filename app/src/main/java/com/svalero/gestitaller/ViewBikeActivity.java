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

import com.svalero.gestitaller.adapters.BikeAdapter;
import com.svalero.gestitaller.adapters.ClientAdapter;
import com.svalero.gestitaller.database.AppDatabase;
import com.svalero.gestitaller.domain.Bike;
import com.svalero.gestitaller.domain.Client;

import java.util.ArrayList;
import java.util.Arrays;

public class ViewBikeActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    public ArrayList<Bike> bikes;
    public BikeAdapter bikeArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_bike);

        bikeList();
    }

    @Override
    protected void onResume() {
        super.onResume();

        bikeList();
    }

    private void bikeList() {

        bikes = new ArrayList<>();
        ListView bikesListView = findViewById(R.id.bike_lisview);
        registerForContextMenu(bikesListView);
        bikeArrayAdapter = new BikeAdapter(this, bikes);

        loadBikes();

        bikesListView.setAdapter(bikeArrayAdapter);
        bikesListView.setOnItemClickListener(this);

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

    private void loadBikes() {
        bikes.clear();
        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "bike").allowMainThreadQueries()
                .fallbackToDestructiveMigration().build();
        bikes.addAll(db.bikeDao().getAll());

        bikeArrayAdapter.notifyDataSetChanged();
    }

    public void addBike(View view) {
        Intent intent = new Intent(this, AddBikeActivity.class);
        startActivity(intent);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}