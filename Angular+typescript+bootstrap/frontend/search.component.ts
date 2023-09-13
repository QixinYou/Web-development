import {Component, OnInit,AfterViewInit,HostListener} from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { FormControl } from '@angular/forms';
import { NgForm, FormGroup,FormsModule } from '@angular/forms';
interface Eventsave{
  save_eventid:string;
  save_eventname:string;
  save_date:string;
  save_category:string;
  save_eventvenue:string;

}
@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css']
})
export class SearchComponent implements  OnInit, AfterViewInit{
  categorys:string='Default';
  locationplace:string='';
  keyword:string='';
  distance:string='10';
  isChecked: boolean = false;

  showresult:boolean=false;

  noresult:boolean=false;

  longtitude:string='';

  latitude:string='';

  venuelongtitude:number=0;

  venuelatitude:number=0;

  artist:string='';

  eventdate:string='';

  venue:string='';

  genres:string='';

  pricerange:string='';

  ticketstatus:string='';

  eventname:string='';

  eventid:string='';

  ticketurl:string='';

  seatmap:string='';

  address:string='';

  phonenumber:string='';

  openhours:string='';

  generalrule:string='';

  childrule:string='';

  eventtype:string='';

  detailshow:boolean=false;

  favorite:boolean=false;

  showFullText:boolean=false;

  showFullText1:boolean=false;

  showFullText2:boolean=false;

  showvenuemap:boolean=false;

  ismusic:boolean=false;

  nogeo:boolean=false;



  spotifyArtistList:any=[]

  public artistlist:any=[];

  public eventscontent:any = [];

  public eventscontentdetail:any = [];

  public genresarray:any=[];
  public twitterApi = "https://twitter.com/intent/tweet?text="
  artistdetailList:any = [];

  keywordsearch = new FormControl();
  autooptions: string[] = [];
  events:  Eventsave[] = [];
  constructor() {
    this. loadEvents();
  }

  loadEvents():void{
    const storedEvents=localStorage.getItem('events');
    if(storedEvents){
      this.events=JSON.parse(storedEvents);
      console.log(this.events);
    }
  }
  clearlocalstorage():void {
    localStorage.clear();
  }
  ngOnInit() {
    this.keywordsearch.valueChanges
      .subscribe(truekeyword=>{
        var backendurl="/api/autocomplete?"
        backendurl+="keyword="+truekeyword
        fetch(backendurl)
          .then(autodata=>autodata.json())
          .then(autodata=>{


            let autocompletedata=[]
            for(let i=0;i<autodata.attractions.length;++i){
              autocompletedata.push(autodata.attractions[i].name)
            }
            this.autooptions=autocompletedata
            console.log(this.autooptions)

          })
      })


  }
  ngAfterViewInit() {

    this.categorys = 'Default';
  }
  clearlocainput(){
    this.locationplace='';
  }
  clearall(){
    this.keyword='';
    this.isChecked=false;
    this.categorys = 'Default';
    this.distance='10';
    this.locationplace='';
    this.showresult=false;
    this.noresult=false;
    this.detailshow=false;
  }

  searchresult(event:Event){
    event.preventDefault();
    this.eventscontent=[];
    this.detailshow=false;
    this.showresult=false;
    console.log(this.nogeo);
    if(this.isChecked==false){
      var str="&key=AIzaSyCq31ZK7DXurGbp1LicUHtTHomSfgYEcew";
      var googlegeo="https://maps.googleapis.com/maps/api/geocode/json?address=";
      googlegeo+=this.locationplace;
      googlegeo+=str;

      fetch(googlegeo)
        .then(geodata=>geodata.json())
        .then(geodata=>{
          console.log(geodata);
          console.log()
          if(geodata.results.length==0){
            this.noresult=true;
            this.showresult=true;
            console.log(this.noresult);
            console.log(this.showresult);
            console.log(this.detailshow);
          }
          this.latitude=geodata["results"][0]["geometry"]["location"]["lat"]
          this.longtitude=geodata["results"][0]["geometry"]["location"]["lng"]
          console.log(this.longtitude,this.latitude);
          return this.requestbackend();
        })
    }
    else{
      var ipinfourl="https://ipinfo.io/?token=9e68f44284039e"
      fetch(ipinfourl)
        .then(ipdata=>ipdata.json())
        .then(ipdata=>{
            var result=ipdata.loc.split(',');
            this.longtitude=result[1];
            this.latitude=result[0];
            this.requestbackend();
        })
    }
  }
  requestbackend(){
    this.eventscontent=[];
    console.log(this.longtitude,this.latitude)
    var backend="/api/search?"
    backend+="keyword="+this.keyword+"&category="+this.categorys+"&distance="+this.distance+"&latitude="+this.latitude+"&longitude="+this.longtitude;
    console.log(backend);
    fetch(backend)
      .then(res=>res.json())
      .then(res=>{
        console.log(res);
        console.log(res.page.totalElements);
        if(res.page.totalElements==0){
          this.showresult=true;
          this.noresult=true;
          console.log(this.showresult,this.noresult);
        }
        else{
          this.showresult=true;
          this.noresult=false;
          this.eventscontent=res._embedded.events;
          console.log(this.eventscontent);
          console.log(this.noresult);
          console.log(this.nogeo);
          console.log(this.detailshow);
        }
      })
  }

  showdetails(i:number){
    console.log(i);
    this.openhours='';
    this.phonenumber='';
    this.generalrule='';
    this.childrule='';
    this.venue='';
    this.eventscontentdetail=[];
    this.artistlist=[];
    this.address='' ;
    this.genres='';
    this.genresarray=[];
    this.artist='';
    this.seatmap='';
    this.ticketurl='';
    this.ticketstatus='';
    this.eventname='';
    this.eventdate='';
    this.pricerange='';
    this.artistdetailList=[];

    this.detailshow=true;
    this.noresult=true;
    this.ismusic=false;

    this.twitterApi = "https://twitter.com/intent/tweet?text="

    let choosenevent=this.eventscontent[i];
    var searchdetailurl="/api/detail?"
    searchdetailurl+="eventid="+choosenevent.id;
    console.log(this.ismusic);
    console.log(searchdetailurl)
    fetch(searchdetailurl)
      .then(res=>res.json())
      .then(res=>{
        this.eventscontentdetail=res;
        console.log(this.eventscontentdetail);
        this.eventid=this.eventscontentdetail.id;
        console.log(this.eventid);
        this.favorite=this.isEventInLocalStorage();
        if(this.eventscontentdetail.hasOwnProperty('name')) {
          this.eventname = this.eventscontentdetail.name;
        }
        if(this.eventscontentdetail.hasOwnProperty('dates')&&this.eventscontentdetail.dates.hasOwnProperty('start')
        &&this.eventscontentdetail.dates.start.hasOwnProperty('localDate')){
          this.eventdate=this.eventscontentdetail.dates.start.localDate;
        }
        if(this.eventscontentdetail.hasOwnProperty('_embedded')
        &&this.eventscontentdetail._embedded.hasOwnProperty('venues')&&
        this.eventscontentdetail._embedded.venues.length!=0){
          this.venue=this.eventscontentdetail._embedded.venues[0].name;
        }
        if(this.eventscontentdetail.hasOwnProperty('priceRanges')){
          this.pricerange+=this.eventscontentdetail.priceRanges[0].min+'-'+this.eventscontentdetail.priceRanges[0].max;
        }
        if(this.eventscontentdetail.hasOwnProperty('dates')&&this.eventscontentdetail.dates.hasOwnProperty('status')&&
        this.eventscontentdetail.dates.status.hasOwnProperty('code')){
          this.ticketstatus=this.eventscontentdetail.dates.status.code
        }
        if(this.eventscontentdetail.hasOwnProperty('url')){
          this.ticketurl=this.eventscontentdetail.url;
        }
        if(this.eventscontentdetail.hasOwnProperty('seatmap')&&this.eventscontentdetail.seatmap.hasOwnProperty('staticUrl')){
          this.seatmap=this.eventscontentdetail.seatmap.staticUrl;
        }
        if(this.eventscontentdetail.hasOwnProperty(('classifications'))) {
          this.genresarray=[];

          if(this.eventscontentdetail.classifications[0].hasOwnProperty('genre') &&this.eventscontentdetail.classifications[0].genre.name != 'Undefined'){
            this.genresarray.push(this.eventscontentdetail.classifications[0].genre.name);
          }
          if(this.eventscontentdetail.classifications[0].hasOwnProperty('segment') && this.eventscontentdetail.classifications[0].segment.name != 'Undefined'){
            this.genresarray.push(this.eventscontentdetail.classifications[0].segment.name);
            this.eventtype=this.eventscontentdetail.classifications[0].segment.name;
          }
          if (this.eventscontentdetail.classifications[0].hasOwnProperty('subGenre') && this.eventscontentdetail.classifications[0].subGenre.name != 'Undefined'){
            this.genresarray.push(this.eventscontentdetail.classifications[0].subGenre.name);
          }
          if(this.eventscontentdetail.classifications[0].hasOwnProperty('subType') && this.eventscontentdetail.classifications[0].subType.name != 'Undefined'){
            this.genresarray.push(this.eventscontentdetail.classifications[0].subType.name);
          }
          if(this.eventscontentdetail.classifications[0].hasOwnProperty('type')&& this.eventscontentdetail.classifications[0].type.name != 'Undefined'){
            this.genresarray.push(this.eventscontentdetail.classifications[0].type.name);
          }

          for(let i=0;i<this.genresarray.length;++i){
            this.genres+=this.genresarray[i]
            if(i!=this.genresarray.length-1){
              this.genres+='|'
            }
          }

        }
        if(this.eventscontentdetail.hasOwnProperty('_embedded')&&this.eventscontentdetail._embedded.hasOwnProperty('attractions')){
          for(let i=0;i<this.eventscontentdetail._embedded.attractions.length; i++) {
            this.artist += this.eventscontentdetail._embedded.attractions[i].name;
            this.artistlist.push(this.eventscontentdetail._embedded.attractions[i].name);
            if (i != this.eventscontentdetail._embedded.attractions.length - 1) {
              this.artist += '|'
            }
          }
        }
        this.twitterApi+="Check "+ this.eventname+" on Ticketmaster."+this.ticketurl;
        console.log(this.eventname, this.eventdate, this.venue, this.pricerange, this.ticketstatus, this.ticketurl, this.seatmap,this.genres,this.artist);
        if(this.eventtype=='Music'){
          this.ismusic=true;
          this.getvenuedetail1();

        }
        else {
          this.getvenuedetail();
        }

      })


  }
  getvenuedetail1(){
    var venuedetail="/api/venue?"
    console.log(this.artistlist);
    console.log(this.venue)
    venuedetail+="keyword="+this.venue;
    console.log(venuedetail)
    fetch(venuedetail)
      .then(res=>res.json())
      .then(res=>{
        console.log(res);
        this.venuelongtitude=parseFloat(res.venues[0].location.longitude);
        this.venuelatitude=parseFloat(res.venues[0].location.latitude);
        console.log(this.venuelongtitude,this.venuelatitude);
        if(res.hasOwnProperty('venues')&&res.venues.length>0){
          if(res.venues[0].hasOwnProperty('address')){
            if(res.venues[0].address.hasOwnProperty('line1')){
              this.address+=res.venues[0].address.line1;
              this.address+=', '
            }
          }
          if(res.venues[0].hasOwnProperty('city')){
            if(res.venues[0].city.hasOwnProperty('name')){
              this.address+=res.venues[0].city.name;
              this.address+=', '
            }
          }

          if(res.venues[0].hasOwnProperty('state')){
            if(res.venues[0].hasOwnProperty('name')){
              this.address+=res.venues[0].state.name
            }
          }
          if(res.venues[0].hasOwnProperty('boxOfficeInfo')){
            if(res.venues[0].boxOfficeInfo.hasOwnProperty('phoneNumberDetail')){
              this.phonenumber=res.venues[0].boxOfficeInfo.phoneNumberDetail;
              console.log(this.phonenumber);
            }
            if(res.venues[0].boxOfficeInfo.hasOwnProperty('openHoursDetail')){
              this.openhours=res.venues[0].boxOfficeInfo.openHoursDetail
            }
          }

          if(res.venues[0].hasOwnProperty('generalInfo')){
            if(res.venues[0].generalInfo.hasOwnProperty('generalRule')){
              this.generalrule=res.venues[0].generalInfo.generalRule
            }
            if(res.venues[0].generalInfo.hasOwnProperty('childRule')){
              this.childrule=res.venues[0].generalInfo.childRule
            }
          }


        }
        console.log(this.artistlist);
        this.getArtistDetailList();
        console.log(this.phonenumber,this.childrule,this.generalrule,this.address,this.openhours,this.eventtype)

      })
      .catch(error => {
        console.error('Error fetching venue details:', error);
      });


  }
  getvenuedetail(){
    var venuedetail="/api/venue?"
    console.log(this.venue)
    venuedetail+="keyword="+this.venue;
    console.log(venuedetail)
    fetch(venuedetail)
      .then(res=>res.json())
      .then(res=>{
        console.log(res);
        this.venuelongtitude=parseFloat(res.venues[0].location.longitude);
        this.venuelatitude=parseFloat(res.venues[0].location.latitude);
        console.log(this.venuelongtitude,this.venuelatitude);
        if(res.hasOwnProperty('venues')&&res.venues.length>0){
          if(res.venues[0].hasOwnProperty('address')){
            if(res.venues[0].address.hasOwnProperty('line1')){
              this.address+=res.venues[0].address.line1;
              this.address+=', '
            }
          }
          if(res.venues[0].hasOwnProperty('city')){
            if(res.venues[0].city.hasOwnProperty('name')){
              this.address+=res.venues[0].city.name;
              this.address+=', '
            }
          }

          if(res.venues[0].hasOwnProperty('state')){
            if(res.venues[0].hasOwnProperty('name')){
              this.address+=res.venues[0].state.name
            }
          }
          console.log(res.venues[0].boxOfficeInfo.phoneNumberDetail)
          if(res.venues[0].hasOwnProperty('boxOfficeInfo')){
            if(res.venues[0].boxOfficeInfo.hasOwnProperty('phoneNumberDetail')){
              this.phonenumber=res.venues[0].boxOfficeInfo.phoneNumberDetail;
              console.log(this.phonenumber);
            }
            if(res.venues[0].boxOfficeInfo.hasOwnProperty('openHoursDetail')){
              this.openhours=res.venues[0].boxOfficeInfo.openHoursDetail
            }
          }

          if(res.venues[0].hasOwnProperty('generalInfo')){
            if(res.venues[0].generalInfo.hasOwnProperty('generalRule')){
              this.generalrule=res.venues[0].generalInfo.generalRule
            }
            if(res.venues[0].generalInfo.hasOwnProperty('childRule')){
              this.childrule=res.venues[0].generalInfo.childRule
            }
          }


        }
        console.log(this.phonenumber,this.childrule,this.generalrule,this.address,this.openhours,this.eventtype)

      })


  }

  getArtistDetailList() {
    this.artistdetailList = [];
    const BackendUrl = "/api/spotify?";
    const Backendurl1 = "/api/spotifyalbum?";
    this.spotifyArtistList = [];
    console.log(this.artistlist);

    const fetchArtistDetails = (artist:string) => {
      return new Promise(async (resolve, reject) => {
        try {
          const artisturl = `${BackendUrl}artist=${artist}`;
          const res = await fetch(artisturl).then(res => res.json());
          console.log(res.artists.items[0]);

          const artistDetails = {
            id: res.artists.items[0].id,
            name: res.artists.items[0].name,
            followers: res.artists.items[0].followers.total,
            url: res.artists.items[0].external_urls.spotify,
            popularity: res.artists.items[0].popularity,
            image:res.artists.items[0].images[2].url,
            album1: "",
            album2: "",
            album3: "",
          };

          const albumUrl = `${Backendurl1}id=${res.artists.items[0].id}`;
          const albumRes = await fetch(albumUrl).then(res => res.json());
          console.log(albumRes);

          artistDetails.album1 = albumRes.items[0].images[1].url;
          artistDetails.album2 = albumRes.items[1].images[1].url;
          artistDetails.album3 = albumRes.items[2].images[1].url;

          resolve(artistDetails);
        } catch (error) {
          console.error(`Error fetching artist details for ${artist}:`, error);
          reject(error);
        }
      });
    };

    Promise.all(this.artistlist.map(fetchArtistDetails)).then(details => {
      this.artistdetailList = details;
      console.log(this.artistdetailList);
    });
  }

  tryalbum(){
    var id="2sG4zTOLvjKG1PSoOyf5Ej"
    const BackendUrl = "/api/spotifyalbum?";
    var searchurl=BackendUrl+'id='+id;
    fetch(searchurl)
      .then(res=>res.json())
      .then(res=>{
        console.log(res);
      })
  }


  getalbumdetail(){
    console.log(this.artistdetailList);
  }
  backtotable(){
    this.detailshow=false;
    this.noresult=false;
  }
  toggleFullText(){
    this.showFullText=!this.showFullText;
  }
  toggleFullText1(){
    this.showFullText1=!this.showFullText1;
  }

  toggleFullText2(){
    this.showFullText2=!this.showFullText2;
  }


  isEventInLocalStorage(): boolean {
    return this.events.some(
      (event) => event.save_eventid === this.eventid);
  }

  saveEvent(): void {
    this.favorite=!this.favorite;
    const newEvent: Eventsave = {
      save_eventid:this.eventid,
      save_eventname:this.eventname,
      save_date:this.eventdate,
      save_category:this.genres,
      save_eventvenue:this.venue,
    };
    console.log(newEvent);
    this.events.push(newEvent);
    localStorage.setItem('events', JSON.stringify(this.events));
    alert('Event Added to Favorites!');
    console.log('saved');
  }

  deleteEventByid(){
    this.favorite=!this.favorite;
    const eventindex=this.events.findIndex(event=>event.save_eventid===this.eventid);
    if(eventindex !==-1){
      this.events.splice(eventindex,1);
      localStorage.setItem('events',JSON.stringify(this.events));
      console.log('deleted');
      alert('Event Removed from Favorites!');
    }
    else{
      console.log('no this event');
    }
  }


}
