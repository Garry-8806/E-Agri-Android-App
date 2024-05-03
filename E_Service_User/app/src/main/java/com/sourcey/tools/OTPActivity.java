package com.sourcey.tools;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import es.dmoral.toasty.Toasty;

public class OTPActivity extends AppCompatActivity {
    TextView  txtUsername, txtPickup_time, txtReturn_time, txtToolName, txtCustomerName, txtMobile;
    JSONParser jsonParser = new JSONParser();
    EditText etOTP;
    Button btnUpdate;
    String otp = "";
    String booking_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        Date date = new Date();
        String time = simpleDateFormat.format(date);
        txtUsername = (TextView) findViewById(R.id.txtUsername);
        txtPickup_time = (TextView) findViewById(R.id.txtpickup_time);
        txtReturn_time = (TextView) findViewById(R.id.txtReturnTime);
        txtToolName = (TextView) findViewById(R.id.txtToolName);
        txtCustomerName = (TextView) findViewById(R.id.txtCustomerName);
        txtMobile = (TextView) findViewById(R.id.txtMobile);
        btnUpdate = (Button) findViewById(R.id.btnUpdate);
        etOTP = (EditText) findViewById(R.id.etOTP);
        booking_id = getIntent().getExtras().getString("id");
        String username = ServerUtility.username;
        String customername = getIntent().getExtras().getString("customername");
        String toolname = getIntent().getExtras().getString("toolname");
        String mobile = getIntent().getExtras().getString("mobile");

        txtUsername.setText("Dear " + username + ",");
        txtPickup_time.setText(time);
        txtCustomerName.setText(customername);

        txtToolName.setText(toolname);
        txtMobile.setText(mobile);
        //Toast.makeText(this, "OTP Send ON Customer's mobile number", Toast.LENGTH_SHORT).show();
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etOTP.getText().equals("")) {
                    Toast.makeText(OTPActivity.this, "Please enter OTP", Toast.LENGTH_SHORT).show();
                } else if (etOTP.getText().toString().equals(otp)) {
                    new ChangeStatus(booking_id, "Delivered").execute();
                } else {
                    Toast.makeText(OTPActivity.this, "Enter valid OTP", Toast.LENGTH_SHORT).show();
                }
            }
        });

        generateOTP();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    public void generateOTP() {
        Random rnd = new Random();
        otp = "" + rnd.nextInt(9999);
        while (otp.length() != 4) {
            otp = "" + rnd.nextInt(9999);
        }
        String message = "Share this OTP to SP for complete your tool delivery. OTP is " + otp;
        SendMessage sendMessage = new SendMessage(txtMobile.getText().toString(), message);
        Toast.makeText(this, "Please enter OTP and Update for tool delivery", Toast.LENGTH_SHORT).show();
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
            JSONObject object = jsonParser.makeHttpRequest(ServerUtility.url_change_request_status(), "GET", params);
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
                Toasty.success(OTPActivity.this, "Tool Delivered successfully..", Toast.LENGTH_SHORT).show();
                //   Toast.makeText(DeliveryActivity.this, "Vehicle Detailed Updated successfully..", Toast.LENGTH_SHORT).show();

            } else {
                Toasty.error(OTPActivity.this, "There is problem while changing status", Toast.LENGTH_SHORT).show();
            }
            Intent intent = new Intent(OTPActivity.this, GarageHomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }
}
