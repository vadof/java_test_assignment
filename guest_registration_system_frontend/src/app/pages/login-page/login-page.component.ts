import { Component } from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {personalCodeValidator} from "../../validators/PersonalCodeValidator";
import {ApiService} from "../../services/api.service";
import {SessionStorage} from "../../authorization/SessionStorage";
import {Router} from "@angular/router";

@Component({
  selector: 'app-login-page',
  templateUrl: './login-page.component.html',
  styleUrls: ['./login-page.component.scss']
})
export class LoginPageComponent {
  constructor(
    private api: ApiService,
    private storage: SessionStorage,
    private router: Router
  ) {}

  public errorMessage = ''

  loginForm = new FormGroup({
    personalCode: new FormControl<string>('', [Validators.required, personalCodeValidator]),
    firstname: new FormControl<string>(''),
    lastname: new FormControl<string>('')
  })

  login() {
    if (this.loginForm.valid) {
      const requestObj = {
        personalCode: this.loginForm.value.personalCode,
        firstname: this.loginForm.value.firstname,
        lastname: this.loginForm.value.lastname
      }
      this.api.sendPostRequest("/v1/login", requestObj).subscribe(
        () => {
          this.storage.savePersonalCode(this.loginForm.value.personalCode as string);
          this.router.navigate([''])
        }, error => {
          this.errorMessage = error.error;
          if (error.error === 'MISSING_FIRST_AND_LAST_NAME') {
            this.errorMessage = 'Te pole veel meie süsteemis registreerunud, palun sisestage oma ees- ja perekonnanimi.';
          } else {
            this.errorMessage = error.error;
          }
        }
      )
    } else {
      this.errorMessage = 'Invalid isikukood'
    }
  }
}
