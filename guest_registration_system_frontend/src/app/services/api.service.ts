import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {SessionStorage} from "../authorization/SessionStorage";

const API_URL = 'http://localhost:8080/api'

@Injectable({
  providedIn: 'root'
})
export class ApiService {

  constructor(
    private http: HttpClient,
    private storage: SessionStorage
  ) {}

  private getHttpOptions() {
    const personalCode: string | null = this.storage.getPersonalCode();
    if (personalCode) {
      return {
        headers: new HttpHeaders({
          'Content-Type': 'application/json',
          'personalCode': personalCode
        })
      }
    } else {
      return {
        headers: new HttpHeaders({
          'Content-Type': 'application/json',
        })
      }
    }
  }

  public sendPostRequest(url: string, body: any) {
    return this.http.post(API_URL + url, body, this.getHttpOptions());
  }

  public sendGetRequest(url: string) {
    return this.http.get(API_URL + url, this.getHttpOptions())
  }

  public sendDeleteRequest(url: string) {
    return this.http.delete(API_URL + url, this.getHttpOptions())
  }
}
