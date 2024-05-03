package com.sourcey.tools;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
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

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dinesh on 4/13/2018.
 */

public class GarageHomeAdapter extends ArrayAdapter {
    List<ToolRequest> list = new ArrayList<>();
    Context ctx;
    JSONParser jsonParser = new JSONParser();

    public GarageHomeAdapter(@NonNull Context context, int resource) {

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
        BookingHolder bookingHolder;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.single_garage_home, parent, false);
            bookingHolder = new BookingHolder();
            bookingHolder.txtCost = (TextView) row.findViewById(R.id.txtCost);
            bookingHolder.txtName = (TextView) row.findViewById(R.id.txtSPName);
            bookingHolder.txtStatus = (TextView) row.findViewById(R.id.txtStatus);
            bookingHolder.txtToolName = (TextView) row.findViewById(R.id.txtToolName);
//            bookingHolder.txtRequestBack = (TextView) row.findViewById(R.id.txtReturnRequest);
            bookingHolder.btnAccept = (Button) row.findViewById(R.id.btnAccept);
            bookingHolder.btnReject = (Button) row.findViewById(R.id.btnReject);
            bookingHolder.btnViewOnMap = (Button) row.findViewById(R.id.btnView);
            row.setTag(bookingHolder);
        } else {
            bookingHolder = (BookingHolder) row.getTag();
        }
        try {
            final ToolRequest toolRequest = (ToolRequest) getItem(position);
            bookingHolder.txtToolName.setText(toolRequest.getTool_name());
            bookingHolder.txtName.setText(toolRequest.getCust_name());
            bookingHolder.txtStatus.setText(toolRequest.getRequest_status());
            bookingHolder.txtCost.setText("" + toolRequest.getCost());
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
                    } catch (Exception e) {
                        Toast.makeText(ctx, "LatLong Errror", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            bookingHolder.btnAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new ChangeStatus(toolRequest.getId(), "Accepted").execute();
                }
            });
            bookingHolder.btnReject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new ChangeStatus(toolRequest.getId(), "Rejected").execute();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
        return row;
    }

    public static class BookingHolder {
        TextView txtCost, txtName, txtStatus, txtToolName;
        String returnTime = "";
        Button btnAccept, btnReject, btnViewOnMap;
    }

    public class ChangeStatus extends AsyncTask<String, String, String> {
        List<NameValuePair> params = new ArrayList<>();
        boolean flag = false;
        String id, status;

        public ChangeStatus(String id, String status) {
            this.id = id;
            this.status = status;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            params.add(new BasicNameValuePair("id", id));
            params.add(new BasicNameValuePair("status", status));
        }

        @Override
        protected String doInBackground(String... strings) {
            JSONObject object = jsonParser.makeHttpRequest(ServerUtility.url_change_request_status(), "POST", params);
            try {
                flag = object.has(ServerUtility.TAG_SUCCESS);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if (flag) {
                Toast.makeText(ctx, "Request " + status, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ctx, GarageHomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                ctx.startActivity(intent);
            } else {
                Toast.makeText(ctx, "There is problem while changing status", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
