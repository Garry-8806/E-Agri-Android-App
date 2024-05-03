package com.sourcey.tools;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class CurrentBookingActivity extends AppCompatActivity {

    TextView txtAmount, txtUsername, txtPickup_time, txtReturn_time, txtVehicleNo, txtValetName, txtDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_booking);
        txtAmount = (TextView) findViewById(R.id.txtpayment_amount);
        txtUsername = (TextView) findViewById(R.id.txtUsername);
        txtPickup_time = (TextView) findViewById(R.id.txtpickup_time);
        txtAmount = (TextView) findViewById(R.id.txtpayment_amount);
        txtReturn_time = (TextView) findViewById(R.id.txtReturnTime);
        txtVehicleNo = (TextView) findViewById(R.id.txtVehicleNumber);
        txtValetName = (TextView) findViewById(R.id.txtvalet_assigned);
        txtDescription = (TextView) findViewById(R.id.txtDescription);
        //get extras from currentbookingfragment
        String amount = getIntent().getExtras().getString("amount");
        String username = ServerUtility.txtUsername;
        String pickup = getIntent().getExtras().getString("pickup_time");
        String valet = getIntent().getExtras().getString("valetassigned");
        String return_time = getIntent().getExtras().getString("return_time");
        String vehicle = getIntent().getExtras().getString("valetnumber");
        String description = getIntent().getExtras().getString("description");
        txtDescription.setText(description);
        txtAmount.setText(amount);
        txtUsername.setText("Dear " + username + ",");
        txtPickup_time.setText(pickup);
        txtValetName.setText(valet);
        if (!return_time.equals("")) {
            txtReturn_time.setText(return_time);
        }
        txtVehicleNo.setText(vehicle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
