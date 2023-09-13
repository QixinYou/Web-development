package com.example.hw9;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class DetailsFragment extends Fragment {
    String event_id;
    private RequestQueue requestQueue;

    private String artistteam;

    private String venue;

    private String date;

    private String time;

    private String genre;

    private String pricerange;

    private String ticketstatus;

    private String ticket_url;

    private String seatmap_url;
    private View view;



    public DetailsFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_details, container, false);

        Bundle bundle = getArguments();

        String backendurl="https://csci571-qixin-homework8.wl.r.appspot.com/api/detail?";


        event_id = bundle.getString("event_id");
        Log.d("id",event_id);
        backendurl+="eventid="+event_id;
        requestQueue = Volley.newRequestQueue(getContext());
        requestDetail(backendurl);


        return view;
    }

    private void  requestDetail(String backendurl) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                backendurl,
                null,
                response -> {
                    try {
                        Log.d("mytag","url:"+backendurl);
                        JSONObject event_detail = new JSONObject(response.toString());
                        if (event_detail.has("dates") && event_detail.getJSONObject("dates").has("start")
                                && event_detail.getJSONObject("dates").getJSONObject("start").has("localDate")) {
                            date = event_detail.getJSONObject("dates").getJSONObject("start").getString("localDate");
                            Log.d("date",date);
                        }
                        if (event_detail.has("dates") && event_detail.getJSONObject("dates").has("start")
                                && event_detail.getJSONObject("dates").getJSONObject("start").has("localTime")) {
                            time = event_detail.getJSONObject("dates").getJSONObject("start").getString("localTime");
                            Log.d("time",time);
                        }
                        if (event_detail.has("_embedded") && event_detail.getJSONObject("_embedded").has("venues")
                                && event_detail.getJSONObject("_embedded").getJSONArray("venues").length() != 0) {
                            venue = event_detail.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getString("name");
                            Log.d("venue",venue);
                        }
                        if (event_detail.has("priceRanges")) {
                            pricerange="";
                            pricerange += event_detail.getJSONArray("priceRanges").getJSONObject(0).getString("min") + "-"
                                    + event_detail.getJSONArray("priceRanges").getJSONObject(0).getString("max")+"(USD)";
                            Log.d("pricerange",pricerange);
                        }
                        if (event_detail.has("dates") && event_detail.getJSONObject("dates").has("status")
                                && event_detail.getJSONObject("dates").getJSONObject("status").has("code")) {
                            ticketstatus = event_detail.getJSONObject("dates").getJSONObject("status").getString("code");
                            Log.d("ticketstatus",ticketstatus);
                        }
                        if (event_detail.has("url")) {
                            ticket_url = event_detail.getString("url");
                            Log.d("ticketurl",ticket_url);
                        }
                        if (event_detail.has("seatmap") && event_detail.getJSONObject("seatmap").has("staticUrl")) {
                            seatmap_url = event_detail.getJSONObject("seatmap").getString("staticUrl");
                            Log.d("seatmap",seatmap_url);
                        }
                        if (event_detail.has("classifications")) {
                            genre="";
                            ArrayList<String> genresarray = new ArrayList<String>();

                            JSONObject classification = event_detail.getJSONArray("classifications").getJSONObject(0);

                            if (classification.has("genre") && !classification.getJSONObject("genre").getString("name").equals("Undefined")) {
                                genresarray.add(classification.getJSONObject("genre").getString("name"));
                            }

                            if (classification.has("segment") && !classification.getJSONObject("segment").getString("name").equals("Undefined")) {
                                genresarray.add(classification.getJSONObject("segment").getString("name"));
                            }

                            if (classification.has("subGenre") && !classification.getJSONObject("subGenre").getString("name").equals("Undefined")) {
                                genresarray.add(classification.getJSONObject("subGenre").getString("name"));
                            }

                            if (classification.has("subType") && !classification.getJSONObject("subType").getString("name").equals("Undefined")) {
                                genresarray.add(classification.getJSONObject("subType").getString("name"));
                            }

                            if (classification.has("type") && !classification.getJSONObject("type").getString("name").equals("Undefined")) {
                                genresarray.add(classification.getJSONObject("type").getString("name"));
                            }

                            for (int i = 0; i < genresarray.size(); ++i) {
                                genre += genresarray.get(i);
                                if (i != genresarray.size() - 1) {
                                    genre += "|";
                                }
                            }
                            Log.d("genres",genre);
                        }
                        if (event_detail.has("_embedded") && event_detail.getJSONObject("_embedded").has("attractions")) {
                            artistteam="";
                            JSONArray attractions = event_detail.getJSONObject("_embedded").getJSONArray("attractions");
                            for (int i = 0; i < attractions.length(); i++) {
                                JSONObject attraction = attractions.getJSONObject(i);
                                String name = attraction.getString("name");
                                artistteam += name;
                                if (i != attractions.length() - 1) {
                                    artistteam += "|";
                                }
                            }
                            Log.d("artistteam",artistteam);
                        }
                        TextView textView_url= view.findViewById(R.id.detail_url);
                        textView_url.setPaintFlags(textView_url.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
                        textView_url.setText(ticket_url);
                        textView_url.setSelected(true);

                        textView_url.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
                                intent.setData(Uri.parse(ticket_url));
                                startActivity(intent);
                            }
                        });
                        TextView textView_art=view.findViewById(R.id.detail_name);
                        textView_art.setText(artistteam);
                        textView_art.setSelected(true);

                        TextView textView_venue=view.findViewById(R.id.detail_venue);
                        textView_venue.setText(venue);
                        textView_venue.setSelected(true);

                        TextView textView_date=view.findViewById(R.id.detail_date);
                        textView_date.setText(date);
                        textView_date.setSelected(true);

                        TextView textView_time=view.findViewById(R.id.detail_time);
                        textView_time.setText(time);
                        textView_time.setSelected(true);

                        TextView textView_genres=view.findViewById(R.id.detail_genres);
                        textView_genres.setText(genre);
                        textView_genres.setSelected(true);

                        TextView textView_price=view.findViewById(R.id.detail_price);
                        textView_price.setText(pricerange);
                        textView_price.setSelected(true);

                        ImageView imageView=view.findViewById(R.id.seatmap);
                        Glide.with(getContext())
                                .load(seatmap_url)
                                .into(imageView);








                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {

                    Log.e("mytag", "Error occurred", error);
                }
        );
        requestQueue.add(jsonObjectRequest);
    }
    @Override
    public void onResume() {
        super.onResume();
        TextView textView_url= view.findViewById(R.id.detail_url);
        textView_url.setPaintFlags(textView_url.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
        textView_url.setText(ticket_url);
        textView_url.setSelected(true);

    }






}