import {Injectable} from "@angular/core";
import {CanActivate, Router} from "@angular/router";
import {SessionStorage} from "./SessionStorage";


@Injectable({
  providedIn: 'root'
})
export class AuthorizationGuard implements CanActivate {

  constructor(
    private router: Router,
    private sessionStorage: SessionStorage
  ) {}

  canActivate(): boolean {
    if (this.sessionStorage.getPersonalCode()) {
      return true;
    } else {
      this.router.navigate(['login']);
      return false;
    }
  }
}
