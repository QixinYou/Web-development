package com.example.hw9;
import android.util.Log;
import java.util.List;


public class ArtistDetail {
    private String artist_id;

    private String artist_name;

    private String followers;

    private String url;

    private String popularity;

    private String artist_image_url;

    private String Album1;

    private String Album2;

    private String Album3;

    private int index;

    public ArtistDetail(int index,String id, String name, String followers,String url,String popularity,String artist_image_url ){
        this.index=index;
        this.artist_id=id;
        this.artist_name=name;
        this.followers=followers;
        this.url=url;
        this.popularity=popularity;
        this.artist_image_url=artist_image_url;

    }

    public void setAlbum1(String album1){
        this.Album1=album1;
    }

    public void setAlbum2(String album2){
        this.Album2=album2;
    }

    public void setAlbum3(String album3){
        this.Album3=album3;
    }
    public String getArtist_id(){
        return this.artist_id;
    }
    public String getArtist_name(){
        return this.artist_name;
    }
    public String getFollowers(){
        return this.followers;
    }

    public String getUrl(){
        return this.url;
    }

    public String getPopularity(){
        return this.popularity;
    }

    public String getArtist_image_url(){
        return this.artist_image_url;
    }

    public String getAlbum1(){
        return this.Album1;
    }

    public String getAlbum2(){
        return this.Album2;
    }
    public String getAlbum3(){
        return this.Album3;
    }

    public int getIndex() {
        return index;
    }


}
