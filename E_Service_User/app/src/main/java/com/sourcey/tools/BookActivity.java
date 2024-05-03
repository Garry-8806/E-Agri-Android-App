package com.sourcey.tools;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BookActivity extends AppCompatActivity {
    TextView txtvehicle, txtpickup, txtValetAmount;
    String vehicleNo, pickup, lat, lon, userid, email, mobile;
    Button btnConfirm;
    CheckBox checkBox;
    ProgressDialog dialog;
    JSONParser jsonParser = new JSONParser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        txtValetAmount = (TextView) findViewById(R.id.txtValetAmount);

        final String valetAmount = ServerUtility.getDefaults(ServerUtility.VALET, BookActivity.this);
        txtValetAmount.setText("(₹ " + valetAmount + " )");
        btnConfirm = (Button) findViewById(R.id.btnConfirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkBox.isChecked()) {
                    new ConfirmBooking().execute();

                } else {
                    Snackbar.make(view, "Please accept terms and conditions", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });
        checkBox = (CheckBox) findViewById(R.id.termscheckbox);


        txtpickup = (TextView) findViewById(R.id.txtPickupTime);
        txtvehicle = (TextView) findViewById(R.id.txtVehicleNumber);
        //  String vehicle=getIntent().getExtras().getString("vehicleno");
        lat = getIntent().getExtras().getString("lat");
        lon = getIntent().getExtras().getString("lon");
        userid = ServerUtility.txtEmail;
        email = getIntent().getExtras().getString("email");
        vehicleNo = getIntent().getExtras().getString("vehicleno");
        pickup = getIntent().getExtras().getString("pickup");
        mobile = getIntent().getExtras().getString("mobile");
        txtvehicle.setText(vehicleNo);
        txtpickup.setText(pickup);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    public class ConfirmBooking extends AsyncTask<String, String, String> {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        boolean flag = false;
        String url = ServerUtility.url_confirm_booking();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(BookActivity.this);
            dialog.setMessage("Please wait..");
            dialog.setCancelable(false);
            dialog.setIndeterminate(false);
            dialog.show();
            params.add(new BasicNameValuePair("email", email));
            params.add(new BasicNameValuePair("userid", userid));
            params.add(new BasicNameValuePair("pickup", pickup));
            params.add(new BasicNameValuePair("mobile", mobile));
            params.add(new BasicNameValuePair("vehicleno", vehicleNo));
            params.add(new BasicNameValuePair("lat", lat));
            params.add(new BasicNameValuePair("lon", lon));
        }

        @Override
        protected String doInBackground(String... strings) {
            JSONObject jsonObject = jsonParser.makeHttpRequest(url, "GET", params);
            flag = jsonObject.has(ServerUtility.TAG_SUCCESS);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();
            if (flag) {
                Toast.makeText(BookActivity.this, "Booking Confirmed...", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(BookActivity.this, "It Seems Vehicle Already in Garage", Toast.LENGTH_SHORT).show();
            }
            Intent intent = new Intent(getApplicationContext(), CustomerHomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }
}
