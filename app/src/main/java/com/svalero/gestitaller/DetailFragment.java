package com.svalero.gestitaller;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.svalero.gestitaller.domain.Bike;
import com.svalero.gestitaller.domain.Client;
import com.svalero.gestitaller.domain.dto.OrderDTO;
import com.svalero.gestitaller.util.DateUtils;
import com.svalero.gestitaller.util.ImageUtils;

import java.time.LocalDate;

public class DetailFragment extends Fragment {

    private closeDetails closeDetails;
    private Activity thisActivity;
    private Bike bike;
    private Client client;
    private OrderDTO orderDTO;
    private String activity;
    private final String VIEW_BIKE_ACTIVITY = "???";
    private final String VIEW_CLIENT_ACTIVITY = "??????";
    private final String VIEW_ORDER_ACTIVITY = "ViewOrderActivity";

    public FloatingActionButton closeButton;

    public DetailFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        thisActivity = getActivity();
        if (thisActivity != null) {
            Log.i("oncreate_thisActivity", thisActivity.toString());
            if (thisActivity.toString().contains(VIEW_BIKE_ACTIVITY)) {
                bike = new Bike();
                activity = VIEW_BIKE_ACTIVITY;
            } else if (thisActivity.toString().contains(VIEW_CLIENT_ACTIVITY)) {
                client = new Client();
                activity = VIEW_CLIENT_ACTIVITY;
            } else if (thisActivity.toString().contains(VIEW_ORDER_ACTIVITY)) {
                orderDTO = new OrderDTO();
                activity = VIEW_ORDER_ACTIVITY;
            }
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.i("activity", thisActivity.toString());
        View detailView = inflater.inflate(R.layout.fragment_order_detail, container, false);

        switch (activity) {
            case VIEW_BIKE_ACTIVITY:
                // TODO crear un layout para cada fragment de detalles o borrar el swich
            case VIEW_CLIENT_ACTIVITY:
                // TODO crear un layout para cada fragment de detalles o borrar el swich
            case VIEW_ORDER_ACTIVITY:
                // detailView = inflater.inflate(R.layout.fragment_order_detail, container, false);
                Log.i("oncreate_case_order", String.valueOf(getArguments() == null));
                if (getArguments() != null) {
                    orderDTO.setBikeImageOrder(getArguments().getByteArray("bike_image"));
                    orderDTO.setDate(LocalDate.parse(getArguments().getString("date")));
                    orderDTO.setClientNameSurname(getArguments().getString("name"));
                    orderDTO.setBikeBrandModel(getArguments().getString("model"));
                    orderDTO.setBikeLicensePlate(getArguments().getString("license"));
                    orderDTO.setDescription(getArguments().getString("description"));
                }
                Log.i("oncreate_case_order", "orderDTO: " + orderDTO.toString());
            default:
                closeButton = detailView.findViewById(R.id.close_detail_button);
                closeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        closeDetails.hiddeDetails();
                    }
                });
        }   // End switch

        return detailView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        switch (activity) {
            case VIEW_BIKE_ACTIVITY:
                // TODO crear un layout para cada fragment de detalles o borrar el swich
            case VIEW_CLIENT_ACTIVITY:
                // TODO crear un layout para cada fragment de detalles o borrar el swich
            case VIEW_ORDER_ACTIVITY:
                if (orderDTO.getBikeImageOrder() != null)
                    ((ImageView) view.findViewById(R.id.order_detail_bike_imageview)).setImageBitmap
                            (ImageUtils.getBitmap(orderDTO.getBikeImageOrder()));
                if (orderDTO.getDate() != null) {
                    ((TextView) view.findViewById(R.id.order_date_detail_textview)).setText
                            (DateUtils.fromLocalDateToMyDateFormatString(orderDTO.getDate()));
                }
                ((TextView) view.findViewById(R.id.order_client_name_detail_textview)).setText(orderDTO.getClientNameSurname());
                ((TextView) view.findViewById(R.id.order_bike_model_detail_textview)).setText(orderDTO.getBikeBrandModel() + " || " + orderDTO.getBikeLicensePlate());
                ((TextView) view.findViewById(R.id.order_description_detail_textview)).setText(orderDTO.getDescription());
        }   // End switch

    }

    /**
     * Interfaz para comunicar el boton de cerrar con la clase desde la que se llama al fragment
     */
    public interface closeDetails {
        void hiddeDetails();
    }

    /**
     * Se llama cuando un fragmento se adjunta por primera vez a su contexto (La clase que lo llama).
     * {@link #onCreate(Bundle)} will be called after this.
     *
     * @param context
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof closeDetails) {
            closeDetails = (DetailFragment.closeDetails) context;
        } else {
            throw new RuntimeException(context.toString());
        }
    }
}