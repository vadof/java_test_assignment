import {Component, Input} from '@angular/core';
import {ApiService} from "../../services/api.service";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {personalCodeValidator} from "../../validators/PersonalCodeValidator";
import {ICompanyInvitation} from "../../models/ICompanyInvitation";

@Component({
  selector: 'app-company-add-form',
  templateUrl: './company-add-form.component.html',
  styleUrls: ['./company-add-form.component.scss']
})
export class CompanyAddFormComponent {
  @Input() eventId: number = 0;
  @Input() companyInvitation: ICompanyInvitation | null = null;

  constructor(private api: ApiService) {
  }

  companyAddForm = new FormGroup({
    name: new FormControl<string>('', Validators.required),
    registryCode: new FormControl<string>('', Validators.required),
    participants: new FormControl<string>('', Validators.required),
    paymentMethod: new FormControl<string>('', Validators.required),
    info: new FormControl<string>('', Validators.required)
  })

  addCompany() {
    console.log("FORM - " + this.companyAddForm.valid)
    if (this.companyAddForm.valid) {
      const requestObject = {
        name: this.companyAddForm.controls.name.value,
        registryCode: this.companyAddForm.controls.registryCode.value,
        participants: this.companyAddForm.controls.participants.value,
        paymentMethod: this.companyAddForm.controls.paymentMethod.value,
        additionalInfo: this.companyAddForm.controls.info.value
      }

      if (!this.companyInvitation) {
        this.api.sendPostRequest(`/v1/event/${this.eventId}/company`, requestObject).subscribe(response => {
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
