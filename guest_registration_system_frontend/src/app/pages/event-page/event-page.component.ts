import {Component, OnInit} from '@angular/core';
import {ApiService} from "../../services/api.service";
import {ActivatedRoute, Router} from "@angular/router";
import {IEvent} from "../../models/IEvent";
import {SessionStorage} from "../../authorization/SessionStorage";
import {EventService} from "../../services/event.service";
import {IUser} from "../../models/IUser";
import {IUserInvitation} from "../../models/IUserInvitation";
import {ICompanyInvitation} from "../../models/ICompanyInvitation";

@Component({
  selector: 'app-event-page',
  templateUrl: './event-page.component.html',
  styleUrls: ['./event-page.component.scss']
})
export class EventPageComponent implements OnInit {

  public changeUserInvitation: IUserInvitation | null = null;
  public changeCompanyInvitation: ICompanyInvitation | null = null;

  public eventId: number = 0;
  public event!: IEvent;
  public personalCode: number = 0;

  public displayUserForm: boolean = true;
  public displayCompanyForm: boolean = false;

  public hasAccessToChangeUserData: boolean = false;
  public organizer: boolean = false;

  constructor(
    private api: ApiService,
    private router: Router,
    private route: ActivatedRoute,
    private storage: SessionStorage,
    private eventService: EventService
  ) {
  }

  ngOnInit(): void {
    this.route.paramMap.subscribe(async params => {
      this.eventId = +params.get('id')!
      this.api.sendGetRequest(`/v1/event/${this.eventId}`).subscribe(
        response => {
          this.eventService.setEvent(response as IEvent)
          this.event = this.eventService.event;

          this.eventService.changeUserInvitation = null;
          this.eventService.changeCompanyInvitation = null;

          const personalCode = this.storage.getPersonalCode();
          if (personalCode !== null) {

            this.organizer = this.event.organizer.personalCode === +personalCode;
            this.personalCode = +personalCode;

            if (!this.organizer) {
              this.hasAccessToChangeUserData = this.event.admins
                .some((admin) => admin.personalCode === +personalCode);
            }
          }
        }, () => {
          this.router.navigate([''])
        }
      )
    });
  }

  removeUserInvitation(id: number) {
    this.api.sendDeleteRequest(`/v1/event/${this.eventId}/user/${id}`).subscribe(() => {
      this.eventService.event.userInvitations = this.eventService.event.userInvitations
        .filter((userInvitation) => userInvitation.id !== id)
    }, error => {
      console.log(error)
    })
  }

  removeCompanyInvitation(id: number) {
    this.api.sendDeleteRequest(`/v1/event/${this.eventId}/company/${id}`).subscribe(() => {
      this.eventService.event.companyInvitations = this.eventService.event.companyInvitations
        .filter((companyInvitation) => companyInvitation.id !== id)
    }, error => {
      console.log(error)
    })
  }

  userIsModerator(user: IUser): boolean {
    return this.event.admins.some((u) => u.id === user.id)
  }

  changeModeratorRole(user: IUser): void {
    this.api.sendPostRequest(`/v1/event/${this.eventId}/moderator`, user).subscribe(
      (response) => {
        const updatedUser = response as IUser;
        let isAdmin = this.event.admins.some(u => u.id === user.id);
        if (isAdmin) {
          this.eventService.event.admins = this.eventService.event.admins.filter(u => u.id !== updatedUser.id);
        } else {
          this.eventService.event.admins.push(updatedUser);
        }
    })
  }

  changeUserInfo(userInvitation: IUserInvitation) {
    this.changeUserInvitation = this.eventService.changeUserInvitation = userInvitation;
    this.changeCompanyInvitation = this.eventService.changeCompanyInvitation = null;
  }

  changeCompanyInfo(companyInvitation: ICompanyInvitation) {
    this.changeCompanyInvitation = this.eventService.changeCompanyInvitation = companyInvitation;
    this.changeUserInvitation = this.eventService.changeUserInvitation = null;
  }

  showInfoChange(): boolean {
    return this.eventService.changeCompanyInvitation !== null
      || this.eventService.changeUserInvitation !== null;
  }
}
