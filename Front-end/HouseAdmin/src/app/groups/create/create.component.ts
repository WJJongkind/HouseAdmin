import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { GroupService } from '../group.service';
import { CFM, KeyEvents, KeyEventKeys } from '../../common/common-functions.module';
import { Group } from '../../persistence/Group';

@Component({
  selector: 'app-create',
  templateUrl: './create.component.html',
  styleUrls: ['./create.component.css']
})
export class CreateComponent implements OnInit {


  constructor(private router: Router, private serv: GroupService) { }

  ngOnInit() {
    CFM.linkKeyEventOnInputToAction("name", KeyEvents.keyUp, KeyEventKeys.enter, () => {
      CFM.getElementByID("description").focus();
    });
    CFM.linkKeyEventOnInputToAction("description", KeyEvents.keyUp, KeyEventKeys.enter, () => {
      CFM.getElementByID("create").click();
    });
  }

  cancel() {
    this.router.navigateByUrl('/groups');
  }

  create() {
    var name = CFM.getElementByIDValue("name");
    var description = CFM.getElementByIDValue("description");

    this.serv.createGroup(name, description, (response: Group) => {
      this.router.navigateByUrl('/groups');
    }, (error: any) => {
      // TODO proper error handling
    });
  }
}
