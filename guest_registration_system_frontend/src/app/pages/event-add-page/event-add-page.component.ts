import { Component } from '@angular/core';
import {AbstractControl, FormControl, FormGroup, ValidatorFn, Validators} from "@angular/forms";
import {ApiService} from "../../services/api.service";
import {Router} from "@angular/router";

function customFutureDateTimeValidator(): ValidatorFn {
  return (control: AbstractControl): { [key: string]: any } | null => {
    const value = control.value;

    if (!value) {
      return null;
    }

    const regex = /^(0[1-9]|[12][0-9]|3[01])\.(0[1-9]|1[0-2])\.\d{4}\s([01]\d|2[0-3]):([0-5]\d)$/;

    if (!regex.test(value)) {
      return { invalidFormat: true };
    }

    const currentDate = new Date();
    const selectedDate = convertStringToDate(value)

    if (selectedDate <= currentDate ||
      (selectedDate.getDay() === currentDate.getDay()
        && selectedDate.getMonth() === currentDate.getMonth()
        && selectedDate.getFullYear() === currentDate.getFullYear())) {
      return { futureDateTime: true };
    }

    return null;
  };
}

function convertStringToDate(date: string): Date {
  const [datePart, timePart] = date.split(' ');

  const [day, month, year] = datePart.split('.').map(Number);
  const [hour, minute] = timePart.split(':').map(Number);

  return new Date(year, month - 1, day, hour, minute);
}

@Component({
  selector: 'app-event-add-page',
  templateUrl: './event-add-page.component.html',
  styleUrls: ['./event-add-page.component.scss']
})
export class EventAddPageComponent {

  eventForm = new FormGroup({
    name: new FormControl<string>('', [Validators.required]),
    date: new FormControl<string>('', [Validators.required, customFutureDateTimeValidator()]),
    place: new FormControl<string>('', [Validators.required]),
    info: new FormControl<string>('', [Validators.required])
  })

  constructor(
    private api: ApiService,
    private router: Router
  ) {
  }

  public createEvent() {
    if (this.eventForm.valid) {
      const eventForm = {
        name: this.eventForm.controls.name.value,
        date: convertStringToDate(this.eventForm.controls.date.value + ''),
        place: this.eventForm.controls.place.value,
        additionalInfo: this.eventForm.controls.info.value
      }

      this.api.sendPostRequest('/v1/event', eventForm).subscribe(
        response => {
          const id = response;
          this.router.navigate(['/event/' + id])
        }
      )
    }
  }
}
