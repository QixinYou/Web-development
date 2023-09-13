const express = require('express')
const app = express();
const axios=require('axios');
const util=require('util');
var geohash=require('ngeohash');
var SpotifyWebApi=require('spotify-web-api-node');
const {response, json} = require("express");



app.get('/search', (req,res) => {
 console.log(req.query.latitude,req.query.longitude)
  var geohashresult= geohash.encode(req.query.latitude,req.query.longitude)
  var segmentId;
  ticketmasterurl="https://app.ticketmaster.com/discovery/v2/events.json?apikey=VoKc9EuVCyTafOPJF7sGMyrQUVO9JuQE&sort=date,asc"
  ticketmasterurl+='&keyword='+req.query.keyword
    if(req.query.category != 'Default'){
        if(req.query.category == 'Music'){
            segmentId = 'KZFzniwnSyZfZ7v7nJ'
        }
        else if(req.query.category == 'Sports'){
            segmentId = 'KZFzniwnSyZfZ7v7nE'
        }
        else if(req.query.category == 'ArtsTheatre'){
            segmentId = 'KZFzniwnSyZfZ7v7na'
        }
        else if(req.query.category == 'Films'){
            segmentId = 'KZFzniwnSyZfZ7v7nn'
        }
        else if(req.query.category == 'Miscellaneous'){
            segmentId = 'KZFzniwnSyZfZ7v7n1'

        }
    }
    if(req.query.category != 'Default') {
        ticketmasterurl += "&segmentId=" + segmentId
    }
  ticketmasterurl+="&radius="+req.query.distance
  ticketmasterurl+="&unit=miles"
  ticketmasterurl+="&geoPoint="+geohashresult
  console.log(ticketmasterurl)
  axios.get(ticketmasterurl)
      .then(response=>{
          res.header("Access-Control-Allow-Origin","*");
          console.log(response.data);
          res.send(JSON.stringify(response.data))
          })
        .catch(error => {
        console.log(error);
        });

});
app.get('/autocomplete',(req,res)=>{
    autocompleteurl="https://app.ticketmaster.com/discovery/v2/suggest?apikey=VoKc9EuVCyTafOPJF7sGMyrQUVO9JuQE&keyword=";
    autocompleteurl+=req.query.keyword
    axios.get(autocompleteurl)
        .then(response=>{
            console.log(autocompleteurl);
            res.header("Access-Control-Allow-Origin","*");
            res.send(JSON.stringify(response.data._embedded));
        })
        .catch(error => {
            console.log(error);
        });
})
app.get('/detail',(req,res)=>
{
    detailurl="https://app.ticketmaster.com/discovery/v2/events/"
    detailurl+=req.query.eventid;
    detailurl+=".json?apikey=VoKc9EuVCyTafOPJF7sGMyrQUVO9JuQE&"
    console.log(detailurl)
    axios.get(detailurl)
        .then(response=>{
            res.header("Access-Control-Allow-Origin","*");
            console.log(res);
            res.send(JSON.stringify((response.data)))
            
        })
        .catch(error => {
            console.log(error);
        });

})
app.get('/spotify',(req,res)=>{
    var spotifyApi= new SpotifyWebApi({
        clientId:'3a0053ab3adf498e802c245b5dbb2bcc',
        clientSecret:'d8678ac3de3842188b4dd00068eaaed1'
    });

    spotifyApi.clientCredentialsGrant().then(
        function (data){
            console.log('access token' + data.body['access_token']);
            spotifyApi.setAccessToken(data.body['access_token']);
            spotifyApi.searchArtists(req.query['artist']).then(
                function(data){
                    res.header("Access-Control-Allow-Origin","*");
                    res.send(JSON.stringify(data.body));
                },
                function(err){
                    console.log('wrong',err);
                }

            );

        },
        function(err){
            console.log('wrong',err);
        }
    );

});
app.get('/spotifyalbum',(req,res)=>{
    var spotifyApi= new SpotifyWebApi({
        clientId:'3a0053ab3adf498e802c245b5dbb2bcc',
        clientSecret:'d8678ac3de3842188b4dd00068eaaed1'
    });

    spotifyApi.clientCredentialsGrant().then(
        function (data){
            console.log('access token' + data.body['access_token']);
            spotifyApi.setAccessToken(data.body['access_token']);
            spotifyApi.getArtistAlbums(req.query['id'],{ limit: 3}).then(
                function(data){
                    res.header("Access-Control-Allow-Origin","*");
                    res.send(JSON.stringify(data.body));
                },
                function(err){
                    console.log('wrong',err);
                }

            );

        },
        function(err){
            console.log('wrong',err);
        }
    );

});
app.get('/venue',(req,res)=>{
    venueurl="https://app.ticketmaster.com/discovery/v2/venues.json?apikey=VoKc9EuVCyTafOPJF7sGMyrQUVO9JuQE&keyword="
    venueurl+=req.query.keyword
    console.log(req.query.keyword)
    console.log(venueurl)
    axios.get(venueurl)
        .then(response=>{
            res.header("Access-Control-Allow-Origin","*");
            res.send(JSON.stringify(response.data._embedded))
        })
        .catch(error => {
            console.log(error);
        });
})


module.exports = app;
