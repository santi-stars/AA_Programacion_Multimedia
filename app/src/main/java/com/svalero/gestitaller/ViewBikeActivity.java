package com.svalero.gestitaller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.svalero.gestitaller.adapters.BikeAdapter;
import com.svalero.gestitaller.adapters.ClientAdapter;
import com.svalero.gestitaller.database.AppDatabase;
import com.svalero.gestitaller.domain.Bike;
import com.svalero.gestitaller.domain.Client;

import java.util.ArrayList;

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

        bikeArrayAdapter = new BikeAdapter(this, bikes);

        loadBikes();

        bikesListView.setAdapter(bikeArrayAdapter);
        bikesListView.setOnItemClickListener(this);

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