import requests
from flask import Flask, request, jsonify
from geolib import geohash

app=Flask(__name__)
@app.route('/')
def index():
    return app.send_static_file('index.html')
@app.route('/styles.css')
def css():
    return app.send_static_file("styles.css"), {"Content-Type": "text/css"}
@app.route('/scripts.js')
def scripts():
    return app.send_static_file("scripts.js")
@app.route('/search', methods=["GET"])
def search1():
    ticketmaster_url="https://app.ticketmaster.com/discovery/v2/events.json?apikey=VoKc9EuVCyTafOPJF7sGMyrQUVO9JuQE"
    a=request.args
    # keyword
    keyword=a.get("keyword")
    ticketmaster_url +="&keyword=" + keyword
    #segment id
    segment_dec=a.get("category")
    if segment_dec != "Default":
        if segment_dec == "Miscellaneous":
            segment_key ="KZFzniwnSyZfZ7v7n1"
            ticketmaster_url += "&segmentId=" + segment_key
        if segment_dec == "Film":
            segment_key = "KZFzniwnSyZfZ7v7nn"
            ticketmaster_url += "&segmentId=" + segment_key
        if segment_dec == "Arts":
            segment_key ="KZFzniwnSyZfZ7v7na"
            ticketmaster_url += "&segmentId=" + segment_key
        if segment_dec == "Music":
            segment_key ="KZFzniwnSyZfZ7v7nJ"
            ticketmaster_url += "&segmentId=" + segment_key
        if segment_dec == "Sports":
            segment_key ="KZFzniwnSyZfZ7v7nE"
            ticketmaster_url += "&segmentId=" + segment_key
    # radius
    radius = a.get("distance")
    ticketmaster_url += "&radius=" + radius
    #unit
    ticketmaster_url += "&unit=" + "miles"
    #geopoint
    lat=a.get("latitude")
    print(lat)
    long=a.get("longitude")
    print(long)
    geopoint=geohash.encode(lat,long,7)
    ticketmaster_url += "&geoPoint="+geopoint
    print(ticketmaster_url)
    #get json
    ticketmaster_data=requests.get(ticketmaster_url).json()
    ticketmaster_response= jsonify(ticketmaster_data)
    return ticketmaster_data

@app.route('/detail', methods=["GET"])
def detail1():
    detail_url="https://app.ticketmaster.com/discovery/v2/events/"
    a = request.args
    id=a.get("id")
    detail_url+=id
    detail_url+="?apikey=VoKc9EuVCyTafOPJF7sGMyrQUVO9JuQE&"
    print(detail_url)
    detail_data=requests.get(detail_url).json()
    return detail_data

@app.route('/venue', methods=["GET"])
def venue1():
    venue_url="https://app.ticketmaster.com/discovery/v2/venues?apikey=VoKc9EuVCyTafOPJF7sGMyrQUVO9JuQE&keyword="
    a=request.args
    venue=a.get("venue")
    venue_url+=venue
    print(venue_url)
    venue_data=requests.get(venue_url).json()
    return venue_data
if __name__ == '__main__':
    app.run(debug=True)