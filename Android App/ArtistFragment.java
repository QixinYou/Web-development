package com.example.hw9;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class ArtistFragment extends Fragment {
    private ArrayList<String> artistlist;

    private List<ArtistDetail> artistDetailList = new ArrayList<>();

    private RequestQueue requestQueue;

    private ArtistAdapter artistAdapter;

    private RecyclerView recyclerView;

    private View view;

    private ProgressBar progressBar;
    public ArtistFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_artist_detail, container, false);

        Bundle bundle = getArguments();
        String[] array = bundle.getStringArray("artistarray");
        Log.d("array", Arrays.toString(array));
        artistlist = new ArrayList<String>(Arrays.asList(array));
        String category= bundle.getString("category");
        Log.d("getcategory",category);

        recyclerView = view.findViewById(R.id.artist_recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireContext());
        recyclerView.setLayoutManager(layoutManager);

        requestQueue = Volley.newRequestQueue(getContext());

       progressBar=view.findViewById(R.id.loadbar);


        if(category.equals("Music")){

            Log.d("test","i am in");
            artistdetail();
        }
        else{
            progressBar.setVisibility(GONE);
            LinearLayout linearLayout=view.findViewById(R.id.no_artist);
            linearLayout.setVisibility(VISIBLE);
            TextView textview1=view.findViewById(R.id.no_artist_text);
            textview1.setVisibility(VISIBLE);
        }


        return view;
    }
    private void artistdetail(){
        String BACKEND_URL = "https://csci571-qixin-homework8.wl.r.appspot.com/api/spotify?";
        String BACKEND_URL1 = "https://csci571-qixin-homework8.wl.r.appspot.com/api/spotifyalbum?";
        for (int i = 0; i < artistlist.size(); i++) {
            String artist = artistlist.get(i);
            String artistUrl = BACKEND_URL + "artist=" + artist;
            Log.d("artisturl", artistUrl);

            int finalI = i;
            JsonObjectRequest artistRequest = new JsonObjectRequest(Request.Method.GET, artistUrl, null,
                    response -> {
                        try {
                            JSONObject artistData = response.getJSONObject("artists").getJSONArray("items").getJSONObject(0);
                            ArtistDetail artistDetails = new ArtistDetail(
                                    finalI,
                                    artistData.getString("id"),
                                    artistData.getString("name"),
                                    artistData.getJSONObject("followers").getString("total"),
                                    artistData.getJSONObject("external_urls").getString("spotify"),
                                    artistData.getString("popularity"),
                                    artistData.getJSONArray("images").getJSONObject(2).getString("url")
                            );
                            // ...
                            String albumUrl = BACKEND_URL1 + "id=" + artistDetails.getArtist_id();
                            // ...
                            JsonObjectRequest albumRequest = new JsonObjectRequest(Request.Method.GET, albumUrl, null,
                                    albumResponse -> {
                                        try {
                                            JSONArray albumItems = albumResponse.getJSONArray("items");
                                            artistDetails.setAlbum1(albumItems.getJSONObject(0).getJSONArray("images").getJSONObject(1).getString("url"));
                                            artistDetails.setAlbum2(albumItems.getJSONObject(1).getJSONArray("images").getJSONObject(1).getString("url"));
                                            artistDetails.setAlbum3(albumItems.getJSONObject(2).getJSONArray("images").getJSONObject(1).getString("url"));
                                            artistDetailList.add(artistDetails);
                                            if (artistDetailList.size() == artistlist.size()) {

                                                Collections.sort(artistDetailList, new Comparator<ArtistDetail>() {
                                                    @Override
                                                    public int compare(ArtistDetail o1, ArtistDetail o2) {
                                                        return o1.getIndex() - o2.getIndex();
                                                    }
                                                });

                                                if (recyclerView == null) {
                                                    return;
                                                }
                                                progressBar.setVisibility(GONE);

                                                artistAdapter = new ArtistAdapter(requireContext(), artistDetailList);
                                                recyclerView.setAdapter(artistAdapter);
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }, error -> {
                                error.printStackTrace();
                            });
                            requestQueue.add(albumRequest);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }, error -> {
                error.printStackTrace();
            });
            requestQueue.add(artistRequest);
        }
    }



}