import { Injectable } from '@angular/core';
import {IEvent} from "../models/IEvent";
import {ApiService} from "./api.service";
import {IUserInvitation} from "../models/IUserInvitation";
import {ICompanyInvitation} from "../models/ICompanyInvitation";

@Injectable({
  providedIn: 'root'
})
export class EventService {

  public event!: IEvent;
  public changeUserInvitation: IUserInvitation | null = null;
  public changeCompanyInvitation: ICompanyInvitation | null = null;

  constructor() {

  }

  public setEvent(event: IEvent) {
    this.event = event;
  }
}
