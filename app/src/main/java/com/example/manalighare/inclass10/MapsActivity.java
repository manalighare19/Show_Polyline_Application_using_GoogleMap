package com.example.manalighare.inclass10;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String Trip_Points_Json;
    private ArrayList<Point> Map_points=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        Trip_Points_Json=loadJSONFromAsset();

        Log.d("demo","Points : "+Trip_Points_Json);

        try {
            JSONObject root=new JSONObject(Trip_Points_Json);
            JSONArray points=root.getJSONArray("points");
            Gson gson=new Gson();

            for (int i=0;i<points.length();i++){
                Point tmp=gson.fromJson(points.get(i).toString(),Point.class);
                Map_points.add(tmp);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

       /* // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/

       LatLng position_of_point;
       PolylineOptions polylineOptions=new PolylineOptions();
       LatLngBounds.Builder builder= new LatLngBounds.Builder();



       for (int i=0;i<Map_points.size();i++){
           String lat= Map_points.get(i).latitude.toString();
           String longi=Map_points.get(i).longitude.toString();

           position_of_point=new LatLng(Double.parseDouble(lat),Double.parseDouble(longi));
           builder.include(position_of_point);
           polylineOptions.add(position_of_point);
           if(i==0){
               mMap.addMarker(new MarkerOptions().position(position_of_point).title("Starting point"));
           }
           if(i==Map_points.size()-1){
               mMap.addMarker(new MarkerOptions().position(position_of_point).title("End Point"));
           }
       }

        mMap.moveCamera(CameraUpdateFactory.zoomTo(mMap.getMinZoomLevel()));
       polylineOptions.color(Color.BLUE).width(6);


       LatLngBounds latLngBounds=builder.build();
       mMap.addPolyline(polylineOptions);
       //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLngBounds.getCenter(),10),900,null);
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds,10),900,null);

    }

    public String loadJSONFromAsset() {
        String json = null;
        try {

            InputStream is = this.getAssets().open("trip.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            Log.d("demo","Error is : "+ex.toString());
            return null;
        }
        return json;
    }
}
