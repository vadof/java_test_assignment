import {Component, Input, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {personalCodeValidator} from "../../validators/PersonalCodeValidator";
import {ApiService} from "../../services/api.service";
import {IUserInvitation} from "../../models/IUserInvitation";
import {EventService} from "../../services/event.service";

@Component({
  selector: 'app-user-add-form',
  templateUrl: './user-add-form.component.html',
  styleUrls: ['./user-add-form.component.scss']
})
export class UserAddFormComponent implements OnInit {

  @Input() eventId: number = 0;
  @Input() userInvitation: IUserInvitation | null = null;

  constructor(private api: ApiService,
              private eventService: EventService) {
  }

  userAddForm = new FormGroup({
    firstname: new FormControl<string>('', Validators.required),
    lastname: new FormControl<string>('', Validators.required),
    personalCode: new FormControl<string>('', [Validators.required, personalCodeValidator]),
    paymentMethod: new FormControl<string>('', Validators.required),
    info: new FormControl<string>('', Validators.required)
  })

  ngOnInit(): void {
    if (this.userInvitation) {
      this.userAddForm.patchValue({
        firstname: this.userInvitation.user.firstname,
        lastname: this.userInvitation.user.lastname,
        personalCode: this.userInvitation.user.personalCode + '',
        paymentMethod: this.userInvitation.paymentMethod,
        info: this.userInvitation.additionalInfo
      });
    }
  }

  addUser() {
    if (this.userAddForm.valid) {
      const requestObject = {
        firstname: this.userAddForm.controls.firstname.value,
        lastname: this.userAddForm.controls.lastname.value,
        personalCode: this.userAddForm.controls.personalCode.value,
        paymentMethod: this.userAddForm.controls.paymentMethod.value,
        additionalInfo: this.userAddForm.controls.info.value
      }

      if (!this.userInvitation) {
        this.api.sendPostRequest(`/v1/event/${this.eventId}/user`, requestObject).subscribe(response => {
          this.eventService.event.userInvitations.push(response as IUserInvitation);
          this.userAddForm.reset();
        }, error => {
          console.log(error)
        });
      } else {
        this.api.sendPutRequest(`/v1/event/${this.eventId}/user/${this.userInvitation.id}`, requestObject).subscribe(
          response => {
            this.eventService.event.userInvitations = this.eventService.event.userInvitations
              .filter(ui => ui.id !== this.userInvitation?.id)
            this.eventService.event.userInvitations.push(response as IUserInvitation);
            this.backToEventPage();
        }, error => {
            console.log(error);
          })
      }
    }
  }

  backToEventPage() {
    this.eventService.changeUserInvitation = null;
    this.eventService.changeCompanyInvitation = null;
  }
}
