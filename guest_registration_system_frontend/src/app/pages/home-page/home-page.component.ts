import { Component } from '@angular/core';
import {ApiService} from "../../services/api.service";
import {IEvent} from "../../models/IEvent";

@Component({
  selector: 'app-home-page',
  templateUrl: './home-page.component.html',
  styleUrls: ['./home-page.component.scss']
})
export class HomePageComponent {

  public previousEvents: IEvent[] = []
  public futureEvents: IEvent[] = []

  constructor(private api: ApiService) {
    this.api.sendGetRequest("/v1/user").subscribe(
      response => {
        let events: IEvent[] = response as IEvent[];
        const currentDate = new Date();
        for (const event of events) {
          const eventDate = new Date(event.date);

          if (eventDate < currentDate) {
            this.previousEvents.push(event);
          } else {
            this.futureEvents.push(event);
          }
        }

        this.previousEvents.sort((a, b) => new Date(a.date).getTime() - new Date(b.date).getTime());
        this.futureEvents.sort((a, b) => new Date(a.date).getTime() - new Date(b.date).getTime());
    }, error => {
      console.log(error)
    })
  }

  // TODO if organizer remove event else leave from event
  remove() {

  }
}
