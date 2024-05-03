package com.sourcey.tools;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.sourcey.tools.model.ToolRequest;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ViewGarageBookingFragment extends Fragment {
    GarageViewBookingAdapter garageViewBookingAdapter;

    ListView booking_list;
    JSONParser jsonParser = new JSONParser();

    public ViewGarageBookingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_garage_booking, container, false);
        booking_list = (ListView) view.findViewById(R.id.garage_booking_list);
        booking_list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                ToolRequest toolRequest = (ToolRequest) garageViewBookingAdapter.getItem(i);
                if (toolRequest.getRequest_status().equals("Accepted")) {

                    //send OTP TO Farmer for confirmation

                    Intent intent = new Intent(ViewGarageBookingFragment.this.getContext(), OTPActivity.class);
                    intent.putExtra("toolname", toolRequest.getTool_name());
                    intent.putExtra("customername", toolRequest.getCust_name());
                    intent.putExtra("mobile", toolRequest.getMobile());
                    intent.putExtra("amount", toolRequest.getTotal_cost());
                    intent.putExtra("id", toolRequest.getId());
                    startActivity(intent);
                } else if (toolRequest.getRequest_status().equals("Completed")) {
                    // Toast.makeText(ViewGarageBookingFragment.this.getContext(), "Vehicle Already Delivered", Toast.LENGTH_SHORT).show();

                } else if (toolRequest.getRequest_status().equals("Delivered") || toolRequest.getRequest_status().equals("Return Requested")) {
                    //Toast.makeText(ViewGarageBookingFragment.this.getContext(), " Already Delivered", Toast.LENGTH_SHORT).show();
                    //send OTP to farmer and pickup the tool
                    Intent intent = new Intent(ViewGarageBookingFragment.this.getContext(), DeliveryActivity.class);
                    intent.putExtra("toolname", toolRequest.getTool_name());
                    intent.putExtra("customername", toolRequest.getCust_name());
                    intent.putExtra("cust_email", toolRequest.getCust_email());
                    intent.putExtra("mobile", toolRequest.getMobile());
                    intent.putExtra("delivered_on", toolRequest.getDelivered_on());
                    intent.putExtra("cost_per_hour", toolRequest.getCost());
                    intent.putExtra("return_on", toolRequest.getReturn_on());
                    intent.putExtra("id", toolRequest.getId());
                    startActivity(intent);
                } else if (toolRequest.getRequest_status().equals("Rejected")) {
                    Toast.makeText(ViewGarageBookingFragment.this.getContext(), "Tool Request Rejected by you", Toast.LENGTH_SHORT).show();
                }

                return false;
            }
        });
        new GetBookingInfo().execute();
        Toast.makeText(ViewGarageBookingFragment.this.getContext(), "Press long to update status", Toast.LENGTH_SHORT).show();
        return view;
    }

    public class GetBookingInfo extends AsyncTask<String, String, String> {
        List<NameValuePair> params = new ArrayList<>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            params.add(new BasicNameValuePair("username", ServerUtility.txtEmail));
        }

        @Override
        protected String doInBackground(String... strings) {
            JSONObject object = jsonParser.makeHttpRequest(ServerUtility.url_view_garage_bookings(), "POST", params);
            try {
                JSONArray array = object.getJSONArray("BookingInfo");
                garageViewBookingAdapter = new GarageViewBookingAdapter(ViewGarageBookingFragment.this.getContext(), R.layout.single_garage_booking);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject json = array.getJSONObject(i);
                    String id = json.getString("id");
                    String cust_name = json.getString("username");
                    String cust_email = json.getString("cust_email");
                    String sp_name = ServerUtility.txtUsername;
                    String sp_email = json.getString("sp_email");
                    String tool_name = json.getString("tool_name");
                    String request_status = json.getString("request_status");
                    String request_on = json.getString("request_on");
                    String delivered_on = json.getString("delivered_on");
                    String return_on = json.getString("return_on");
                    double cost = json.getDouble("cost_per_hour");
                    double total_payment = json.getDouble("total_payment");
                    int hours = json.getInt("time_in_hour");
                    String lat = json.getString("latitude");
                    String longitude = json.getString("longitude");
                    String mobile = json.getString("mobile");
                    ToolRequest toolRequest = new ToolRequest(id, cust_name, cust_email, sp_name, sp_email, tool_name, request_status, request_on, delivered_on, return_on, hours, total_payment, cost, lat, longitude);
                    toolRequest.setMobile(mobile);
                    garageViewBookingAdapter.add(toolRequest);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                booking_list.setAdapter(garageViewBookingAdapter);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
