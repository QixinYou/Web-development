var latitude;
var longitude;
var global_eventlist
var global_detail
function cleanform(){
	if(document.getElementById('location').checked){
		document.getElementById('search_5').innerHTML='<input style="width:96%; height:25px;border-style: solid; border-radius:5px;border-color:white; border-width:1px;background-color:rgba(255,255,255,0); color:white" type="text" id="inputlocation" required>';
		document.getElementById('searchpart').style.height='350px';
		document.getElementById('search_form').reset();
		document.getElementById('error').innerHTML='';
		document.getElementById('searchresult').innerHTML='';
		document.getElementById('detail').style.width='0px';
		document.getElementById('detail').style.height='0px';
		document.getElementById('venuedetail2').style.width='0px';
		document.getElementById('venuedetail2').style.height='0px';
		document.getElementById('header2').innerHTML='';
		document.getElementById('detailpart').innerHTML='';
		document.getElementById('seatmap').innerHTML='';
		document.getElementById('showvenue').innerHTML='';
		document.getElementById('header3').innerHTML='';
		document.getElementById('venuephoto').innerHTML='';
		document.getElementById('venueleft').innerHTML='';
		document.getElementById('venueright').innerHTML='';
		document.getElementById('venuedetail').style.width='0px';
		document.getElementById('venuedetail').style.padding='0px';
		document.getElementById('showarrow').innerHTML='';
	}
	document.getElementById('search_form').reset();
	document.getElementById('error').innerHTML='';
	document.getElementById('searchresult').innerHTML='';
	document.getElementById('detail').style.width='0px';
	document.getElementById('detail').style.height='0px';
	document.getElementById('venuedetail2').style.width='0px';
	document.getElementById('venuedetail2').style.height='0px';
	document.getElementById('header2').innerHTML='';
	document.getElementById('detailpart').innerHTML='';
	document.getElementById('seatmap').innerHTML='';
	document.getElementById('showvenue').innerHTML='';
	document.getElementById('header3').innerHTML='';
	document.getElementById('venuephoto').innerHTML='';
	document.getElementById('venueleft').innerHTML='';
	document.getElementById('venueright').innerHTML='';
	document.getElementById('venuedetail').style.width='0px';
	document.getElementById('venuedetail').style.padding='0px';
	document.getElementById('showarrow').innerHTML='';

}
function LocationhereInput(){
	if(document.getElementById('location').checked){
		document.getElementById('search_5').innerHTML="";
		document.getElementById('searchpart').style.height='325px';
	}
	else{
		document.getElementById('search_5').innerHTML='<input style="width:96%; height:25px;border-style: solid; border-radius:5px;border-color:white; border-width:1px;background-color:rgba(255,255,255,0); color:white" type="text" id="inputlocation" required>';
		document.getElementById('searchpart').style.height='350px';
	}
}
function check(event){
	var isValid1=document.querySelector('#keyword').reportValidity();
	if(isValid1==true){
		if(document.getElementById('location').checked){
			ipinfo="https://ipinfo.io/?token=9e68f44284039e"
			var response=fetch(ipinfo)
			response.then(res=>res.json())
					.then(function(ipdata){
					 console.log(ipdata)
					 var result=ipdata["loc"].split(",");
					 latitude=result[0]
					 longitude=result[1]
					 console.log(latitude)
					 console.log(longitude)
					 requestBack(latitude,longitude);
				 })
		}
		else{
		var isValid2=document.querySelector('#inputlocation').reportValidity();
		if(isValid2==true){
			var str="&key=AIzaSyCBeXZ4yVSkvbpgd18fZbQxLaiodBClung";
			var googlegeo= "https://maps.googleapis.com/maps/api/geocode/json?address=";
			var inputlocation= document.getElementById("inputlocation").value;
			googlegeo+= inputlocation
			googlegeo+= str
			console.log(googlegeo)
			var response=fetch(googlegeo)
			response.then(res=>res.json())
					.then(function(geo){
						console.log(geo)
						latitude=geo["results"][0]["geometry"]["location"]["lat"]
						longitude=geo["results"][0]["geometry"]["location"]["lng"]
						console.log("geo:",latitude,longitude)
						requestBack(latitude,longitude)
						
					})
			}		
		
		
		}
	}
}

function requestBack(lat,longi){
	document.getElementById('error').innerHTML='';
	document.getElementById('searchresult').innerHTML='';
	document.getElementById('detail').style.width='0px';
	document.getElementById('detail').style.height='0px';
	document.getElementById('venuedetail2').style.width='0px';
	document.getElementById('venuedetail2').style.height='0px';
	document.getElementById('header2').innerHTML='';
	document.getElementById('detailpart').innerHTML='';
	document.getElementById('seatmap').innerHTML='';
	document.getElementById('showvenue').innerHTML='';
	document.getElementById('header3').innerHTML='';
	document.getElementById('venuephoto').innerHTML='';
	document.getElementById('venueleft').innerHTML='';
	document.getElementById('venueright').innerHTML='';
	document.getElementById('venuedetail').style.width='0px';
	document.getElementById('venuedetail').style.padding='0px';
	document.getElementById('showarrow').innerHTML='';
	var backend="/search?"
	
	var keyword=document.getElementById("keyword").value;
	var category=document.getElementById("category").value;
	var distance=document.getElementById("distance").value;
	if(distance==''){
		distance=10
	}
	backend += "keyword="+keyword+"&category="+category+"&distance="+distance+"&latitude="+lat+"&longitude="+longi
	var response=fetch(backend)
	response.then(res=>res.json())
			.then(function(event_data){
				console.log(event_data);
				global_eventlist=event_data;
				console.log(global_eventlist);
				return tableout();
			})
	
	
	
	
}
function tableout(){
	console.log(global_eventlist['page']['totalElements']);
	if(global_eventlist['page']['totalElements']==0){
		document.getElementById('error').innerHTML='<div id="noRecords" style="background-color: white; margin:auto;color:red; text-align: center; line-height:30px; width:1200px; height:30px;">No Records found</div>'
	}
	else{
		var tableHead=document.createElement('tr');
		var resultTable=document.getElementById('searchresult');
		document.getElementById('searchtable').style.width='1400px';
		var global_event_data=global_eventlist._embedded.events;
		tableHead.innerHTML='<th>Date</th><th>Icon</th><th onclick="tablesort(2)">Event</th><th onclick="tablesort(3)">Genre</th><th onclick="tablesort(4)">Venue</th>'
		resultTable.appendChild(tableHead);
		console.log(global_event_data.length);
		for(var i=0;i<global_event_data.length;++i){
			var newRow =document.createElement('tr');
			var localDate="";
			var localTime="";
			if(global_event_data[i].dates.hasOwnProperty('start')&&global_event_data[i].hasOwnProperty('dates')){
				if(global_event_data[i].dates.start.hasOwnProperty('localDate')){
					localDate=global_event_data[i].dates.start.localDate;
				}
				if(global_event_data[i].dates.start.hasOwnProperty('localTime')){
					localTime=global_event_data[i].dates.start.localTime;
				}
			}
			var imageUrl="";
			if(global_event_data[i].hasOwnProperty('images')&&global_event_data[i].images.length>0){
				if(global_event_data[i].images[0].hasOwnProperty('url')){
					imageUrl=global_event_data[i].images[0].url
				}
			}
			var name="";
			if(global_event_data[i].hasOwnProperty('name')){
				name=global_event_data[i].name;
			}
			
			var genre="";
			if(global_event_data[i].hasOwnProperty('classifications')){
				if(global_event_data[i].classifications[0].segment.name=="Undefined"){
					console.log("undefined");
				}
				else{
				genre=global_event_data[i].classifications[0].segment.name;
				}
			}
			var venname="";
			if(global_event_data[i]._embedded.venues[0].hasOwnProperty('name')){
				if(global_event_data[i]._embedded.venues[0].name=='Undefined'){
					venname='';
				}
				venname=global_event_data[i]._embedded.venues[0].name;
			}
			
			newRow.innerHTML="<td>"+localDate+"</br>"+localTime+"</td><td>"+
			'<img src="' +imageUrl+'"style="width:70px; height:40px"></img>'+'</td><td>'
			+'<a href="###" style="text-decoration:none" onclick="showDetail('+i+')">'+name+'</a>'+"</td><td>"+genre+"</td><td>"+venname+"</td>";
			resultTable.appendChild(newRow);
		}
	}
}

function tablesort(index){
	var wtable=document.querySelector('table');
	var wbody=wtable.tBodies[0];
	var wtr=wbody.rows;
	var temp=[];
	for(var i=1;i<wtr.length;i++){
		temp[i-1]=wtr[i];
	}
	
	if(wbody.sortCol== index){
		temp.reverse();
	}
	else{
		temp.sort(function(tr1,tr2){
			var temp1=tr1.cells[index].innerText;
			var temp2=tr2.cells[index].innerText;
			return temp1.localeCompare(temp2);

		})
	}
	var fragment= document.createDocumentFragment();
	for(var i=0;i<temp.length;i++){
		fragment.appendChild(temp[i]);
	}
	wbody.appendChild(fragment);
	wbody.sortCol=index;
}

function showDetail(Index){
	var backendSec="/detail?"
	var chooseEvent=global_eventlist._embedded.events[Index];
	var id=chooseEvent.id;
	backendSec+="id="+id;
	var response= fetch(backendSec)
	response.then(res=>res.json())
			.then(function(detaildata){
				console.log(detaildata);
				global_detail=detaildata;
				return detailMap();
			})
}

function detailMap(){
	document.getElementById('header3').innerHTML='';
	document.getElementById('venuephoto').innerHTML='';
	document.getElementById('venueleft').innerHTML='';
	document.getElementById('venueright').innerHTML='';
	document.getElementById('venuedetail').style.width='0px';
	document.getElementById('venuedetail').style.padding='0px';
	document.getElementById('detail').style.width='1200px';
	document.getElementById('detail').style.height='auto';
	var detailname=global_detail.name;
	document.getElementById('header2').innerHTML= detailname;
	var date='';
	if(global_detail.dates.start.hasOwnProperty('localDate')){
		date= global_detail.dates.start.localDate+' ';
	}
	if(global_detail.dates.start.hasOwnProperty('localTime')){
		date+=global_detail.dates.start.localTime;
	}
	var ArtList='';
	if(global_detail.hasOwnProperty('_embedded')&&global_detail._embedded.hasOwnProperty('attractions')){
		for(var i=0;i<global_detail._embedded.attractions.length;i++){
			if(global_detail._embedded.attractions[i].hasOwnProperty('url')){
				ArtList+='<a href="'+ global_detail._embedded.attractions[i].url+'"class="detaillink" target="_blank" style="font-family: Arial;text-decoration:none">'+global_detail._embedded.attractions[i].name+'</a>'
				
			}
			else{
				ArtList+=global_detail._embedded.attractions[i].name;
			}
			if(i!=global_detail._embedded.attractions.length-1){
				ArtList+=' '+'|'+' ';
			}
		}
	}
	var venue=''
	if(global_detail._embedded.hasOwnProperty('venues')){
		venue=global_detail._embedded.venues[0].name;
	}
	var genres=''
	if(global_detail.hasOwnProperty('classifications')){
		if(global_detail.classifications[0].hasOwnProperty('segment')&&global_detail.classifications[0].segment.name!='Undefined'){
			genres+=global_detail.classifications[0].segment.name;
		}
		if(global_detail.classifications[0].hasOwnProperty('genre')&&global_detail.classifications[0].genre.name!='Undefined'){
			if(global_detail.classifications[0].hasOwnProperty('segment')){
			genres+=' '+'|'+' ';
			}
			genres+=global_detail.classifications[0].genre.name;
		}
		if(global_detail.classifications[0].hasOwnProperty('subGenre')&&global_detail.classifications[0].subGenre.name!='Undefined'){
			genres+=' '+'|'+' '+global_detail.classifications[0].subGenre.name;
		}
		if(global_detail.classifications[0].hasOwnProperty('subType')&&global_detail.classifications[0].subType.name!='Undefined'){
			genres+=' '+'|'+' '+global_detail.classifications[0].subType.name;
		}
		if(global_detail.classifications[0].hasOwnProperty('type')&&global_detail.classifications[0].type.name!='Undefined'){
			genres+=' '+'|'+' '+global_detail.classifications[0].type.name;
		}
	}
	var price='';
	if(global_detail.hasOwnProperty('priceRanges')){
		price=global_detail.priceRanges[0].min+'-'+global_detail.priceRanges[0].max+' '+global_detail.priceRanges[0].currency;
	}
	var status1='';
	if(global_detail.dates.hasOwnProperty('status')){
		status1=global_detail.dates.status.code;
		
	}
	var buyurl='';
	if(global_detail.hasOwnProperty('url')){
		buyurl=global_detail.url;
	}
	var seatmap='';
	if(global_detail.hasOwnProperty('seatmap')){
		seatmap=global_detail.seatmap.staticUrl;
	}
	var temp=''
	if(date!=''){
		temp+='<h2 style="font-family: Arial">Date</h2><p style="font-family: Arial, Helvetica, sans-serif;color:white;">'+date+'</p>';
	}
	if(ArtList!=''){
		temp+='<h2 style="font-family: Arial">Artist/team</h2><p style="font-family: Arial, Helvetica, sans-serif; color:white">'+ArtList+'</p>';
	}
	if(venue!=''){
		temp+='<h2 style="font-family: Arial">Venue</h2><p style="font-family: Arial, Helvetica, sans-serif; color:white">'+venue+'</p>';
	}
	if(genres!=''){
		temp+='<h2 style="font-family: Arial">Genres</h2><p style="font-family: Arial, Helvetica, sans-serif; color:white">'+genres+'</p>';
	}
	if(price!=''){
		temp+='<h2 style="font-family: Arial">Price Ranges</h2><p style="font-family: Arial, Helvetica, sans-serif; color:white">'+price+'</p>';
	}
	if(status1!=''){
		if(status1=='onsale'){
		temp+='<h2 style="font-family: Arial">Ticket Status</h2><div style="font-family: Arial; background-color:green;width:fit-content;padding: 2px 5px 2px 5px;border-style:solid; border-radius:6px; border-color:green; color:white">On Sale</div>';
		}
		else if(status1=='offsale'){
		temp+='<h2 style="font-family: Arial">Ticket Status</h2><div style="font-family: Arial; background-color:red; width:fit-content;padding: 2px 5px 2px 5px; border-style:solid; border-radius:6px;border-color:red;color:white">Off Sale</div>';	
		}
		else if(status1=='cancelled'){
		temp+='<h2 style="font-family: Arial">Ticket Status</h2><div style="font-family: Arial; background-color:black; width:fit-content;padding: 2px 5px 2px 5px;border-style:solid; border-radius:6px;border-color:black; color:white">Cancelled</div>';	
		}
		else if(status1=='postponed'){
		temp+='<h2 style="font-family: Arial">Ticket Status</h2><div style="font-family: Arial; background-color:orange; width:fit-content; padding: 2px 5px 2px 5px; border-style:solid; border-radius:6px;border-color:orange; color:white">Postponed</div>';	
		}
		else if(status1=='rescheduled'){
		temp+='<h2 style="font-family: Arial">Ticket Status</h2><div style="font-family: Arial; background-color:orange; width:fit-content; padding: 2px 5px 2px 5px; border-style:solid; border-radius:6px;border-color:orange; color:white">Rescheduled</div>';	
		}
	}
	if(buyurl!=''){
		temp+='<h2 style="font-family: Arial">Buy Ticket At</h2><p><a href="'+buyurl+'"class="detaillink" target="_blank" style="font-family: Arial;text-decoration:none">Ticketmaster</a></p>'
	}
	document.getElementById('detailpart').innerHTML=temp;
	if(seatmap!=''){
		document.getElementById('seatmap').innerHTML='<img src="'+seatmap+'" style="width:450px;position: absolute;top: 50%;transform:translateY(-50%);right:5%"></img>'
	}
	document.getElementById('showvenue').innerHTML='Show Venue Details';
	document.getElementById('showarrow').innerHTML='<a href="###" style="text-decoration:none;color:white" onclick="showvenue()">'+'&#8964'+'</a>'
	
}
function showvenue(){

	if(global_detail._embedded.hasOwnProperty('venues')){
		venue=global_detail._embedded.venues[0].name;
		var backendThr="/venue?"
		var venue=global_detail._embedded.venues[0].name;
		backendThr+="venue="+venue;
		var response= fetch(backendThr)
		response.then(res=>res.json())
			.then(function(venuedata){
				console.log(venuedata);
				document.getElementById('showvenue').innerHTML='';
				document.getElementById('showarrow').innerHTML='';
				document.getElementById('venuedetail').style.backgroundColor='white';
				document.getElementById('venuedetail').style.padding='10px';
				document.getElementById('venuedetail').style.marginTop='50px';
				document.getElementById('venuedetail').style.width='1200px';
				document.getElementById('venuedetail').style.height='auto';
				document.getElementById('venuedetail').style.borderStyle='solid';
				document.getElementById('venuedetail2').style.borderColor='black';
				document.getElementById('venuedetail2').style.borderStyle='solid';
				document.getElementById('venuedetail2').style.width='1200px';
				document.getElementById('venuedetail2').style.height='auto';
				document.getElementById('header3').style.marginTop='10px';
				document.getElementById('header3').style.textDecoration='underline';
				document.getElementById('header3').style.fontSize='30px';
				document.getElementById('header3').style.textAlign='center';
				if(venuedata._embedded.venues[0].hasOwnProperty('name')){
					var name=venuedata._embedded.venues[0].name;
					document.getElementById('header3').innerHTML=name;
				}
				if(venuedata._embedded.venues[0].hasOwnProperty('images')){
					var venueimage=venuedata._embedded.venues[0].images[0].url;
					var height1=venuedata._embedded.venues[0].images[0].height;
					var width1=venuedata._embedded.venues[0].images[0].width;
					console.log(venueimage);
					document.getElementById('venuephoto').innerHTML='<img src= "'+venueimage+'" style="width:90px;height:60px" ></img>';
				}
				var address='';
				var address2='';
				if(venuedata._embedded.venues[0].hasOwnProperty('address')||venuedata._embedded.venues[0].hasOwnProperty('city')||venuedata._embedded.venues[0].hasOwnProperty('postalCode')){
				address+='Address:'
				}
				if(venuedata._embedded.venues[0].hasOwnProperty('address')){
				address+=venuedata._embedded.venues[0].address.line1+','+'</br>';
				address2+=venuedata._embedded.venues[0].address.line1+' ';
					if(venuedata._embedded.venues[0].hasOwnProperty('city')){
						address+=venuedata._embedded.venues[0].city.name+',';
						address2+=venuedata._embedded.venues[0].city.name+' ';
							if(venuedata._embedded.venues[0].hasOwnProperty('state')){
								address+=venuedata._embedded.venues[0].state.stateCode+'</br>';
								address2+=venuedata._embedded.venues[0].state.stateCode+' ';
								if(venuedata._embedded.venues[0].hasOwnProperty('postalCode')){
									address+=venuedata._embedded.venues[0].postalCode;
									address2+=venuedata._embedded.venues[0].postalCode;
								}
							}
							
							else{
								if(venuedata._embedded.venues[0].hasOwnProperty('postalCode')){
									address+=venuedata._embedded.venues[0].postalCode;
									address2+=venuedata._embedded.venues[0].postalCode;
								}
							}
						}	
					else{
						if(venuedata._embedded.venues[0].hasOwnProperty('state')){
								address+=venuedata._embedded.venues[0].state.stateCode+'</br>'
								address2+=venuedata._embedded.venues[0].state.stateCode+' '
								if(venuedata._embedded.venues[0].hasOwnProperty('postalCode')){
									address+=venuedata._embedded.venues[0].postalCode;
									address2+=venuedata._embedded.venues[0].postalCode;
								}
						}
						else{
								if(venuedata._embedded.venues[0].hasOwnProperty('postalCode')){
									address+=venuedata._embedded.venues[0].postalCode;
									address2+=venuedata._embedded.venues[0].postalCode;
								}
						}
					}
				}
				else{
					if(venuedata._embedded.venues[0].hasOwnProperty('city')){
						address+=venuedata._embedded.venues[0].city.name+', ';
						address2+=venuedata._embedded.venues[0].city.name+' ';
							if(venuedata._embedded.venues[0].hasOwnProperty('state')){
								address+=venuedata._embedded.venues[0].state.stateCode+'</br>';
								address2+=venuedata._embedded.venues[0].state.stateCode+' ';
								if(venuedata._embedded.venues[0].hasOwnProperty('postalCode')){
									address+=venuedata._embedded.venues[0].postalCode;
									address2+=venuedata._embedded.venues[0].postalCode;
								}
							}
							else{
								if(venuedata._embedded.venues[0].hasOwnProperty('postalCode')){
									address+=venuedata._embedded.venues[0].postalCode;
									address2+=venuedata._embedded.venues[0].postalCode;
								}
							}
						}	
					else{
						if(venuedata._embedded.venues[0].hasOwnProperty('state')){
								address+=venuedata._embedded.venues[0].state.stateCode+'</br>'
								address2+=venuedata._embedded.venues[0].state.stateCode+' '
								if(venuedata._embedded.venues[0].hasOwnProperty('postalCode')){
									address+=venuedata._embedded.venues[0].postalCode;
									address2+=venuedata._embedded.venues[0].postalCode;
								}
						}
						else{
								if(venuedata._embedded.venues[0].hasOwnProperty('postalCode')){
									address+=venuedata._embedded.venues[0].postalCode;
									address2+=venuedata._embedded.venues[0].postalCode;
								}
						}
					}
				}
				address+='</br>';
				var addressurl='';
				addressurl+=venuedata._embedded.venues[0].name+' '+address2;
				var final_addressurl=encodeURIComponent(addressurl);
				address+='<a href="https://www.google.com/maps/search/?api=1&query='+final_addressurl+'" class="venuelink" target="_blank" style="text-decoration:none; font-family: Arial, Helvetica, sans-serif">Open in Google Maps</a>';
				document.getElementById('venueleft').innerHTML=address;
				
				var upcomeingEvent='';
				if(venuedata._embedded.venues[0].hasOwnProperty('url')){
					upcomeingEvent=venuedata._embedded.venues[0].url
				}

				document.getElementById('venueright').innerHTML='<a href="'+upcomeingEvent+'" class="venuelink" target="_blank"  style="text-decoration:none; font-family: Arial, Helvetica, sans-serif">More events at this venue</a>';
			})
	}
}