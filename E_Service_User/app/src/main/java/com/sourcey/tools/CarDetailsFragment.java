package com.sourcey.tools;


import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.sql.Time;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class CarDetailsFragment extends Fragment {

    EditText etVehicleNumebr, etMobileNumber;
    Button btnPickupTime, btnContinue;
    private int mYear, mMonth, mDay, mHour, mMinute;
    TextView txtName, txtAddress;

    public CarDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_car_details, container, false);
        etVehicleNumebr = (EditText) view.findViewById(R.id.etVehicleNumber);
        etMobileNumber = (EditText) view.findViewById(R.id.etMobile);
        btnContinue = (Button) view.findViewById(R.id.btnContinue);
        btnPickupTime = (Button) view.findViewById(R.id.btnPickup);
        btnPickupTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(CarDetailsFragment.this.getContext(),
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                btnPickupTime.setText(getTime(hourOfDay, minute));
                            }
                        }, mHour, mMinute + 5, false);
                timePickerDialog.show();
            }
        });

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btnPickupTime.getText().toString().equals("Pickup Time")) {
                    Snackbar.make(view, "Please select pickup time", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    return;
                }
                if (etMobileNumber.getText().toString().equals("") || etVehicleNumebr.getText().toString().equals("")) {
                    Snackbar.make(view, "Please enter all details", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    return;
                }
                Intent intent = new Intent(CarDetailsFragment.this.getContext(), BookActivity.class);
                intent.putExtra("vehicleno", etVehicleNumebr.getText().toString().toUpperCase());
                intent.putExtra("mobile", etMobileNumber.getText().toString());
                intent.putExtra("pickup", btnPickupTime.getText().toString());
                intent.putExtra("email", SendRequestFragment.spInfo.getEmail());
                intent.putExtra("lat", ""+ServerUtility.latitude);
                intent.putExtra("lon", ""+ServerUtility.longitude);
                startActivity(intent);
            }
        });
        return view;
    }

    private String getTime(int hr, int min) {
        Time tme = new Time(hr, min, 0);//seconds by default set to zero
        Format formatter;
        formatter = new SimpleDateFormat("h:mm a");
        return formatter.format(tme);
    }
}
