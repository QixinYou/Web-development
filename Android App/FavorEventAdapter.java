package com.example.hw9;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

public class FavorEventAdapter extends RecyclerView.Adapter<FavorEventAdapter.FavorEventViewHolder> {
    private Context context;
    private List<event> eventList;
    public interface OnEmptyListListener {
        void onEmptyList();
    }
    private OnEmptyListListener onEmptyListListener;

    public FavorEventAdapter(Context context, List<event> eventList, OnEmptyListListener onEmptyListListener) {
        this.context = context;
        this.eventList = eventList;
        this.onEmptyListListener = onEmptyListListener;
    }

    @NonNull
    @Override
    public FavorEventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_event, parent, false);
        return new FavorEventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavorEventViewHolder holder, int position) {
        event event = eventList.get(position);
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyFavorite1", Context.MODE_PRIVATE);
        String eventObjectInPref = sharedPreferences.getString(event.getEvent_id(), null);

        if (eventObjectInPref != null) {
            event.setIsFavorite(true);
            holder.imageButton.setImageResource(R.drawable.heart_filled);
        } else {
            event.setIsFavorite(false);
            holder.imageButton.setImageResource(R.drawable.heart_outline);
        }

        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();

                if (event.getIsFavorite()) {
                    event.setIsFavorite(false);
                    holder.imageButton.setImageResource(R.drawable.heart_outline);

                    editor.remove(event.getEvent_id());
                    editor.apply();

                    Snackbar.make(v, event.getName()+" removed from favorites", Snackbar.LENGTH_SHORT).show();
                    eventList.remove(holder.getAbsoluteAdapterPosition());
                    notifyDataSetChanged();

                    if (eventList.isEmpty()) {
                        onEmptyListListener.onEmptyList();
                    }

                }

                editor.apply();

                Map<String, ?> allEntries = sharedPreferences.getAll();

                for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
                    Log.d("SharedPreferences", entry.getKey() + ": " + entry.getValue().toString());
                }
            }
        });

        holder.eventName.setText(event.getName());
        holder.venue.setText(event.getVenue());
        holder.date.setText(event.getDate());
        holder.time.setText(event.getTime());
        holder.eventCategory.setText(event.getCategory());

        Glide.with(context)
                .load(event.getEvent_image())
                .into(holder.imageView);

        holder.eventName.setSelected(true);
        holder.venue.setSelected(true);
        holder.eventCategory.setSelected(true);
        holder.date.setSelected(true);
        holder.time.setSelected(true);
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }
    public void updateEvents(List<event> newEvents) {
        this.eventList.clear();
        this.eventList.addAll(newEvents);
        notifyDataSetChanged();
    }

    static class FavorEventViewHolder extends RecyclerView.ViewHolder {

        TextView eventName;
        TextView venue;
        TextView date;
        TextView time;
        TextView eventCategory;
        ImageView imageView;

        ImageButton imageButton;

        public FavorEventViewHolder(@NonNull View itemView) {
            super(itemView);
            eventName = itemView.findViewById(R.id.event_name);
            venue = itemView.findViewById(R.id.Venue);
            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.time);
            eventCategory = itemView.findViewById(R.id.event_category);
            imageView = itemView.findViewById(R.id.event_image);
            imageButton=itemView.findViewById(R.id.imageButton2);
        }
    }

}
