import { Injectable } from '@angular/core';
import {IEvent} from "../models/IEvent";
import {ApiService} from "./api.service";

@Injectable({
  providedIn: 'root'
})
export class EventService {

  public event!: IEvent;

  constructor(private api: ApiService) {

  }
}
