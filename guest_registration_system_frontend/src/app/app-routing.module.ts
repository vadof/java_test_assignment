import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {HomePageComponent} from "./pages/home-page/home-page.component";
import {AuthorizationGuard} from "./authorization/AuthorizationGuard";
import {LoginPageComponent} from "./pages/login-page/login-page.component";


const routes: Routes = [
  {path: '', component: HomePageComponent, canActivate: [AuthorizationGuard]},
  {path: 'login', component: LoginPageComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
