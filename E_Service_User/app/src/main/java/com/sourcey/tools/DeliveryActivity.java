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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import es.dmoral.toasty.Toasty;

public class DeliveryActivity extends AppCompatActivity {
    TextView txtUsername, txtPickup_time, txtReturn_time, txtToolName, txtCustomerName, txtMobile, txtPayment;
    JSONParser jsonParser = new JSONParser();
    EditText etOTP;
    Button btnUpdate;
    String otp = "";
    String booking_id = "";
    int total_hours = 0;
    double cost_per_hour = 0;
    double total_payment = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        txtUsername = (TextView) findViewById(R.id.txtUsername);
        txtPayment = (TextView) findViewById(R.id.txtpayment_amount);
        txtPickup_time = (TextView) findViewById(R.id.txtpickup_time);
        txtReturn_time = (TextView) findViewById(R.id.txtReturnTime);
        txtToolName = (TextView) findViewById(R.id.txtToolName);
        txtCustomerName = (TextView) findViewById(R.id.txtCustomerName);
        txtMobile = (TextView) findViewById(R.id.txtMobile);
        btnUpdate = (Button) findViewById(R.id.btnUpdate);
        etOTP = (EditText) findViewById(R.id.etOTP);
        booking_id = getIntent().getExtras().getString("id");
        String username = ServerUtility.txtUsername;
        String customername = getIntent().getExtras().getString("customername");
        String toolname = getIntent().getExtras().getString("toolname");
        String mobile = getIntent().getExtras().getString("mobile");
        final String cust_email = getIntent().getExtras().getString("cust_email");
        String delievered_on = getIntent().getExtras().getString("delivered_on");
        String return_on = getIntent().getExtras().getString("return_on");
        cost_per_hour = getIntent().getExtras().getDouble("cost_per_hour");
        if (return_on.equals("")) {
            Calendar cal = Calendar.getInstance();

            return_on = simpleDateFormat.format(cal.getTime());
        }
        txtUsername.setText("Dear " + username + ",");
        txtPickup_time.setText(delievered_on);
        txtCustomerName.setText(customername);
        txtReturn_time.setText(return_on);
        txtToolName.setText(toolname);
        txtMobile.setText(mobile);
        //Toast.makeText(this, "OTP Send ON Customer's mobile number", Toast.LENGTH_SHORT).show();
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etOTP.getText().equals("")) {
                    Toast.makeText(DeliveryActivity.this, "Please enter OTP", Toast.LENGTH_SHORT).show();
                } else if (etOTP.getText().toString().equals(otp)) {
                    new ChangeStatus(booking_id, "Completed", cust_email, ServerUtility.txtEmail, txtReturn_time.getText().toString(), "" + total_hours, txtPayment.getText().toString()).execute();
                } else {
                    Toast.makeText(DeliveryActivity.this, "Enter valid OTP", Toast.LENGTH_SHORT).show();
                }
            }
        });

        try {
            Date d1 = simpleDateFormat.parse(delievered_on);
            Date d2 = simpleDateFormat.parse(return_on);
            total_hours = hoursDifference(d2, d1);
            total_payment = total_hours * cost_per_hour;
            txtPayment.setText("" + total_payment);
        } catch (Exception e) {
            e.printStackTrace();
        }
        generateOTP();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    private static int hoursDifference(Date date1, Date date2) {

        final int MILLI_TO_HOUR = 1000 * 60 * 60;
        return (int) (date1.getTime() - date2.getTime()) / MILLI_TO_HOUR;
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
        String id, status, cust_email, sp_email, return_on, hours, amount;

        public ChangeStatus(String id, String status, String cust_email, String sp_email, String return_on, String hours, String amount) {
            this.id = id;
            this.status = status;
            this.cust_email = cust_email;
            this.sp_email = sp_email;
            this.return_on = return_on;
            this.hours = hours;
            this.amount = amount;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            params.add(new BasicNameValuePair("id", id));
            params.add(new BasicNameValuePair("status", status));
            params.add(new BasicNameValuePair("hours", hours));
            params.add(new BasicNameValuePair("amount", amount));
            params.add(new BasicNameValuePair("return_on", return_on));
            params.add(new BasicNameValuePair("cust_email", cust_email));
            params.add(new BasicNameValuePair("sp_email", sp_email));

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
                Toasty.success(DeliveryActivity.this, "Tool Delivered successfully..", Toast.LENGTH_SHORT).show();
                //   Toast.makeText(DeliveryActivity.this, "Vehicle Detailed Updated successfully..", Toast.LENGTH_SHORT).show();

            } else {
                Toasty.error(DeliveryActivity.this, "There is problem while changing status", Toast.LENGTH_SHORT).show();
            }
            Intent intent = new Intent(DeliveryActivity.this, GarageHomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }
}