package com.svalero.gestitaller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }

    public void addBike(View view) {
        Intent intent = new Intent(this, AddBikeActivity.class);
        startActivity(intent);
    }

    public void addClient (View view){
        Intent intent = new Intent(this, AddClientActivity.class);
        startActivity(intent);
    }

    public void addOrder (View view){
        Intent intent = new Intent(this, AddOrderActivity.class);
        startActivity(intent);
    }

    public void viewBike (View view){
        Intent intent = new Intent(this, ViewBikeActivity.class);
        startActivity(intent);
    }

    public void viewClient (View view){
        Intent intent = new Intent(this, ViewClientActivity.class);
        startActivity(intent);
    }

    public void viewOrder (View view){
        Intent intent = new Intent(this, ViewOrderActivity.class);
        startActivity(intent);
    }

}