import {Component, Input, OnInit} from '@angular/core';
import {ApiService} from "../../services/api.service";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {ICompanyInvitation} from "../../models/ICompanyInvitation";
import {EventService} from "../../services/event.service";
import {IUserInvitation} from "../../models/IUserInvitation";

@Component({
  selector: 'app-company-add-form',
  templateUrl: './company-add-form.component.html',
  styleUrls: ['./company-add-form.component.scss']
})
export class CompanyAddFormComponent implements OnInit {
  @Input() eventId: number = 0;
  @Input() companyInvitation: ICompanyInvitation | null = null;

  constructor(private api: ApiService,
              private eventService: EventService) {
  }

  companyAddForm = new FormGroup({
    name: new FormControl<string>('', Validators.required),
    registryCode: new FormControl<string>('', Validators.required),
    participants: new FormControl<string>('', Validators.required),
    paymentMethod: new FormControl<string>('', Validators.required),
    info: new FormControl<string>('', Validators.required)
  })

  ngOnInit(): void {
    if (this.companyInvitation) {
      this.companyAddForm.patchValue({
        name: this.companyInvitation.company.name,
        registryCode: this.companyInvitation.company.registryCode + '',
        participants: this.companyInvitation.participants + '',
        paymentMethod: this.companyInvitation.paymentMethod,
        info: this.companyInvitation.additionalInfo
      });
    }
  }

  addCompany() {
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
          this.eventService.event.companyInvitations.push(response as ICompanyInvitation);
          this.companyAddForm.reset();
        }, error => {
          console.log(error)
        });
      } else {
        this.api.sendPutRequest(`/v1/event/${this.eventId}/company/${this.companyInvitation.id}`, requestObject).subscribe(
          response => {
            this.eventService.event.companyInvitations = this.eventService.event.companyInvitations
              .filter(ci => ci.id !== this.companyInvitation?.id)
            this.eventService.event.companyInvitations.push(response as ICompanyInvitation);
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
