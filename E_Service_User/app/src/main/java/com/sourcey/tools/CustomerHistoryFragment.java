package com.sourcey.tools;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
public class CustomerHistoryFragment extends Fragment {
    PaymentHistoryAdapter paymentHistoryAdapter;
    JSONParser jsonParser = new JSONParser();
    ListView payment_list;
    View view;

    public CustomerHistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_customer_history, container, false);

        payment_list = (ListView) view.findViewById(R.id.payment_list);
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
            JSONObject object = jsonParser.makeHttpRequest(ServerUtility.url_get_customer_payment_history(), "GET", params);
            try {
                JSONArray array = object.getJSONArray("BookingInfo");
                paymentHistoryAdapter = new PaymentHistoryAdapter(CustomerHistoryFragment.this.getContext(), R.layout.single_payment_history);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject json = array.getJSONObject(i);
                    String id = json.getString("id");
                    String cust_name = json.getString("tool_name");
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

                    paymentHistoryAdapter.add(toolRequest);

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
                payment_list.setAdapter(paymentHistoryAdapter);
                if (paymentHistoryAdapter.getCount() == 0) {
                    Snackbar.make(view, "There is no any payment history", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

