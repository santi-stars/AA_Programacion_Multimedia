package com.svalero.gestitaller.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.room.Room;

import com.svalero.gestitaller.R;
import com.svalero.gestitaller.database.AppDatabase;
import com.svalero.gestitaller.domain.Bike;
import com.svalero.gestitaller.domain.Order;
import com.svalero.gestitaller.domain.dto.OrderDTO;
import com.svalero.gestitaller.util.DateUtils;
import com.svalero.gestitaller.util.ImageUtils;

import java.util.ArrayList;

public class OrderAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<OrderDTO> orderArrayList;
    private LayoutInflater inflater;

    public OrderAdapter(Activity context, ArrayList<OrderDTO> orderArrayList) {
        this.context = context;
        this.orderArrayList = orderArrayList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        OrderDTO orderDTO = (OrderDTO) getItem(position);

        convertView = inflater.inflate(R.layout.client_and_bike_adapter, null);
        ImageView orderBikeImage = convertView.findViewById(R.id.client_bike_item_imageView);
        TextView orderDateAndBikeModel = convertView.findViewById(R.id.client_bike_tv1);
        TextView orderLicensePlateAndDescription = convertView.findViewById(R.id.client_bike_tv2);

        orderDateAndBikeModel.setTextSize(22);
        orderLicensePlateAndDescription.setTextSize(18);

        if (orderDTO.getBikeImageOrder() != null) {  // Valido si no es null la foto, si no sale fallo nullpoint...
            orderBikeImage.setImageBitmap(ImageUtils.getBitmap(orderDTO.getBikeImageOrder()));
        }
        orderDateAndBikeModel.setText(DateUtils.fromLocalDateToMyDateFormatString(orderDTO.getDate()) + " || " + orderDTO.getBikeBrandModel());
        orderLicensePlateAndDescription.setText(orderDTO.getBikeLicensePlate() + " || " + orderDTO.getDescription());


        return convertView;
    }

    @Override
    public int getCount() {
        return orderArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return orderArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
