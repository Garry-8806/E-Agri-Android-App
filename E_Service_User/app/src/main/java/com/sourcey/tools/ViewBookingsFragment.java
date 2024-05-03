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

import com.sourcey.tools.model.ToolRequest;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ViewBookingsFragment extends Fragment {

    ListView booking_list;
    JSONParser jsonParser = new JSONParser();
    BookingAdapter bookingAdapter;

    public ViewBookingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_bookings, container, false);

        booking_list = (ListView) view.findViewById(R.id.booking_list);
        booking_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ToolRequest bookingInfo = (ToolRequest) bookingAdapter.getItem(i);
                //  Toast.makeText(ViewCustomerBookingFragment.this.getContext(), ""+bookingInfo.getVehicleno(), Toast.LENGTH_SHORT).show();
//                if (bookingInfo.getStatus().equals("Rejected")) {
//                    Intent intent = new Intent(getContext(), BookingDetailsActivity.class);
//                    intent.putExtra("valetnumber", bookingInfo.getVehicleno());
//                    intent.putExtra("valetassigned", bookingInfo.getName());
//                    //  intent.putExtra("amount", bookingInfo.getAmount());
//                    intent.putExtra("pickup_time", bookingInfo.getPickup_time());
//                    startActivity(intent);
//
//                } else {
//                    Intent intent = new Intent(getContext(), CurrentBookingActivity.class);
//                    intent.putExtra("valetnumber", bookingInfo.getVehicleno());
//                    intent.putExtra("valetassigned", bookingInfo.getName());
//                    intent.putExtra("amount", bookingInfo.getAmount());
//                    intent.putExtra("pickup_time", bookingInfo.getPickup_time());
//                    intent.putExtra("return_time", bookingInfo.getReturn_time());
//                    intent.putExtra("description", bookingInfo.getDescription());
//                    startActivity(intent);
//                }
            }
        });

        new GetBookingInfo().execute();
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
            JSONObject object = jsonParser.makeHttpRequest(ServerUtility.url_view_booking(), "POST", params);
            try {
                JSONArray array = object.getJSONArray("BookingInfo");
                bookingAdapter = new BookingAdapter(ViewBookingsFragment.this.getContext(), R.layout.single_booking);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject json = array.getJSONObject(i);
                    String id = json.getString("id");
                    String cust_name = ServerUtility.txtUsername;
                    String cust_email = json.getString("cust_email");
                    String sp_name = json.getString("sp_name");
                    String sp_email = json.getString("sp_email");
                    String tool_name = json.getString("tool_name");
                    String request_status = json.getString("request_status");
                    String request_on = json.getString("request_on");
                    String delivered_on = json.getString("delivered_on");
                    String return_on = json.getString("return_on");
                    double cost = json.getDouble("cost_per_hour");
                    double total_payment = json.getDouble("total_payment");
                    int hours=json.getInt("time_in_hour");
                    String lat=json.getString("latitude");
                    String longitude=json.getString("longitude");
                    ToolRequest toolRequest = new ToolRequest(id, cust_name, cust_email, sp_name, sp_email, tool_name, request_status, request_on, delivered_on, return_on, hours, total_payment, cost,lat,longitude);
                    bookingAdapter.add(toolRequest);

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
                booking_list.setAdapter(bookingAdapter);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
