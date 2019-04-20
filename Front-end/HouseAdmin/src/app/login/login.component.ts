import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { LoginService } from './login.service';
import { CFM, KeyEvents, KeyEventKeys } from '../common/common-functions.module';
import { CookieService } from 'ngx-cookie-service';
import { HttpErrorResponse } from '@angular/common/http';
import { LoginResponse } from '../persistence/LoginResponse';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  constructor(private router: Router, private serv: LoginService, private tool: CFM) { }

  ngOnInit() {
    CFM.linkKeyEventOnInputToAction("username", KeyEvents.keyUp, KeyEventKeys.enter, () => {
      CFM.getElementByID("password").focus();
    });
    CFM.linkKeyEventOnInputToAction("password", KeyEvents.keyUp, KeyEventKeys.enter, () => {
      CFM.getElementByID("login").click();
    });
  }

  login() {
    var username = CFM.getElementByIDValue("username");
    var password = CFM.getElementByIDValue("password");

    this.serv.login(username, password, (response: LoginResponse) => {
      this.tool.deleteCookies();
      this.tool.setCookie("sessionID", response.id);
      this.router.navigateByUrl('/groups');
    }, (error: any) => {
      // TODO proper error handling
    });
  }
}
