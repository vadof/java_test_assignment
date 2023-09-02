import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {HomePageComponent} from "./pages/home-page/home-page.component";
import {AuthorizationGuard} from "./authorization/AuthorizationGuard";
import {LoginPageComponent} from "./pages/login-page/login-page.component";
import {EventAddPageComponent} from "./pages/event-add-page/event-add-page.component";
import {EventPageComponent} from "./pages/event-page/event-page.component";


const routes: Routes = [
  {path: '', component: HomePageComponent, canActivate: [AuthorizationGuard]},
  {path: 'login', component: LoginPageComponent},
  {path: 'event/add', component: EventAddPageComponent, canActivate: [AuthorizationGuard]},
  {path: 'event/:id', component: EventPageComponent, canActivate: [AuthorizationGuard]}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
