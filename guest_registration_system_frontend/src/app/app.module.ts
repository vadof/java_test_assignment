import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { HomePageComponent } from './pages/home-page/home-page.component';
import { NavbarComponent } from './components/navbar/navbar.component';
import {NgOptimizedImage} from "@angular/common";
import { FooterComponent } from './components/footer/footer.component';
import { LoginPageComponent } from './pages/login-page/login-page.component';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {HttpClientModule} from "@angular/common/http";
import { EventAddPageComponent } from './pages/event-add-page/event-add-page.component';
import { EventPageComponent } from './pages/event-page/event-page.component';
import { ImgHeaderComponent } from './components/img-header/img-header.component';
import { UserAddFormComponent } from './components/user-add-form/user-add-form.component';
import { CompanyAddFormComponent } from './components/company-add-form/company-add-form.component';

@NgModule({
  declarations: [
    AppComponent,
    HomePageComponent,
    NavbarComponent,
    FooterComponent,
    LoginPageComponent,
    EventAddPageComponent,
    EventPageComponent,
    ImgHeaderComponent,
    UserAddFormComponent,
    CompanyAddFormComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    NgbModule,
    NgOptimizedImage,
    ReactiveFormsModule,
    FormsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
