package com.example.hw9;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.hw9.event;
import com.example.hw9.EventAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class DetailSearchFragment extends Fragment {
    private List<event> event_list;
    private RequestQueue requestQueue;

    private EventAdapter eventAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_search, container, false);
        requestQueue = Volley.newRequestQueue(requireContext());
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireContext());
        recyclerView.setLayoutManager(layoutManager);
        String backendUrl = getArguments().getString("backend_url");
        fetchData(backendUrl);
        Button backButton = view.findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 返回到初始界面的逻辑
                goBackToInitialView();
            }
        });



        return view;
    }
    private void fetchData(String backendurl) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                backendurl,
                null,
                response -> {
                    try {
                        Log.d("mytag","url:"+backendurl);
                        Log.d("test","into fetch");
                        JSONObject page = response.getJSONObject("page");
                        int total_element = page.getInt("totalElements");
                        if(total_element == 0){
                            Log.d("total number", "no results");
                            LinearLayout noevent=getView().findViewById(R.id.noevent);
                            TextView noeventtext=getView().findViewById(R.id.noeventtext);
                            noevent.setVisibility(View.VISIBLE);
                            noeventtext.setVisibility(View.VISIBLE);
                            ProgressBar progressBar = getView().findViewById(R.id.progressBar);
                            progressBar.setVisibility(View.GONE);
                        }
                        else{
                            JSONObject embedded = response.getJSONObject("_embedded");
                            JSONArray events = embedded.getJSONArray("events");
                            JSONObject each_event = null;
                            event_list = new ArrayList<>();
                            for (int j = 0; j < events.length(); j++) {
                                each_event = events.getJSONObject(j);

                                String name = each_event.getString("name");
                                String venue = each_event.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getString("name");
                                String date = each_event.getJSONObject("dates").getJSONObject("start").getString("localDate");
                                String time=each_event.getJSONObject("dates").getJSONObject("start").optString("localTime", "");
                                String category = each_event.getJSONArray("classifications").getJSONObject(0).getJSONObject("segment").getString("name");
                                String image_url=each_event.getJSONArray("images").getJSONObject(0).getString("url");
                                String Id=each_event.getString("id");
                                String ticket_url=each_event.getString("url");
                                JSONArray artists_teams_list = new JSONArray();
                                try{
                                    artists_teams_list = each_event.getJSONObject("_embedded").getJSONArray("attractions");
                                }catch (Exception e) {
                                }

                                List<String> artists_teams_array = new ArrayList<String>();
                                for(int k = 0; k < artists_teams_list.length(); ++k){
                                    artists_teams_array.add(artists_teams_list.getJSONObject(k).getString("name"));
                                }





                                Log.d("name", name);
                                Log.d("venue", venue);
                                Log.d("date", date);
                                Log.d("category", category);
                                Log.d("time",time);
                                Log.d("image_url",image_url);
                                Log.d("ID",Id);
                                Log.d("ticket_url",ticket_url);


                                event event = new event(name, venue, date, category,time,image_url,Id);
                                event.setTicket_url(ticket_url);
                                event.setArtists_teams_array(artists_teams_array);
                                event_list.add(event);

                            }
                            ProgressBar progressBar = getView().findViewById(R.id.progressBar);
                            progressBar.setVisibility(View.GONE);
                            eventAdapter = new EventAdapter(requireContext(), event_list);
                            RecyclerView recyclerView = getView().findViewById(R.id.recyclerView);
                            recyclerView.setAdapter(eventAdapter);

                        }
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
    private void goBackToInitialView() {
        getParentFragmentManager().popBackStack();
        getParentFragmentManager().popBackStack();
        Fragment parentFragment = getParentFragment();
        if (parentFragment instanceof SearchFragment) {
            ((SearchFragment) parentFragment).restoreInitialView();
        }
    }

    public void onResume() {
        super.onResume();
        if (eventAdapter != null) {
            eventAdapter.notifyDataSetChanged();
        }

    }
}