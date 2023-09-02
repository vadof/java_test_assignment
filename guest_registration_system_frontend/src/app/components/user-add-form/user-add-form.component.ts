import {Component, Input} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {personalCodeValidator} from "../../validators/PersonalCodeValidator";
import {ApiService} from "../../services/api.service";
import {IUserInvitation} from "../../models/IUserInvitation";

@Component({
  selector: 'app-user-add-form',
  templateUrl: './user-add-form.component.html',
  styleUrls: ['./user-add-form.component.scss']
})
export class UserAddFormComponent {

  @Input() eventId: number = 0;
  @Input() userInvitation: IUserInvitation | null = null;

  constructor(private api: ApiService) {
  }

  userAddForm = new FormGroup({
    firstname: new FormControl<string>('', Validators.required),
    lastname: new FormControl<string>('', Validators.required),
    personalCode: new FormControl<string>('', [Validators.required, personalCodeValidator]),
    paymentMethod: new FormControl<string>('', Validators.required),
    info: new FormControl<string>('', Validators.required)
  })

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
          console.log(response)
        }, error => {
          console.log(error)
        });
      } else {
        // TODO send put request
      }
    }
  }
}
