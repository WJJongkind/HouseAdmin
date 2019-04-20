import { Component, OnInit } from '@angular/core';
import { CFM, KeyEvents, KeyEventKeys } from '../../common/common-functions.module';
import { Router } from '@angular/router';
import { RegisterService } from './register.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {
  public showRegistrationFailedError = false;
  public showServerUnreachableError = false;

  constructor(private router: Router, private serv: RegisterService) { }

  ngOnInit() {
    CFM.linkKeyEventOnInputToAction("email", KeyEvents.keyUp, KeyEventKeys.enter, () => {
      CFM.getElementByID("username").focus();
    });
    CFM.linkKeyEventOnInputToAction("username", KeyEvents.keyUp, KeyEventKeys.enter, () => {
      CFM.getElementByID("password").focus();
    });
    CFM.linkKeyEventOnInputToAction("password", KeyEvents.keyUp, KeyEventKeys.enter, () => {
      CFM.getElementByID("register").click();
    });
  }

  register() {
    var username = CFM.getElementByIDValue("username");
    var password = CFM.getElementByIDValue("password");
    var email = CFM.getElementByIDValue("email");

    this.serv.register(username, password, email, (success?: boolean, error?: number) => {
      this.showRegistrationFailedError = false;
      this.showServerUnreachableError = false;
      this.router.navigateByUrl('/');
    }, (error: any) => {
      // if(error == 0 || error == 500) {
      //   this.showServerUnreachableError = true;
      // }
      // TODO reenable error handling. Enable error booleans.
    });
  }

}
