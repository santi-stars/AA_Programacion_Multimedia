package com.svalero.gestitaller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.room.Room;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.svalero.gestitaller.database.AppDatabase;
import com.svalero.gestitaller.domain.Client;
import com.svalero.gestitaller.util.ImageUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddClientActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {

    private boolean vip = false;
    private GoogleMap map;
    private Marker marker;
    private float[] clientPosition = {0, 0};
    private Switch vipSwitch;
    private ImageView clientImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_client);

        vipSwitch = findViewById(R.id.vip_switch_add_client);
        clientImage = findViewById(R.id.client_imageView);

        // Permisos para la camara y almacenar en el dispositivo
        if (ContextCompat.checkSelfPermission(AddClientActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(AddClientActivity.this,
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AddClientActivity.this, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1000
            );
        }

        // Carga el fragment del mapa de Google
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

    }

    public void addClient(View view) {
        EditText etName = findViewById(R.id.name_edittext_add_client);
        EditText etSurname = findViewById(R.id.surname_edittext_add_client);
        EditText etDni = findViewById(R.id.dni_edittext_add_client);

        String name = etName.getText().toString().trim();
        String surname = etSurname.getText().toString().trim();
        String dni = etDni.getText().toString().trim();
        byte[] clientImageByteArray = ImageUtils.fromImageViewToByteArray(clientImage);

        if ((name.equals("")) || (surname.equals("")) || (dni.equals(""))) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
        } else if (clientPosition[0] == 0) {
            Toast.makeText(this, "Selecciona una posición en el mapa", Toast.LENGTH_SHORT).show();
        } else {
            Client client = new Client(0, name, surname, dni, vip, clientPosition[0], clientPosition[1], clientImageByteArray);

            AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                    AppDatabase.class, "client").allowMainThreadQueries().build();
            db.clientDao().insert(client);

            Toast.makeText(this, "Cliente añadido", Toast.LENGTH_SHORT).show();
            clientImage.setImageResource(R.drawable.ic_menu_camera);
            clientImageByteArray = ImageUtils.fromImageViewToByteArray(clientImage);

            etName.setText("");
            etSurname.setText("");
            etDni.setText("");
            vipSwitch.setChecked(false);

            clientPosition[0] = 0;
            clientPosition[1] = 0;
            marker.remove();

        }
    }

    /**
     * Cambia el texto del switch y el valor de la variable booleana VIP, que define si es VIP o no
     *
     * @param view
     */
    public void switchVip(View view) {

        if (vipSwitch.isChecked()) {
            vipSwitch.setText(R.string.vip);
            vip = true;
        } else {
            vipSwitch.setText(R.string.no_vip);
            vip = false;
        }

    }

    //Método para tomar foto
    public void takePhoto(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, 1);
        }
    }

    // Muestra la vista previa en un imageWiev de la foto tomada
    static final int REQUEST_IMAGE_CAPTURE = 1;

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            clientImage.setImageBitmap(imageBitmap);
        }
    }


    /**
     * Se ejecuta cuando esté el mapa cargado y asi poder interactuar con el
     *
     * @param googleMap
     */
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        map = googleMap;    // Asignamos el mapa pasado por parámetro a nuestra variable de tipo GoogleMap
        googleMap.setOnMapClickListener(this);  // Establecemos un listener de click sencillo para el mapa

    }

    @Override
    public void onMapClick(@NonNull LatLng latLng) {

        if (marker != null) {   // Si el marker NO está vacio,
            marker.remove();    // lo borramos para asignarle las coordenadas del click
        }

        marker = map.addMarker(new MarkerOptions().position(latLng));
        clientPosition[0] = (float) latLng.latitude;    // Asignamos las coordenadas del marker a la
        clientPosition[1] = (float) latLng.longitude;   // posición (Dirección) del cliente

    }
}