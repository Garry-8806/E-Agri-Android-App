package com.sourcey.tools;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class BookingDetailsActivity extends AppCompatActivity {

    TextView txtvehicle_number, txtpayment_amount, txtvalet_assigned, txtpickup_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_details);
        txtvehicle_number = (TextView) findViewById(R.id.txtVehicleNumber);
        txtpayment_amount = (TextView) findViewById(R.id.txtpayment_amount);
        txtpickup_time = (TextView) findViewById(R.id.txtpickup_time);
        txtvalet_assigned = (TextView) findViewById(R.id.txtvalet_assigned);

        //get Extras from ViewCurrentBookingFragment
        String vehiclenumber=getIntent().getExtras().getString("valetnumber");
        // String amount=getIntent().getExtras().getString("amount");
        String pickup_time=getIntent().getExtras().getString("pickup_time");
        String valet_assigned=getIntent().getExtras().getString("valetassigned");
        txtvehicle_number.setText(vehiclenumber);
        txtpickup_time.setText(pickup_time);
        //   txtpayment_amount.setText(amount);
        txtvalet_assigned.setText(valet_assigned);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
