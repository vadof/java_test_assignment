import {Component, OnInit} from '@angular/core';
import {ApiService} from "../../services/api.service";
import {ActivatedRoute, Router} from "@angular/router";
import {IEvent} from "../../models/IEvent";
import {SessionStorage} from "../../authorization/SessionStorage";

@Component({
  selector: 'app-event-page',
  templateUrl: './event-page.component.html',
  styleUrls: ['./event-page.component.scss']
})
export class EventPageComponent implements OnInit {

  private eventId: number = 0;
  public event!: IEvent;

  public displayUserForm: boolean = true;
  public displayCompanyForm: boolean = false;

  public membersIndex = 0;

  public hasAccessToChangeUserData: boolean = false;
  public organizer: boolean = false;

  constructor(
    private api: ApiService,
    private router: Router,
    private route: ActivatedRoute,
    private storage: SessionStorage
  ) {
  }

  ngOnInit(): void {
    this.route.paramMap.subscribe(async params => {
      this.eventId = +params.get('id')!
      this.api.sendGetRequest(`/v1/event/${this.eventId}`).subscribe(
        response => {
          this.event = response as IEvent;

          const personalCode = this.storage.getPersonalCode();
          if (personalCode !== null) {
            this.organizer = this.event.organizer.personalCode === +personalCode;
            if (!this.organizer) {
              this.hasAccessToChangeUserData = this.event.userInvitations
                .some((invitation) => invitation.user.personalCode === +personalCode);
            }

            console.log(this.organizer)
            console.log(this.hasAccessToChangeUserData)
          }
        }, () => {
          this.router.navigate([''])
        }
      )
    });
  }

}
