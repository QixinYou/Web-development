package com.example.hw9;

import static android.view.View.GONE;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.OnMapReadyCallback;


public class VenueFragment extends Fragment implements OnMapReadyCallback {
    private String venue_name;

    private GoogleMap mMap;

    private String venuelongitude;

    private String venuelatitude;

    private String address;

    private String phonenumber;

    private String openhours;

    private String generalrule;

    private String childrule;

    private String city_state;

    private View view;

    private RequestQueue requestQueue;


    public VenueFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_venue_detail, container, false);

        Bundle bundle = getArguments();
        venue_name = bundle.getString("venue");
        Log.d("venue",venue_name);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync( this);

        String backendurl="https://csci571-qixin-homework8.wl.r.appspot.com/api/venue?";
        backendurl+="keyword="+venue_name;

        requestQueue = Volley.newRequestQueue(getContext());
        requestvenuedetail(backendurl);

        return view;
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // The map is now ready to be used, but you need to wait for the venue details request to finish.
    }

   private void requestvenuedetail(String backendurl){


       JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, backendurl, null,
               response -> {
                   try {
                       JSONObject venue = response.getJSONArray("venues").getJSONObject(0);
                       venuelongitude = venue.getJSONObject("location").getString("longitude");
                       venuelatitude = venue.getJSONObject("location").getString("latitude");
                       address="";
                       if(venue.has("address")) {
                           if (venue.getJSONObject("address").has("line1")) {
                               address += venue.getJSONObject("address").getString("line1");
                           }
                       }
                       city_state="";
                       if (venue.has("city")) {
                           if (venue.getJSONObject("city").has("name")) {
                               city_state += venue.getJSONObject("city").getString("name");
                               city_state += ", ";
                           }
                       }

                       if (venue.has("state")) {
                           if (venue.getJSONObject("state").has("name")) {
                               city_state += venue.getJSONObject("state").getString("name");
                           }
                       }
                       phonenumber = "";
                       openhours = "";
                       Log.d("address",address);
                       if (venue.has("boxOfficeInfo")) {
                           if (venue.getJSONObject("boxOfficeInfo").has("phoneNumberDetail")) {

                               phonenumber = venue.getJSONObject("boxOfficeInfo").getString("phoneNumberDetail");
                           }
                           if (venue.getJSONObject("boxOfficeInfo").has("openHoursDetail")) {

                               openhours = venue.getJSONObject("boxOfficeInfo").getString("openHoursDetail");
                           }
                       }
                       Log.d("phonenubmer",phonenumber);
                       Log.d("openhours",openhours);
                       generalrule = "";
                       childrule = "";
                       if (venue.has("generalInfo")) {
                           if (venue.getJSONObject("generalInfo").has("generalRule")) {

                               generalrule = venue.getJSONObject("generalInfo").getString("generalRule");
                           }
                           if (venue.getJSONObject("generalInfo").has("childRule")) {
                               childrule = venue.getJSONObject("generalInfo").getString("childRule");
                           }
                       }
                       Log.d("generalrule",generalrule);
                       Log.d("childrule",childrule);

                       TextView textView_name=view.findViewById(R.id.venue_name);
                       textView_name.setText(venue_name);
                       textView_name.setSelected(true);


                       TextView textView_address=view.findViewById(R.id.venue_address);
                       textView_address.setText(address);
                       textView_address.setSelected(true);

                       TextView textView_city=view.findViewById(R.id.venue_city);
                       textView_city.setText(city_state);
                       textView_city.setSelected(true);


                       TextView textView_contact=view.findViewById(R.id.venue_contact);
                       if(phonenumber.isEmpty()){
                           textView_contact.setText("No contact info");
                       }
                       else {
                           textView_contact.setText(phonenumber);
                           textView_contact.setSelected(true);
                       }

                       TextView textView_openhour=view.findViewById(R.id.openhour);
                       if(openhours.isEmpty()){
                           textView_openhour.setText("No information of Open Hours");
                       }
                       else {
                           textView_openhour.setText(openhours);
                       }

                       TextView textView_g_rule=view.findViewById(R.id.g_rule);

                       if(generalrule.isEmpty()){
                           textView_g_rule.setText("No information of General rules");
                       }
                       else {
                           textView_g_rule.setText(generalrule);


                           textView_g_rule.setOnClickListener(new View.OnClickListener() {
                               @Override
                               public void onClick(View view) {
                                   TextView textView = (TextView) view;
                                   int maxLines = textView.getMaxLines() == 3 ? Integer.MAX_VALUE : 3;
                                   textView.setMaxLines(maxLines);
                               }
                           });
                       }

                       TextView textView_child_rule=view.findViewById(R.id.child_rule);
                       if(childrule.isEmpty()){
                           textView_child_rule.setText("No information of Child Rules");
                       }
                       else {
                           textView_child_rule.setText(childrule);

                           textView_child_rule.setOnClickListener(new View.OnClickListener() {
                               @Override
                               public void onClick(View view) {
                                   TextView textView = (TextView) view;
                                   int maxLines = textView.getMaxLines() == 3 ? Integer.MAX_VALUE : 3;
                                   textView.setMaxLines(maxLines);
                               }
                           });
                       }



                       updateMap();




                   } catch (JSONException e) {
                       e.printStackTrace();
                   }
               },  error -> {

           Log.e("mytag", "Error occurred", error);
       });
       requestQueue.add(request);

   }
    private void updateMap() {
        if (mMap != null && venuelatitude != null && venuelongitude != null) {
            double venueLatitudeDouble = Double.parseDouble(venuelatitude);
            double venueLongitudeDouble = Double.parseDouble(venuelongitude);
            LatLng venueLocation = new LatLng(venueLatitudeDouble, venueLongitudeDouble);
            mMap.addMarker(new MarkerOptions().position(venueLocation).title(venue_name));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(venueLocation, 15));
        }
    }





}