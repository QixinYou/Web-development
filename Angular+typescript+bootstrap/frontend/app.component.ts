import { Component } from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'hw8-app';

  selectedLink = 'search-component';

  onLinkClick(link: string) {
    this.selectedLink = link;
  }
}
