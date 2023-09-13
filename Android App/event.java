package com.example.hw9;

import android.util.Log;
import java.util.List;


public class event {
    private String Event_name;

    private String Venue;

    private String Date;

    private String Time;

    private String Category;

    private String event_image;

    private Boolean isFavorite;

    private List<String> artists_teams_array;

    private String artists_team="";

    private String category_detail="";

    private String Price;

    private String Status;

    private String ticket_url;

    private String seatmap_url;

    private String Event_id;

    public event(String Event_name,String Venue,String Date,String Category,String Time,String event_image,String Event_id){
        this.Event_name=Event_name;
        this.Venue=Venue;
        this.Date=Date;
        this.Category=Category;
        this.Time=Time;
        this.event_image=event_image;
        this.Event_id=Event_id;
        isFavorite=false;
    }
    public String getName(){return Event_name;}
    public String getVenue(){return Venue;}
    public String getDate(){return Date;}
    public String getCategory(){return Category;}
    public String getTime(){return Time;}
    public String getEvent_image(){return event_image;}

    public String getEvent_id(){return Event_id;}

    public Boolean getFavorite(){
        if(isFavorite){
            return true;
        }
        else{
            return false;
        }
    }

    public Boolean getIsFavorite(){return isFavorite;}
    public String getTicket_url(){return ticket_url;}

    public List<String> getArtists_teams_array() {
        return artists_teams_array;
    }

    public void setCategory(String category){this.Category=category;}
    public void setIsFavorite(Boolean isFavorite){this.isFavorite=isFavorite;}
    public void setArtists_teams_array(List<String> artists_teams_array){
        this.artists_teams_array=artists_teams_array;
        for(int i=0;i<artists_teams_array.size();i++){
            if(artists_teams_array.get(i)!=null){
                artists_team+=artists_teams_array.get(i);

            if(i<artists_teams_array.size()-1){
                artists_team+="|";
                }
            }
        }
        Log.d("artists_team","setArtistsTeams: " + artists_team);
    }



    public void setTicket_url(String ticket_url){this.ticket_url=ticket_url;}





}
