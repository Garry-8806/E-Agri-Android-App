package com.sourcey.tools;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class SendRequestFragment extends Fragment implements OnMapReadyCallback {
    public static SPInfo spInfo;
    Button btnCall, btnMessage, btnPrebook;
    private GoogleMap mMap;
    double lat;
    double lon;
    CheckBox checkBox, checkBox1, checkBox2, checkBox3, checkBox4;
    SupportMapFragment mapFragment;
    JSONParser jsonParser = new JSONParser();

    public SendRequestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_send_request, container, false);
        btnCall = (Button) view.findViewById(R.id.btnCall);
        checkBox = (CheckBox) view.findViewById(R.id.service1);
        checkBox1 = (CheckBox) view.findViewById(R.id.service2);
        checkBox2 = (CheckBox) view.findViewById(R.id.service3);
        checkBox3 = (CheckBox) view.findViewById(R.id.service4);
        checkBox4 = (CheckBox) view.findViewById(R.id.service5);
        btnMessage = (Button) view.findViewById(R.id.btnMessage);
        btnMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = "Call back..";
                SendMessage sendMessage = new SendMessage(spInfo.getMobile().toString().trim(), message);
            }
        });
        btnPrebook = (Button) view.findViewById(R.id.btnPrebook);
        btnPrebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CarDetailsFragment carDetailsFragment = new CarDetailsFragment();
                FragmentManager manager = getFragmentManager();
                manager.beginTransaction()
                        .replace(R.id.content_customer_home, carDetailsFragment, carDetailsFragment.getTag())
                        .commit();

            }
        });

        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + spInfo.getMobile().toString().trim()));
                if (ActivityCompat.checkSelfPermission(SendRequestFragment.this.getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                getContext().startActivity(callIntent);
            }
        });
        GPSTracker gpsTracker = new GPSTracker(getContext());
        if (gpsTracker.canGetLocation()) {
            lat = gpsTracker.getLatitude();
            lon = gpsTracker.getLongitude();
        }
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment == null) {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            mapFragment = SupportMapFragment.newInstance();
            ft.replace(R.id.map, mapFragment).commit();
        }
        mapFragment.getMapAsync(this);
        new GetServices(spInfo.getEmail().toString().trim()).execute();
        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera

        LatLng sydney = new LatLng(lat, lon);
        mMap.addMarker(new MarkerOptions().position(sydney).title("My Location").icon(bitmapDescriptorFromVector(getActivity(), R.drawable.ic_location_on_black_24dp)));
        try {
            LatLng sydney2 = new LatLng(spInfo.getLat(), spInfo.getLon());
            mMap.addMarker(new MarkerOptions().position(sydney2).title(spInfo.getUsername()).snippet(spInfo.getMobile()).icon(bitmapDescriptorFromVector(getActivity(), R.drawable.ic_local_car_wash_black_24dp)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 12.0f));
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
    public class GetServices extends AsyncTask<String, String, String> {
        List<NameValuePair> params = new ArrayList<>();
        JSONObject object;
        boolean flag = false;
        String email="";

        public GetServices(String email) {
            this.email = email;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            params.add(new BasicNameValuePair("email", this.email));

        }

        @Override
        protected String doInBackground(String... strings) {
            object = jsonParser.makeHttpRequest(ServerUtility.url_view_garage_services(), "GET", params);
            flag = object.has("success");
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (flag) {
                try {
                    checkBox.setChecked(object.getBoolean("service1"));
                    checkBox1.setChecked(object.getBoolean("service2"));
                    checkBox2.setChecked(object.getBoolean("service3"));
                    checkBox3.setChecked(object.getBoolean("service4"));
                    checkBox4.setChecked(object.getBoolean("service5"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
