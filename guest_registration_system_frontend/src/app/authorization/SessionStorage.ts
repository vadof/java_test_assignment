import {Injectable} from "@angular/core";

const PERSONAL_CODE_KEY = "PERSONAL_CODE";

@Injectable({
  providedIn: 'root'
})
export class SessionStorage {
  constructor() {
  }

  public getPersonalCode(): string | null {
    return sessionStorage.getItem(PERSONAL_CODE_KEY)
  }

  public savePersonalCode(personalCode: string): void {
    sessionStorage.setItem(PERSONAL_CODE_KEY, personalCode)
  }
}
