package com.sourcey.tools;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ViewGarageFragment extends Fragment implements OnMapReadyCallback {
    private GoogleMap mMap;
    double lat;
    double lon;
    SupportMapFragment mapFragment;
    JSONParser jsonParser = new JSONParser();
    List<SPInfo> spInfos = new ArrayList<SPInfo>();

    public ViewGarageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_garage, container, false);
        GPSTracker gpsTracker = new GPSTracker(getContext());
        if (gpsTracker.canGetLocation()) {
            lat = gpsTracker.getLatitude();
            lon = gpsTracker.getLongitude();
            ServerUtility.latitude = lat;
            ServerUtility.longitude = lon;
        }
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment == null) {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            mapFragment = SupportMapFragment.newInstance();
            ft.replace(R.id.map, mapFragment).commit();
        }
        new GetSPInfo().execute();
        return view;

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        //  new GetSPInfo().execute();
        LatLng sydney = new LatLng(lat, lon);
        mMap.addMarker(new MarkerOptions().position(sydney).title("My Location").icon(bitmapDescriptorFromVector(getActivity(),R.drawable.ic_location_on_black_24dp)));
        for (int i = 0; i < spInfos.size(); i++) {
            SPInfo spInfo = spInfos.get(i);
            LatLng sydney2 = new LatLng(spInfo.getLat(), spInfo.getLon());
            mMap.addMarker(new MarkerOptions().position(sydney2).title(spInfo.getUsername()).snippet(spInfo.getMobile()).icon(bitmapDescriptorFromVector(getActivity(),R.drawable.ic_local_car_wash_black_24dp)));
        }

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 12.0f));

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Toast.makeText(getContext(), "" + marker.getTitle(), Toast.LENGTH_SHORT).show();
                if (marker.getTitle().equals("My Location")) {
                } else {
                    String mobile = marker.getSnippet();
                    for (int i = 0; i < spInfos.size(); i++) {
                        SPInfo spInfo = spInfos.get(i);
                        if (mobile.equals(spInfo.getMobile())) {

                            SendRequestFragment.spInfo = spInfo;
                            SendRequestFragment sendRequestFragment = new SendRequestFragment();
                            FragmentManager manager = getFragmentManager();
                            manager.beginTransaction()
                                    .replace(R.id.content_customer_home, sendRequestFragment, sendRequestFragment.getTag())
                                    .commit();
                        }
                    }
                }
              /*  SelectSpFragment selectSpFragment = new SelectSpFragment();
                FragmentManager manager = getFragmentManager();
                manager.beginTransaction()
                        .replace(R.id.content_customer_home, selectSpFragment, selectSpFragment.getTag())
                        .commit();*/
                return false;
            }
        });
        //  mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    public class GetSPInfo extends AsyncTask<String, String, String> {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        boolean flag = false;
        String url = ServerUtility.url_get_sp_info();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            params.add(new BasicNameValuePair("latitude", "" + lat));
            params.add(new BasicNameValuePair("longitude", "" + lon));
        }

        @Override
        protected String doInBackground(String... strings) {
            JSONObject object = jsonParser.makeHttpRequest(url, "GET", params);
            try {
                spInfos = new ArrayList<SPInfo>();
                JSONArray jsonArray = object.getJSONArray("SPInfo");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String username = jsonObject.getString("username");
                    String email = jsonObject.getString("email");
                    String mobile = jsonObject.getString("mobile");
                    double lat1 = jsonObject.getDouble("latitude");
                    double lon1 = jsonObject.getDouble("longitude");
                    SPInfo spInfo = new SPInfo(username, email, mobile, lat1, lon1);
                    spInfos.add(spInfo);

                    //add service providers location on google

                    //   LatLng sydney = new LatLng(lat1, lon1);
                    //   mMap.addMarker(new MarkerOptions().position(sydney).title(username).snippet(mobile).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_local_car_wash_black_24dp)));
                    //  mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 12.0f));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {

            super.onPostExecute(s);
            mapFragment.getMapAsync(ViewGarageFragment.this);
        }
    }
    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}
