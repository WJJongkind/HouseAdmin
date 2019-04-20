import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LoginComponent } from './login.component';
import { RegisterComponent } from './register/register.component';
import { CookieService } from 'ngx-cookie-service';

@NgModule({
  imports: [
    CommonModule
  ],
  declarations: [LoginComponent, RegisterComponent],
  exports: [
    LoginComponent,
    RegisterComponent
  ],
  providers: [
    CookieService
  ]
})
export class LoginModule { }
