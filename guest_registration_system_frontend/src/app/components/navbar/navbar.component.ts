import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent implements OnInit {

  homePageSelected: boolean = false;
  eventAddSelected: boolean = false;

  constructor(private router: Router) {
  }

  ngOnInit(): void {
    const href: string = this.router.url;
    if (href === '/') {
      this.homePageSelected = true;
    } else if (href === '/event/add') {
      this.eventAddSelected = true;
    }
  }

}
