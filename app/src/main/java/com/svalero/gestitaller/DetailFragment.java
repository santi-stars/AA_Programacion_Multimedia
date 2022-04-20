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
    private ImageView imageView;
    private TextView textView1, textView2, textView3, textView4;
    private final String VIEW_BIKE_ACTIVITY = "ViewBikeActivity";
    private final String VIEW_CLIENT_ACTIVITY = "ViewClientActivity";
    private final String VIEW_ORDER_ACTIVITY = "ViewOrderActivity";

    public FloatingActionButton closeButton;

    public DetailFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View detailView = inflater.inflate(R.layout.fragment_order_detail, container, false);

        thisActivity = getActivity();
        if (thisActivity != null) {
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

        closeButton = detailView.findViewById(R.id.close_detail_button);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeDetails.hiddeDetails();
            }
        });

        return detailView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imageView = view.findViewById(R.id.fragment_detail_imageview);
        textView1 = view.findViewById(R.id.fragment_detail_textview1);
        textView2 = view.findViewById(R.id.fragment_detail_textview2);
        textView3 = view.findViewById(R.id.fragment_detail_textview3);
        textView4 = view.findViewById(R.id.fragment_detail_textview4);

        Log.i("oncreateview_thisActivity", thisActivity.toString());
        Log.i("oncreateview_activity", activity);
        switch (activity) {
            case VIEW_BIKE_ACTIVITY:
                // TODO fragment de detalles de motos para otro dia
                break;
            case VIEW_CLIENT_ACTIVITY:
                if (getArguments() != null) {
                    if (getArguments().getByteArray("client_image") != null)
                        imageView.setImageBitmap
                                (ImageUtils.getBitmap(getArguments().getByteArray("client_image")));
                    textView1.setText(getArguments().getString("name") + " " + getArguments().getString("surname"));
                    textView2.setText(getArguments().getString("dni"));
                    if (getArguments().getBoolean("vip")) {
                        textView3.setText(R.string.vip);
                    } else {
                        textView3.setText(R.string.no_vip);
                    }
                    //textView4.setVisibility(View.GONE);
                }
                break;
            case VIEW_ORDER_ACTIVITY:
                if (getArguments() != null) {
                    if (getArguments().getByteArray("bike_image") != null)
                    imageView.setImageBitmap
                            (ImageUtils.getBitmap(getArguments().getByteArray("bike_image")));
                    textView1.setText(DateUtils.fromLocalDateToMyDateFormatString
                            (LocalDate.parse(getArguments().getString("date"))));
                    textView2.setText(getArguments().getString("name"));
                    textView3.setText(getArguments().getString("model") + " || " + getArguments().getString("license"));
                    textView4.setText(getArguments().getString("description"));
                }
                break;
        }   // End switch
    }

    /**
     * Interfaz para comunicar el boton de cerrar con la clase desde la que se llama al fragment
     * sobreescribiendolo en esta
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