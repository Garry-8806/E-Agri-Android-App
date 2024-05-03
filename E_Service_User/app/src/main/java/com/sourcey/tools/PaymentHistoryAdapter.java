package com.sourcey.tools;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.sourcey.tools.model.ToolRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dinesh on 5/2/2018.
 */

public class PaymentHistoryAdapter extends ArrayAdapter {
    List<ToolRequest> list = new ArrayList();
    JSONParser jsonParser = new JSONParser();
    Context ctx;
    private int mYear, mMonth, mDay, mHour, mMinute;

    public PaymentHistoryAdapter(@NonNull Context context, int resource) {
        super(context, resource);
        this.ctx = context;
    }


    public void add(@Nullable ToolRequest object) {
        list.add(object);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        final CustomerBookingHolder bookingHolder;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.single_payment_history, parent, false);
            bookingHolder = new CustomerBookingHolder();
            bookingHolder.txtVehicleNo = (TextView) row.findViewById(R.id.txtToolName);
            bookingHolder.txtHours = (TextView) row.findViewById(R.id.txtHours);
            bookingHolder.txtPickup = (TextView) row.findViewById(R.id.pickup_time);
            bookingHolder.txtReturn = (TextView) row.findViewById(R.id.return_time);
            bookingHolder.txtValetAssigned = (TextView) row.findViewById(R.id.txtvalet_assigned);
            bookingHolder.txtAmount = (TextView) row.findViewById(R.id.txtAmount);
            bookingHolder.txtDate = (TextView) row.findViewById(R.id.txtDate);
            row.setTag(bookingHolder);
        } else {
            bookingHolder = (CustomerBookingHolder) row.getTag();
        }
        try {
            final ToolRequest bookingInfo = (ToolRequest) getItem(position);
            bookingHolder.txtVehicleNo.setText(bookingInfo.getTool_name());
            bookingHolder.txtDate.setText(bookingInfo.getRequest_on());
            bookingHolder.txtPickup.setText(bookingInfo.getDelivered_on());
            if (!bookingInfo.getReturn_on().equals("")) {
                bookingHolder.txtReturn.setText(bookingInfo.getReturn_on());
            }
            bookingHolder.txtValetAssigned.setText(bookingInfo.getSp_name());
            bookingHolder.txtAmount.setText("" + bookingInfo.getTotal_cost());
            bookingHolder.txtHours.setText("" + bookingInfo.getHours());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return row;
    }


    public static class CustomerBookingHolder {
        TextView txtValetAssigned, txtVehicleNo, txtPickup, txtReturn, txtAmount, txtDate, txtHours;
        String return_time = "";
    }


}
