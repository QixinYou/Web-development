
import {Component, OnInit,AfterViewInit,HostListener} from '@angular/core';
interface Eventsave{
  save_eventid:string;
  save_eventname:string;
  save_date:string;
  save_category:string;
  save_eventvenue:string;

}




@Component({
  selector: 'app-favorites',
  templateUrl: './favorites.component.html',
  styleUrls: ['./favorites.component.css']
})

export class FavoritesComponent {
  empty:boolean=false;
  events:  Eventsave[] = [];
  constructor() {
    this. loadEvents();
    this.isempty();
  }
  preventdefault(event: MouseEvent) {
    event.preventDefault();
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

  isempty(){

    if (this.events.length === 0){
      this.empty=true;
      console.log(this.empty);

    }
    else{
      this.empty=false;
      console.log(this.empty);
    }
  }

  delete(i:number){
    this.events.splice(i,1);
    localStorage.setItem('events',JSON.stringify(this.events));
    console.log('deleted');
    alert('Removed from favorites');
    this.loadEvents();
    this.isempty();
  }
}
