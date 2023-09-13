package com.example.hw9;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.List;


public class Detailadapter extends FragmentStateAdapter {
    private String eventId;

    private String venue;

    private List<String>artistList;

    private String category;
    public Detailadapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, String eventId, String venue, List<String> ArtistList,String category) {
        super(fragmentManager, lifecycle);
        this.eventId=eventId;
        this.venue=venue;
        this.artistList=ArtistList;
        this.category=category;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                DetailsFragment fragment0 = new DetailsFragment();
                Bundle bundle = new Bundle();
                bundle.putString("event_id", eventId);
                fragment0.setArguments(bundle);
                return fragment0;
            case 1:
                String[] artistarray = new String[artistList.size()];
                artistList.toArray(artistarray);
                ArtistFragment fragment1= new ArtistFragment();
                Bundle bundle1=new Bundle();
                bundle1.putStringArray("artistarray",artistarray);
                bundle1.putString("category",category);
                fragment1.setArguments(bundle1);
                return fragment1;
            case 2:
                VenueFragment fragment2=new VenueFragment();
                Bundle bundle2=new Bundle();
                bundle2.putString("venue",venue);
                fragment2.setArguments(bundle2);
                return fragment2;
            default:
                return new DetailsFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
