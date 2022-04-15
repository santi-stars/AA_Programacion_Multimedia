package com.svalero.gestitaller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.svalero.gestitaller.database.AppDatabase;
import com.svalero.gestitaller.domain.Bike;
import com.svalero.gestitaller.domain.Client;
import com.svalero.gestitaller.domain.Order;
import com.svalero.gestitaller.util.DateUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

public class AddOrderActivity extends AppCompatActivity {

    private Spinner clientSpinner;
    private Spinner bikeSpinner;
    private EditText etDescription;
    private TextView tvDate;
    private Button addButton;
    private Intent intent;

    private Order order;
    private ArrayList<Client> clients;
    private ArrayList<Bike> bikes;
    private boolean modifyOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_order);

        clientSpinner = findViewById(R.id.client_spinner_add_order);
        bikeSpinner = findViewById(R.id.bike_spinner_add_order);
        etDescription = findViewById(R.id.description_edittext_add_order);
        tvDate = findViewById(R.id.date_textlabel_add_order);
        addButton = findViewById(R.id.add_order_button);

        order = new Order(0, null, 0, 0, "");

        clients = new ArrayList<>();
        bikes = new ArrayList<>();
        rellenarSpinners(0);
        intent();

        clientSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i("id_spinner_client", "position " + position + ", id " + id);  // BORRAR
                Log.i("id_spinner_client", String.valueOf(clients.get(clientSpinner.getSelectedItemPosition()).getId()));   // BORRAR
                rellenarSpinners(clients.get(clientSpinner.getSelectedItemPosition()).getId());
                Log.i("id_spinner_bike", String.valueOf(bikeSpinner.getCount()));   // BORRAR
                if (bikeSpinner.getCount() > 0) {                                           // BORRAR
                    Log.i("id_spinner_bike", String.valueOf(bikes.get(bikeSpinner.getSelectedItemPosition()).getId())); // BORRAR
                }                                                               // BORRAR
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        rellenarSpinners(0);
    }

    public void selectDate(View view) {
        final Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String stringMonth = String.valueOf(month + 1);
                if ((month + 1) < 10) {
                    stringMonth = "0" + stringMonth;
                }
                tvDate.setText(dayOfMonth + "/" + stringMonth + "/" + year);
            }
        }, year, month, day);
        datePickerDialog.show();
    }

    private void intent() {

        intent = getIntent();
        modifyOrder = intent.getBooleanExtra("modify_order", false);

        if (modifyOrder) {  // Si se edita una moto, obtiene sus datos y los pinta en el formulario
            order.setId(intent.getIntExtra("id", 0));
            tvDate.setText(String.valueOf(DateUtils.fromMyDateFormatStringToLocalDate
                    (intent.getStringExtra("date"))));
            etDescription.setText(intent.getStringExtra("description"));

            addButton.setText(R.string.modify_capital);
        }
    }

    public void addOrder(View view) {

        order.setDate(DateUtils.fromMyDateFormatStringToLocalDate(tvDate.getText().toString().trim()));
        order.setDescription(etDescription.getText().toString().trim());

        if (bikeSpinner.getCount() == 0) {
            Toast.makeText(this, "Selecciona una moto", Toast.LENGTH_SHORT).show();
        } else if ((order.getDescription().equals("")) || (String.valueOf(order.getDate()).equals(""))) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
        } else {
            order.setClientId(clients.get(clientSpinner.getSelectedItemPosition()).getId());
            order.setBikeId(bikes.get(bikeSpinner.getSelectedItemPosition()).getId());

            AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                    AppDatabase.class, "order").allowMainThreadQueries().build();

            if (modifyOrder) {
                Log.i("modifyed_order", order.toString());
                modifyOrder = false;
                addButton.setText(R.string.add_button);
                db.orderDao().update(order);
                Toast.makeText(this, "Orden modificada", Toast.LENGTH_SHORT).show();
            } else {
                order.setId(0);
                Log.i("new_order", order.toString());
                db.orderDao().insert(order);
                Toast.makeText(this, "Orden añadida", Toast.LENGTH_SHORT).show();
            }

            etDescription.setText("");
            tvDate.setText("");

        }
    }

    /**
     * Rellena los spinner para añadirlos a la orden
     *
     * @param idClient 0 rellena el spinner de clients con todos ellos,
     *                 > 0 rellena el spinner de bikes con las motos del id enviado por parametro
     */
    private void rellenarSpinners(int idClient) {

        if (idClient == 0) {

            clients.clear();

            AppDatabase dbClient = Room.databaseBuilder(getApplicationContext(),
                    AppDatabase.class, "client").allowMainThreadQueries()
                    .fallbackToDestructiveMigration().build();
            clients.addAll(dbClient.clientDao().getAll());

            // Crea un array del tamaño de la lista de clientes
            String[] arrayClientSpinner = new String[clients.size()];

            int i = 0;      // Rellena el spinner con el nombre y apellido de los clientes
            for (Client client : clients) {
                arrayClientSpinner[i] = client.getName() + " " + client.getSurname();
                Log.i("spinner_client", arrayClientSpinner[i]);
                i++;
            }

            ArrayAdapter<String> adapterClientSpinner = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, arrayClientSpinner);
            clientSpinner.setAdapter(adapterClientSpinner);

        } else {    // BIKE

            bikes.clear();

            AppDatabase dbBike = Room.databaseBuilder(getApplicationContext(),
                    AppDatabase.class, "bike").allowMainThreadQueries()
                    .fallbackToDestructiveMigration().build();
            bikes.addAll(dbBike.bikeDao().getBikesByClientId(idClient));

            String[] arrayBikeSpinner = new String[bikes.size()];

            int j = 0;      // Rellena el spinner con la marca y modelo de las motos
            for (Bike bike : bikes) {
                arrayBikeSpinner[j] = bike.getBrand() + " " + bike.getModel();
                Log.i("spinner_bike", arrayBikeSpinner[j]);
                j++;
            }

            ArrayAdapter<String> adapterBikeSpinner = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, arrayBikeSpinner);
            bikeSpinner.setAdapter(adapterBikeSpinner);

        }
    }
}
