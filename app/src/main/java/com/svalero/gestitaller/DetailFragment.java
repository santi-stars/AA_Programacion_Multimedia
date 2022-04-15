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
import com.svalero.gestitaller.util.ImageUtils;

public class DetailFragment extends Fragment {

    private closeDetails closeDetails;
    private byte[] bikeImage;
    private String date;
    private String clientName;
    private String bikeModel;
    private String description;
    private final String VIEW_BIKE_ACTIVITY = "com.svalero.gestitaller.ViewOrderActivity@24393";
    private final String VIEW_CLIENT_ACTIVITY = "com.svalero.gestitaller.ViewOrderActivity@24393f";
    private final String VIEW_ORDER_ACTIVITY = "com.svalero.gestitaller.ViewOrderActivity@24393f0";

    public FloatingActionButton closeButton;

    public DetailFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            bikeImage = getArguments().getByteArray("bike_image");
            date = getArguments().getString("date");
            clientName = getArguments().getString("name");
            bikeModel = getArguments().getString("model");
            description = getArguments().getString("description");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Activity thisActivity = getActivity();
        Log.i("activity", thisActivity.toString());
        View detailView = inflater.inflate(R.layout.fragment_order_detail, container, false);

        switch (thisActivity.toString()) {
            case VIEW_BIKE_ACTIVITY:
                // TODO crear un layout para cada fragment de detalles o borrar el swich
            case VIEW_CLIENT_ACTIVITY:
                // TODO crear un layout para cada fragment de detalles o borrar el swich
            case VIEW_ORDER_ACTIVITY:
                detailView = inflater.inflate(R.layout.fragment_order_detail, container, false);

        }

        closeButton = detailView.findViewById(R.id.close_detail_button);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeDetails.hiddeDetails();
            }
        });

        return detailView;
    }

    public interface closeDetails {
        void hiddeDetails();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof closeDetails) {
            closeDetails = (DetailFragment.closeDetails) context;
        } else {
            throw new RuntimeException(context.toString());
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (bikeImage != null) {
            ((ImageView) view.findViewById(R.id.order_detail_bike_imageview)).setImageBitmap(ImageUtils.getBitmap(bikeImage));
        }
        ((TextView) view.findViewById(R.id.order_date_detail_textview)).setText(date);
        ((TextView) view.findViewById(R.id.order_client_name_detail_textview)).setText(clientName);
        ((TextView) view.findViewById(R.id.order_bike_model_detail_textview)).setText(bikeModel);
        ((TextView) view.findViewById(R.id.order_description_detail_textview)).setText(description);

    }
}