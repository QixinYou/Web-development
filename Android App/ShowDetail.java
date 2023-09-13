package com.example.hw9;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.hw9.event;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.telecom.Call;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ShowDetail extends AppCompatActivity {
    private boolean isFavorite;
    private String eventName;

    private String eventVenue;

    private String eventDate;

    private String eventTime;

    private String eventCategory;

    private String eventImageUrl;

    private String eventId;

    private String ticket_url;

    private Context context;

    private View view;
    private SharedPreferences mSharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_detail);
        view = getWindow().getDecorView().findViewById(android.R.id.content);

        // 从 Intent 中获取数据
        Intent intent = getIntent();
        eventName = intent.getStringExtra("event_name");
        eventVenue = intent.getStringExtra("event_venue");
        eventDate = intent.getStringExtra("event_date");
        eventTime = intent.getStringExtra("event_time");
        eventCategory = intent.getStringExtra("event_category");
        eventImageUrl = intent.getStringExtra("event_image_url");
        eventId = intent.getStringExtra("event_id");
        isFavorite = getIntent().getBooleanExtra("isFavorite", false);
        ticket_url=intent.getStringExtra("ticket_url");
        String myListAsString = getIntent().getStringExtra("ArtistList");
        List<String> artistList = new Gson().fromJson(myListAsString, new TypeToken<ArrayList<String>>(){}.getType());
        Log.d("name",eventName);
        Log.d("url",ticket_url);
        Log.d("isFavorite", String.valueOf(isFavorite));



        mSharedPreferences = getApplicationContext().getSharedPreferences("MyFavorite1", Context.MODE_PRIVATE);
        // 设置 ViewPager2 和 TabLayout
        ViewPager2 viewPager2 = findViewById(R.id.detailviewPager);
        TabLayout tabLayout = findViewById(R.id.detailtabLayout);
        Detailadapter detailAdapter = new Detailadapter(getSupportFragmentManager(), getLifecycle(),eventId,eventVenue,artistList,eventCategory);
        viewPager2.setAdapter(detailAdapter);
        new TabLayoutMediator(tabLayout, viewPager2,
                (tab, position) -> {
                    switch (position) {
                        case 0:
                            tab.setCustomView(getTabView(R.drawable.info_icon, "DETAILS"));
                            break;
                        case 1:
                            tab.setCustomView(getTabView(R.drawable.artist_icon, "ARTIST(S)"));
                            break;
                        case 2:
                            tab.setCustomView(getTabView(R.drawable.venue_icon, "VENUE"));
                            break;
                    }
                }).attach();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true); // 显示返回按钮
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setCustomView(R.layout.actionbar_custom);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.green_back_btn);
            TextView actionBarText = actionBar.getCustomView().findViewById(R.id.action_bar_title);
            actionBarText.setText(eventName);
            actionBarText.setSelected(true); // 启动跑马灯效果
        }
    }
    private View getTabView(int iconResId, String title) {
        View view = LayoutInflater.from(this).inflate(R.layout.detail_tab_custom, null);
        ImageView imageView = view.findViewById(R.id.tab_icon);
        TextView textView = view.findViewById(R.id.tab_text);
        imageView.setImageResource(iconResId);
        textView.setText(title);
        return view;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        MenuItem button3 = menu.findItem(R.id.action_button3);
        if (isFavorite) {
            button3.setIcon(R.drawable.heart_filled); // 设置为收藏图标
        } else {
            button3.setIcon(R.drawable.heart_outline); // 设置为未收藏图标
        }
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_button1:
                openfacebook();
                // 按钮 1 的点击事件处理
                return true;
            case R.id.action_button2:
                opentweet();
                // 按钮 2 的点击事件处理
                return true;
            case R.id.action_button3:
                // 按钮 3 的点击事件处理
                isFavorite = !isFavorite;
                // 更改图标
                if (isFavorite) {
                    item.setIcon(R.drawable.heart_filled);
                } else {
                    item.setIcon(R.drawable.heart_outline);
                }
                // 处理收藏/取消收藏的逻辑
                handleFavoriteStatus(isFavorite);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void handleFavoriteStatus(boolean isFavorite) {

        SharedPreferences.Editor editor = mSharedPreferences.edit();
        if (isFavorite) {
            JSONObject eventObject = new JSONObject();
            try {
                eventObject.put("event_name", eventName);
                eventObject.put("event_venue", eventVenue);
                eventObject.put("event_date", eventDate);
                eventObject.put("event_time", eventTime);
                eventObject.put("event_category", eventCategory);
                eventObject.put("event_image_url", eventImageUrl);
                eventObject.put("event_id", eventId);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            // 将 JSON 对象作为字符串存储在 SharedPreferences 中
            editor.putString(eventId, eventObject.toString());
            editor.apply();

            Snackbar.make(view, eventName+" added to favorites", Snackbar.LENGTH_SHORT).show();

        } else {
            editor.remove(eventId);
            editor.apply();

            Snackbar.make(view, eventName+" removed from favorites", Snackbar.LENGTH_SHORT).show();

        }
    }

    private void opentweet(){
        String twitterApi="https://twitter.com/intent/tweet?text=";
        twitterApi+="Check "+ eventName+" on Ticketmaster."+ticket_url;

        // 创建一个隐式 Intent，用于打开 URL
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
        intent.setData(Uri.parse(twitterApi));
        startActivity(intent);

    }
    private void openfacebook(){
        String facebookApi="https://www.facebook.com/sharer/sharer.php?u=";
        facebookApi+=ticket_url+"&amp;src=sdkpreparse";

        Log.d("url",facebookApi);

        Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
        intent.setData(Uri.parse(facebookApi));
        startActivity(intent);

    }

}