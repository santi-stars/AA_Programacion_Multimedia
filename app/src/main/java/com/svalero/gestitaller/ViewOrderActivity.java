package com.svalero.gestitaller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.room.Room;

import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.SearchView;

import com.svalero.gestitaller.adapters.OrderAdapter;
import com.svalero.gestitaller.database.AppDatabase;
import com.svalero.gestitaller.domain.Bike;
import com.svalero.gestitaller.domain.Client;
import com.svalero.gestitaller.domain.Order;
import com.svalero.gestitaller.domain.dto.OrderDTO;
import com.svalero.gestitaller.util.DateUtils;
import com.svalero.gestitaller.util.ImageUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class ViewOrderActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, DetailFragment.closeDetails {

    public ArrayList<OrderDTO> ordersDTOArrayList;
    public ArrayList<Order> ordersArrayList;
    public ArrayList<Bike> bikesArrayList;
    public ArrayList<Client> clientsArrayList;
    public OrderAdapter orderDTOArrayAdapter;
    private OrderDTO orderDTO;
    private Bike bike;
    private Client client;
    private Order order;
    private FrameLayout frameLayout;
    private String orderBy;
    private final String DEFAULT_STRING = "";
    private AppDatabase dbBike, dbClient, dbOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_order);

        bike = new Bike();
        client = new Client();
        ordersArrayList = new ArrayList<>();
        ordersDTOArrayList = new ArrayList<>();
        frameLayout = findViewById(R.id.frame_layout);
        orderBy = DEFAULT_STRING;

        dbBike = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "bike").allowMainThreadQueries()
                .fallbackToDestructiveMigration().build();
        dbClient = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "client").allowMainThreadQueries()
                .fallbackToDestructiveMigration().build();
        dbOrder = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "order").allowMainThreadQueries()
                .fallbackToDestructiveMigration().build();

        orderList();

    }

    @Override
    protected void onResume() {
        super.onResume();

        orderList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.order_actionbar, menu);
        final MenuItem searchItem = menu.findItem(R.id.app_bar_order_search);
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

    private void orderList() {

        ListView ordersListView = findViewById(R.id.order_listview);
        registerForContextMenu(ordersListView);

        orderDTOArrayAdapter = new OrderAdapter(this, ordersDTOArrayList);

        findBy(DEFAULT_STRING);

        ordersListView.setAdapter(orderDTOArrayAdapter);
        ordersListView.setOnItemClickListener(this);
    }

    private void findBy(String query) {
        loadOrdersDTO();
        orderDTO = new OrderDTO();
        // TODO spinner con las opciones para buscar    COMPROBAR!!!
        String j = "date";
        switch (j) {
            case "date":
                ordersDTOArrayList.removeIf
                        (orderDTO -> (!String.valueOf(orderDTO.getDate()).contains(query)));
                break;
            case "clien_name":
                ordersDTOArrayList.removeIf
                        (orderDTO -> (!orderDTO.getClientNameSurname().contains(query)));
                break;
            case "bike_model":
                ordersDTOArrayList.removeIf
                        (orderDTO -> (!orderDTO.getBikeBrandModel().contains(query)));
                break;
            case "license_plate":
                ordersDTOArrayList.removeIf
                        (orderDTO -> (!orderDTO.getBikeLicensePlate().contains(query)));
                break;
        }
        orderBy(orderBy);
    }

    private void orderBy(String orderBy) {
        this.orderBy = orderBy;

        Collections.sort(ordersDTOArrayList, new Comparator<OrderDTO>() {
            @Override
            public int compare(OrderDTO o1, OrderDTO o2) {
                switch (orderBy) {
                    case "date":
                        return String.valueOf(o1.getDate()).compareToIgnoreCase(String.valueOf(o2.getDate()));
                    case "client_name":
                        return o1.getClientNameSurname().compareToIgnoreCase(o2.getClientNameSurname());
                    case "license_plate":
                        return o1.getBikeLicensePlate().compareToIgnoreCase(o2.getBikeLicensePlate());
                    case "bike_model":
                        return o1.getBikeBrandModel().compareToIgnoreCase(o2.getBikeBrandModel());
                    default:
                        return String.valueOf(o1.getId()).compareTo(String.valueOf(o2.getId()));
                }
            }
        });
        orderDTOArrayAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.order_by_default_item:
                orderBy("");
                return true;
            case R.id.order_by_date_item:
                orderBy("date");
                return true;
            case R.id.order_by_client_item:
                orderBy("client_name");
                return true;
            case R.id.order_by_license_plate_item:
                orderBy("license_plate");
                return true;
            case R.id.order_by_bike_model_item:
                orderBy("bike_model");
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void loadOrdersDTO() {
        ordersDTOArrayList.clear();
        ordersArrayList.clear();

        ordersArrayList.addAll(dbOrder.orderDao().getAll());

        for (Order order : ordersArrayList) {
            client = dbClient.clientDao().getClientById(order.getClientId());
            bike = dbBike.bikeDao().getBikeById(order.getBikeId());
            orderDTO = new OrderDTO();

            orderDTO.setId(order.getId());
            orderDTO.setDate(order.getDate());
            orderDTO.setClientNameSurname(client.getName() + " " + client.getSurname());
            orderDTO.setBikeBrandModel(bike.getBrand() + " " + bike.getModel());
            orderDTO.setBikeLicensePlate(bike.getLicensePlate());
            orderDTO.setBikeImageOrder(bike.getBikeImage());
            orderDTO.setDescription(order.getDescription());

            ordersDTOArrayList.add(orderDTO);
        }
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

        Intent intent = new Intent(this, AddOrderActivity.class);

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        final int itemSelected = info.position;

        switch (item.getItemId()) {
            case R.id.modify_menu:                      // Modificar moto
                Order order = ordersArrayList.get(itemSelected);
                intent.putExtra("modify_order", true);

                Log.i("case_menu_id", String.valueOf(order.getId()));
                intent.putExtra("id", order.getId());

                Log.i("case_menu_date", String.valueOf(order.getDate()));
                intent.putExtra("date", String.valueOf(order.getDate()));

                Log.i("case_menu_description", order.getDescription());
                intent.putExtra("description", order.getDescription());

                Log.i("case_menu_bikeId", String.valueOf(order.getBikeId()));
                intent.putExtra("bikeId", order.getBikeId());

                Log.i("case_menu_clientId", String.valueOf(order.getClientId()));
                intent.putExtra("clientId", order.getClientId());

                Log.i("order_intent", order.toString());

                startActivity(intent);
                return true;

            case R.id.detail_menu:                      // Detalles de la moto
                showDetails(itemSelected);
                return true;

            case R.id.add_menu:                         // Añadir moto
                startActivity(intent);
                return true;

            case R.id.delete_menu:                      // Eliminar moto
                deleteOrder(info);
                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }

    private void deleteOrder(AdapterView.AdapterContextMenuInfo info) {
        Order order = ordersArrayList.get(info.position);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("¿Estás seguro de eliminar la orden para siempre?")
                .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                                AppDatabase.class, "order").allowMainThreadQueries().build();
                        db.orderDao().delete(order);
                        orderList();
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

        OrderDTO orderDTO = ordersDTOArrayList.get(position);

        Bundle datos = new Bundle();
        datos.putByteArray("bike_image", orderDTO.getBikeImageOrder());
        datos.putString("date", String.valueOf(orderDTO.getDate()));
        datos.putString("name", orderDTO.getClientNameSurname());
        datos.putString("model", orderDTO.getBikeBrandModel());
        datos.putString("description", orderDTO.getDescription());

        DetailFragment detailFragment = new DetailFragment();
        detailFragment.setArguments(datos);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.order_detail, detailFragment)
                .commit();

        frameLayout.setVisibility(View.VISIBLE);
    }


    public void addOrder(View view) {
        Intent intent = new Intent(this, AddOrderActivity.class);
        startActivity(intent);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        showDetails(position);
    }

    @Override
    public void hiddeDetails() {
        frameLayout.setVisibility(View.GONE);
    }
}