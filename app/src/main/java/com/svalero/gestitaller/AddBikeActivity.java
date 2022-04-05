package com.svalero.gestitaller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.svalero.gestitaller.database.AppDatabase;
import com.svalero.gestitaller.domain.Bike;
import com.svalero.gestitaller.domain.Client;
import com.svalero.gestitaller.util.ImageUtils;

import java.util.ArrayList;
import java.util.Arrays;

public class AddBikeActivity extends AppCompatActivity {

    private Bike bike;
    // private Client selectedClient;

    private Button addButton;
    private ImageView bikeImage;
    private Spinner clientSpinner;
    private EditText etBrand;
    private EditText etModel;
    private EditText etLicensePlate;
    private Intent intent;

    private boolean modifyBike;
    public ArrayList<Client> clients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bike);

        bikeImage = findViewById(R.id.bike_imageView);
        etBrand = findViewById(R.id.brand_edittext_add_bike);
        etModel = findViewById(R.id.model_edittext_add_bike);
        etLicensePlate = findViewById(R.id.license_plate_edittext_add_bike);
        clientSpinner = findViewById(R.id.client_spinner_add_bike);
        addButton = findViewById(R.id.add_bike_button);
        bike = new Bike(0, "", "", "", 0, null);

        clients = new ArrayList<>();
        rellenarSpinner();
        intent();
    }


    @Override
    protected void onResume() {
        super.onResume();
        rellenarSpinner();
    }

    private void intent() {

        intent = getIntent();
        modifyBike = intent.getBooleanExtra("modify_bike", false);
        // Si se está editando la moto, obtiene los datos de la moto y los pinta en el formulario
        if (modifyBike) {
            bike.setId(intent.getIntExtra("id", 0));
            bike.setClientId(intent.getIntExtra("clientId", 0));

            bikeImage.setImageBitmap(ImageUtils.getBitmap(intent.getByteArrayExtra("bike_image")));
            etBrand.setText(intent.getStringExtra("brand"));
            etModel.setText(intent.getStringExtra("model"));
            etLicensePlate.setText(intent.getStringExtra("license_plate"));

            addButton.setText(R.string.modify_capital);
        } else {
            addButton.setText(R.string.add_button);
        }
    }

    public void addBike(View view) {

        bike.setBrand(etBrand.getText().toString().trim());
        bike.setModel(etModel.getText().toString().trim());
        bike.setLicensePlate(etLicensePlate.getText().toString().trim());
        //        selectedClient = clients.get(clientSpinner.getSelectedItemPosition());
        //bike.setClientId(selectedClient.getId());
        bike.setClientId(clients.get(clientSpinner.getSelectedItemPosition()).getId());

        bike.setBikeImage(ImageUtils.fromImageViewToByteArray(bikeImage));

        if ((bike.getBrand().equals("")) || (bike.getModel().equals("")) || (bike.getLicensePlate().equals(""))) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
        } else {

            AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                    AppDatabase.class, "bike").allowMainThreadQueries().build();

            if (modifyBike) {
                Log.i("modifyed_bike", bike.toString());
                modifyBike = false;
                addButton.setText(R.string.add_button);
                db.bikeDao().update(bike);
                Toast.makeText(this, "Moto modificada", Toast.LENGTH_SHORT).show();
            } else {
                bike.setId(0);
                Log.i("new_bike", bike.toString());
                db.bikeDao().insert(bike);
                Toast.makeText(this, "Moto añadida", Toast.LENGTH_SHORT).show();
            }

            bikeImage.setImageResource(R.drawable.ic_menu_camera);
            etBrand.setText("");
            etModel.setText("");
            etLicensePlate.setText("");

        }
    }

    private void rellenarSpinner() {
        // Llama a método que carga clientes de la base de datos
        clients.clear();
        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "client").allowMainThreadQueries()
                .fallbackToDestructiveMigration().build();
        clients.addAll(db.clientDao().getAll());    // Llama a la BBDD
        // Crea un array del tamaño de la lista de clientes
        String[] arraySpinner = new String[clients.size()];

        int i = 0;      // Rellena el spinner con el nombre y apellido de los clientes
        for (Client client : clients) {
            arraySpinner[i] = client.getName() + " " + client.getSurname();
            Log.i("spinner", arraySpinner[i]);
            i++;
        }

        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, arraySpinner);
        clientSpinner.setAdapter(adapterSpinner);
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
            bikeImage.setImageBitmap(imageBitmap);
        }
    }
}