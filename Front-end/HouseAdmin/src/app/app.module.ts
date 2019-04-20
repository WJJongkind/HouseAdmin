import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HeaderComponent } from './header/header.component';
import { MainModule } from './main/main.module';
import { HttpClientModule } from '@angular/common/http';
import { GroupsModule } from './groups/groups.module';
import { CookieService } from 'ngx-cookie-service';
import { LoginModule } from './login/login.module';

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    MainModule,
    GroupsModule,
    LoginModule,
    HttpClientModule
  ],
  providers: [CookieService],
  bootstrap: [AppComponent]
})
export class AppModule { }
