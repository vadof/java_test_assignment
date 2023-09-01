import { Component } from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {personalCodeValidator} from "../../validators/PersonalCodeValidator";

@Component({
  selector: 'app-user-add-form',
  templateUrl: './user-add-form.component.html',
  styleUrls: ['./user-add-form.component.scss']
})
export class UserAddFormComponent {

  userAddForm = new FormGroup({
    firstname: new FormControl<string>('', Validators.required),
    lastname: new FormControl<string>('', Validators.required),
    personalCode: new FormControl<string>('', [Validators.required, personalCodeValidator]),
    paymentMethod: new FormControl<string>('', Validators.required),
    info: new FormControl<string>('', Validators.required)
  })

  addUser() {
    if (this.userAddForm.valid) {

    }
  }
}
