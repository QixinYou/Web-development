package com.example.hw9;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import java.util.List;
public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.ArtistViewHolder> {
    private List<ArtistDetail> artistDetails;

    private Context context;

    public ArtistAdapter(Context context, List<ArtistDetail> artistDetails) {
        this.context=context;
        this.artistDetails = artistDetails;
    }

    @NonNull
    @Override
    public ArtistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_artist, parent, false);
        return new ArtistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtistViewHolder holder, int position) {
        ArtistDetail artistDetail = artistDetails.get(position);
        String follower=formatFollowersCount(artistDetail.getFollowers());

        // Set the data for the view elements
        holder.artistName.setText(artistDetail.getArtist_name());
        holder.followers.setText(follower+ " Followers");
        holder.popularity.setText(artistDetail.getPopularity());

        Glide.with(context)
                .load(artistDetail.getArtist_image_url())
                .into(holder.artistImage);

        Glide.with(context)
                .load(artistDetail.getAlbum1())
                .into(holder.album1);

        Glide.with(context)
                .load(artistDetail.getAlbum2())
                .into(holder.album2);
        Glide.with(context)
                .load(artistDetail.getAlbum3())
                .into(holder.album3);
        int popularity = 100-Integer.parseInt(artistDetail.getPopularity());
        holder.circularProgressIndicator.setProgressCompat(popularity, true);
        holder.artistUrl.setPaintFlags(holder.artistUrl.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
        holder.artistUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = artistDetail.getUrl();
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                holder.itemView.getContext().startActivity(browserIntent);
            }
        });



        // Set images for artist and albums using a library like Glide or Picasso
    }

    @Override
    public int getItemCount() {
        return artistDetails.size();
    }

    public static class ArtistViewHolder extends RecyclerView.ViewHolder {
        TextView artistName, followers, artistUrl, popularity;
        ImageView artistImage, album1, album2, album3;
        CircularProgressIndicator circularProgressIndicator;


        public ArtistViewHolder(@NonNull View itemView) {
            super(itemView);
            artistName = itemView.findViewById(R.id.artist_name);
            followers = itemView.findViewById(R.id.followers);
            artistUrl = itemView.findViewById(R.id.artist_url);
            popularity = itemView.findViewById(R.id.popularity);
            artistImage = itemView.findViewById(R.id.artist_image);
            album1 = itemView.findViewById(R.id.Album1);
            album2 = itemView.findViewById(R.id.Album2);
            album3 = itemView.findViewById(R.id.Album3);
            circularProgressIndicator= itemView.findViewById(R.id.pop_progress);
        }
    }
    public static String formatFollowersCount(String count) {
        int followerCount =  Integer.parseInt(count);
        if (followerCount >= 1000000) {
            return String.format("%.1fM", followerCount / 1000000.0);
        } else if (followerCount >= 1000) {
            return String.format("%.1fK", followerCount / 1000.0);
        } else {
            return count;
        }
    }


}


