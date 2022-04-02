package com.svalero.gestitaller.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.svalero.gestitaller.R;
import com.svalero.gestitaller.domain.Bike;
import com.svalero.gestitaller.util.ImageUtils;

import java.util.ArrayList;

public class BikeAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Bike> bikeArrayList;
    private LayoutInflater inflater;

    public BikeAdapter(Activity context, ArrayList<Bike> bikeArrayList) {
        this.context = context;
        this.bikeArrayList = bikeArrayList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Bike bike = (Bike) getItem(position);

        convertView = inflater.inflate(R.layout.client_and_bike_adapter, null);
        ImageView bikeImage = (ImageView) convertView.findViewById(R.id.client_bike_item_imageView);
        TextView bikemodel = convertView.findViewById(R.id.client_bike_tv1);
        TextView bikePlate = convertView.findViewById(R.id.client_bike_tv2);

        if (bike.getBikeImage() != null) {  // Valido si no es null la foto, si no sale fallo nullpoint...
            bikeImage.setImageBitmap(ImageUtils.getBitmap(bike.getBikeImage()));
        }
        bikemodel.setText(bike.getBrand() + " " + bike.getModel());
        bikePlate.setText(bike.getLicensePlate());

        return convertView;
    }

    @Override
    public int getCount() {
        return bikeArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return bikeArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
