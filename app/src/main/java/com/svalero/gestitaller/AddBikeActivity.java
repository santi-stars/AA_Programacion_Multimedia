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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.svalero.gestitaller.database.AppDatabase;
import com.svalero.gestitaller.domain.Bike;
import com.svalero.gestitaller.domain.Client;
import com.svalero.gestitaller.util.ImageUtils;

import java.util.ArrayList;

public class AddBikeActivity extends AppCompatActivity {

    private ImageView bikeImage;
    private Spinner clientSpinner;
    public ArrayList<Client> clients;
    private Client selectedClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bike);

        bikeImage = findViewById(R.id.bike_imageView);
        clientSpinner = findViewById(R.id.client_spinner_add_bike);

        clients = new ArrayList<>();
        rellenarSpinner();
    }

    public void addBike(View view) {
        EditText etBrand = findViewById(R.id.brand_edittext_add_bike);
        EditText etModel = findViewById(R.id.model_edittext_add_bike);
        EditText etLicensePlate = findViewById(R.id.license_plate_edittext_add_bike);

        String brand = etBrand.getText().toString().trim();
        String model = etModel.getText().toString().trim();
        String licensePlate = etLicensePlate.getText().toString().trim();
        selectedClient = clients.get(clientSpinner.getSelectedItemPosition());
        int clientId = selectedClient.getId();
        byte[] bikeImageByteArray = ImageUtils.fromImageViewToByteArray(bikeImage);

        if ((brand.equals("")) || (model.equals("")) || (licensePlate.equals(""))) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
            //  } else if (clientPosition[0] == 0) {
            //     Toast.makeText(this, "Selecciona un cliente", Toast.LENGTH_SHORT).show();
        } else {
            Bike bike = new Bike(0, brand, model, licensePlate, clientId, bikeImageByteArray);

            AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                    AppDatabase.class, "bike").allowMainThreadQueries().build();
            db.bikeDao().insert(bike);

            Toast.makeText(this, "Moto añadida", Toast.LENGTH_SHORT).show();
            bikeImage.setImageResource(R.drawable.ic_menu_camera);
            bikeImageByteArray = ImageUtils.fromImageViewToByteArray(bikeImage);
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
        clients.addAll(db.clientDao().getAll());
        String[] arraySpinner = new String[clients.size()];
        int i = 0;
        for (Client client : clients) {
            arraySpinner[i] = client.getName() + " " + client.getSurname();
            Log.i("spinner", arraySpinner[i]);
            i++;
        }
        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arraySpinner);
        clientSpinner.setAdapter(adapterSpinner);
        Log.i("spinner", arraySpinner.toString());
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