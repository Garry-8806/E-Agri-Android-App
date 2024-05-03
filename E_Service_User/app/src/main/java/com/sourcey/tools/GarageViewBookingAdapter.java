package com.sourcey.tools;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.sourcey.tools.model.ToolRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dinesh on 4/15/2018.
 */

public class GarageViewBookingAdapter extends ArrayAdapter {
    List<ToolRequest> list = new ArrayList<>();
    Context ctx;

    public GarageViewBookingAdapter(@NonNull Context context, int resource) {
        super(context, resource);
        this.ctx = context;
    }


    public void add(@Nullable ToolRequest object) {
        list.add(object);
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        GarageViewBookingHolder bookingHolder;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.single_garage_booking, parent, false);
            bookingHolder = new GarageViewBookingHolder();
            bookingHolder.txtMobile = (TextView) row.findViewById(R.id.txtMobile);
            bookingHolder.txtCost = (TextView) row.findViewById(R.id.txtCost);
            bookingHolder.txtName = (TextView) row.findViewById(R.id.txtSPName);
            bookingHolder.txtStatus = (TextView) row.findViewById(R.id.txtStatus);
            bookingHolder.txtToolName = (TextView) row.findViewById(R.id.txtToolName);
            bookingHolder.btnViewOnMap = (Button) row.findViewById(R.id.btnView);
            row.setTag(bookingHolder);
        } else {
            bookingHolder = (GarageViewBookingHolder) row.getTag();
        }
        try {
            final ToolRequest toolRequest = (ToolRequest) getItem(position);
            bookingHolder.txtToolName.setText(toolRequest.getTool_name());
            bookingHolder.txtName.setText(toolRequest.getCust_name());
            bookingHolder.txtStatus.setText(toolRequest.getRequest_status());
            bookingHolder.txtCost.setText("" + toolRequest.getCost());
            bookingHolder.txtMobile.setText(toolRequest.getMobile());
            bookingHolder.btnViewOnMap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
             try {
                 double lat = Double.parseDouble(toolRequest.getLat());
                 double lon = Double.parseDouble(toolRequest.getLon());
                 MapsActivity.lat = lat;
                 MapsActivity.lon = lon;
                 Intent intent = new Intent(ctx, MapsActivity.class);
                 ctx.startActivity(intent);
             }catch (Exception e)
             {
                 Toast.makeText(ctx, "LatLong Error", Toast.LENGTH_SHORT).show();
             }
                }
            });
            if (toolRequest.getRequest_status().equals("Completed") || toolRequest.getRequest_status().equals("Rejected")) {
                bookingHolder.btnViewOnMap.setVisibility(View.GONE);
            } else {
                bookingHolder.btnViewOnMap.setVisibility(View.VISIBLE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return row;
    }

    public class GarageViewBookingHolder {
        TextView txtCost, txtName, txtStatus, txtToolName, txtMobile;
        String returnTime = "";
        Button btnViewOnMap;
    }
}
