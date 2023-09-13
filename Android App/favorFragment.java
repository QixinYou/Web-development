package com.example.hw9;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.hw9.event;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class favorFragment extends Fragment implements FavorEventAdapter.OnEmptyListListener {
    private RecyclerView favoritesRecyclerView;
    private FavorEventAdapter favoreventsAdapter;

    private View view;


    public void onEmptyList() {
        RecyclerView recyclerView = view.findViewById(R.id.favorites_recyclerview);
        LinearLayout nofavorLayout = view.findViewById(R.id.nofavor);
        TextView nofavorText=view.findViewById(R.id.nofavortext);
        nofavorLayout.setVisibility(View.VISIBLE);
        nofavorText.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       view = inflater.inflate(R.layout.fragment_favor, container, false);
        ProgressBar progressBar = view.findViewById(R.id.progressBar1);
        progressBar.setVisibility(View.VISIBLE);
        RecyclerView recyclerView = view.findViewById(R.id.favorites_recyclerview);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireContext());
        recyclerView.setLayoutManager(layoutManager);

        getFavoriteEventsFromSharedPreferences();


        return view;
    }
    private void getFavoriteEventsFromSharedPreferences() {
        List<event> favoriteEvents = new ArrayList<>();
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("MyFavorite1", Context.MODE_PRIVATE);
        Map<String, ?> allEntries = sharedPreferences.getAll();

        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            String jsonString = (String) entry.getValue();

            if (jsonString != null && isJson(jsonString)) {
                try {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    event event = new event(
                            jsonObject.getString("event_name"),
                            jsonObject.getString("event_venue"),
                            jsonObject.getString("event_date"),
                            jsonObject.getString("event_category"),
                            jsonObject.getString("event_time"),
                            jsonObject.getString("event_image_url"),
                            jsonObject.getString("event_id")
                    );
                    Log.d("name", event.getName());
                    Log.d("venue", event.getVenue());
                    Log.d("date", event.getDate());
                    Log.d("category", event.getCategory());
                    Log.d("time", event.getTime());
                    Log.d("image_url", event.getEvent_image());
                    favoriteEvents.add(event);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        ProgressBar progressBar = view.findViewById(R.id.progressBar1);
        progressBar.setVisibility(View.GONE);
        RecyclerView recyclerView = view.findViewById(R.id.favorites_recyclerview);
        if(favoriteEvents.size() == 0) {
            LinearLayout nofavorLayout = view.findViewById(R.id.nofavor);
            TextView nofavorText=view.findViewById(R.id.nofavortext);
            nofavorText.setVisibility(View.VISIBLE);
            nofavorLayout.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
        else {
            LinearLayout nofavorLayout = view.findViewById(R.id.nofavor);
            TextView nofavorText=view.findViewById(R.id.nofavortext);
            nofavorText.setVisibility(View.GONE);
            nofavorLayout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            favoreventsAdapter = new FavorEventAdapter(requireContext(), favoriteEvents,favorFragment.this);
            recyclerView.setAdapter(favoreventsAdapter);
        }
    }

    private boolean isJson(String jsonString) {
        try {
            new JSONObject(jsonString);
        } catch (JSONException e) {
            return false;
        }
        return true;
    }
    public void onResume() {
        super.onResume();
        LinearLayout nofavorLayout = view.findViewById(R.id.nofavor);
        TextView nofavorText=view.findViewById(R.id.nofavortext);
        nofavorText.setVisibility(View.GONE);
        nofavorLayout.setVisibility(View.GONE);
        ProgressBar progressBar = view.findViewById(R.id.progressBar1);
        progressBar.setVisibility(View.VISIBLE);
        getFavoriteEventsFromSharedPreferences();
    }


}