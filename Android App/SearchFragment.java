package com.example.hw9;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AutoCompleteTextView;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;


import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import android.util.Log;


public class SearchFragment extends Fragment {
    private Spinner spinner;
    private LocationListener locationListener;

    private List<String> dataList = new ArrayList<>();

    private AutoCompleteTextView autoCompleteTextView;

    private ArrayAdapter<String> autoCompleteAdapter;
    private RequestQueue requestQueue;

    private String categoryvalue;

    private String input_long;

    private String input_lat;

    private String keyword;

    private String distance;

    private String locationstring;

    private View view;

    public SearchFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        String[] items = new String[]{"All", "Music", "Sports","Arts&Theatres","Film","Miscellaneous"};
        view = inflater.inflate(R.layout.fragment_search, container, false);
        spinner = view.findViewById(R.id.category);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(R.layout.dropdown);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
                categoryvalue = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        autoCompleteTextView = view.findViewById(R.id.event_autocomplete);
       autoCompleteAdapter = new ArrayAdapter<>(requireActivity(), R.layout.auto_dropdown,dataList);
        autoCompleteTextView.setThreshold(1);
       autoCompleteTextView.setAdapter(autoCompleteAdapter);
        requestQueue = Volley.newRequestQueue(requireContext());
       autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String inputText = s.toString();
                fetchAutoCompleteData(inputText);

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        EditText distance_input = view.findViewById(R.id.distance);
        Switch locationSwitch = view.findViewById(R.id.here);
        EditText locationEditText = view.findViewById(R.id.location);

        locationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    locationEditText.setVisibility(View.GONE);
                } else {
                    locationEditText.setVisibility(View.VISIBLE);
                }
            }
        });


        Button search_button=view.findViewById(R.id.search);
        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 keyword = autoCompleteTextView.getText().toString();
                 distance = distance_input.getText().toString();
                locationstring = locationEditText.getText().toString();
                Log.d("search", "keyWordValue is : " + keyword +
                        " category_chosen: " + categoryvalue+
                        " distanceValue_int: " + distance +
                        " locationValue: " + locationstring
                );
                if(keyword.trim().equals("")){
                    Snackbar.make(view, "Keyword cannot be empty", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if(distance.trim().equals("")){
                    Snackbar.make(view, "Distance cannot be empty", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                boolean locationhere = locationSwitch.isChecked();
                if(!locationhere&&locationstring.trim().equals("")){
                    Snackbar.make(view, "Location cannot be empty", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if(!locationhere){
                    String str="&key=AIzaSyCq31ZK7DXurGbp1LicUHtTHomSfgYEcew";
                    String googlegeo="https://maps.googleapis.com/maps/api/geocode/json?address=";
                    googlegeo+=locationstring;
                    googlegeo+=str;
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, googlegeo, null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        JSONArray jsonArray = response.getJSONArray("results");
                                        JSONObject results = jsonArray.getJSONObject(0);
                                        JSONObject geometry = results.getJSONObject("geometry");
                                        JSONObject location = geometry.getJSONObject("location");
                                        input_lat = location.getString("lat");
                                        input_long = location.getString("lng");
                                        Log.d("MyTag", "lat: " + input_lat + " lng: " + input_long);
                                        requestbackend();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                        }
                    });
                    requestQueue.add(jsonObjectRequest);

                }
                else{
                    String ipinfourl = "https://ipinfo.io/?token=9e68f44284039e";
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, ipinfourl, null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        String loc = response.getString("loc");
                                        String[] result = loc.split(",");
                                        input_long = result[1];
                                        input_lat = result[0];
                                        Log.d("MyTag", "lat: " + input_lat + " lng: " + input_long);
                                        requestbackend();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                        }
                    });
                    requestQueue.add(jsonObjectRequest);
                }



            }
        });



        Button clear_button = view.findViewById(R.id.clear);
        clear_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autoCompleteTextView.setText("");
                distance_input.setText("10");
                locationEditText.setText("");
                locationSwitch.setChecked(false);  // set default location here
                spinner.setSelection(0);

            }
        });

        return view;


    }

    private void fetchAutoCompleteData(String inputText) {
        String url = "https://csci571-qixin-homework8.wl.r.appspot.com/api/autocomplete?keyword="+inputText;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("attractions");
                            Gson gson = new Gson();
                            dataList.clear();

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject item = jsonArray.getJSONObject(i);
                                String itemName = item.getString("name");
                                dataList.add(itemName);
                            }
                            autoCompleteAdapter.clear(); // Add this line
                            autoCompleteAdapter.addAll(dataList); // Add this line
                            autoCompleteAdapter.notifyDataSetChanged();
                            autoCompleteAdapter = new ArrayAdapter<>(requireActivity(), R.layout.auto_dropdown, dataList);
                            autoCompleteTextView.setAdapter(autoCompleteAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        requestQueue.add(jsonObjectRequest);

    }

    private void requestbackend(){
        String backend="https://csci571-qixin-homework8.wl.r.appspot.com/api/search?";
        if(categoryvalue=="All"){
            categoryvalue="Default";
        }
        backend+="keyword="+keyword+"&category="+categoryvalue+"&distance="+distance+"&latitude="+input_lat+"&longitude="+input_long;
        Log.d("backendurl", "backend:"+backend);
        DetailSearchFragment detailSearchFragment = new DetailSearchFragment();

        Bundle bundle = new Bundle();
        bundle.putString("backend_url", backend);
        detailSearchFragment.setArguments(bundle);

        View searchRoot = view.findViewById(R.id.search_content);
        searchRoot.setVisibility(View.GONE);

        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.inner_fragment_container, detailSearchFragment)
                .addToBackStack(null)
                .commit();

        view.findViewById(R.id.search_content).setVisibility(View.GONE);
        view.findViewById(R.id.inner_fragment_container).setVisibility(View.VISIBLE);




    }
    public void restoreInitialView() {
        view.findViewById(R.id.inner_fragment_container).setVisibility(View.GONE);
        view.findViewById(R.id.search_content).setVisibility(View.VISIBLE);
    }



}