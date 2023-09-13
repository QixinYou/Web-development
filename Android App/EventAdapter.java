package com.example.hw9;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.material.snackbar.Snackbar;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {
    private Context context;
    private List<event> eventList;

    public EventAdapter(Context context, List<event> eventList) {
        this.context = context;
        this.eventList = eventList;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_event, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
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

                // 如果event是favorite，那么删除SharedPreferences中的数据
                if (event.getIsFavorite()) {
                    event.setIsFavorite(false);
                    holder.imageButton.setImageResource(R.drawable.heart_outline);

                    // 移除与 event 相关的数据
                    editor.remove(event.getEvent_id());
                    editor.apply();

                    Snackbar.make(v, event.getName()+" removed from favorites", Snackbar.LENGTH_SHORT).show();


                } else {
                    // 否则，将event数据添加到SharedPreferences
                    event.setIsFavorite(true);
                    holder.imageButton.setImageResource(R.drawable.heart_filled);

                    // 将事件数据保存到一个 JSON 对象
                    JSONObject eventObject = new JSONObject();
                    try {
                        eventObject.put("event_name", event.getName());
                        eventObject.put("event_venue", event.getVenue());
                        eventObject.put("event_date", event.getDate());
                        eventObject.put("event_time", event.getTime());
                        eventObject.put("event_category", event.getCategory());
                        eventObject.put("event_image_url", event.getEvent_image());
                        eventObject.put("event_id", event.getEvent_id());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    // 将 JSON 对象作为字符串存储在 SharedPreferences 中
                    editor.putString(event.getEvent_id(), eventObject.toString());
                    editor.apply();

                    Snackbar.make(v, event.getName()+" added to favorites", Snackbar.LENGTH_SHORT).show();
                }

                editor.apply();

                Map<String, ?> allEntries = sharedPreferences.getAll();

                for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
                    Log.d("SharedPreferences", entry.getKey() + ": " + entry.getValue().toString());
                }
            }
        });
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 创建一个 Intent 对象，用于启动 ShowDetail Activity
                Intent intent = new Intent(context, ShowDetail.class);

                // 使用 Intent.putExtra() 将事件数据传递给 ShowDetail Activity
                intent.putExtra("event_name", event.getName());
                intent.putExtra("event_venue", event.getVenue());
                intent.putExtra("event_date", event.getDate());
                intent.putExtra("event_time", event.getTime());
                intent.putExtra("event_category", event.getCategory());
                intent.putExtra("event_image_url", event.getEvent_image());
                intent.putExtra("event_id", event.getEvent_id());
                intent.putExtra("isFavorite",event.getIsFavorite());
                intent.putExtra("ticket_url",event.getTicket_url());
                String myListAsString = new Gson().toJson(event.getArtists_teams_array());
                intent.putExtra("ArtistList", myListAsString);

                // 启动 ShowDetail Activity
                context.startActivity(intent);
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
        // 设置跑马灯效果
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

    static class EventViewHolder extends RecyclerView.ViewHolder {

        TextView eventName;
        TextView venue;
        TextView date;
        TextView time;
        TextView eventCategory;
        ImageView imageView;

        ImageButton imageButton;

        public EventViewHolder(@NonNull View itemView) {
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
