import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { GroupService } from '../group.service';
import { Group } from '../../persistence/Group';
import { CFM, KeyEvents, KeyEventKeys } from '../../common/common-functions.module';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-edit',
  templateUrl: './edit.component.html',
  styleUrls: ['./edit.component.css']
})
export class EditComponent implements OnInit {
  public group: Group;
  private subscriptions: Subscription[] = []

  constructor(private route: ActivatedRoute, private serv: GroupService, private router: Router) { }

  ngOnInit() {
    this.subscriptions.push(this.route.params.subscribe(params => {
      var id = params['id'];

      this.serv.getGroup(id, (group: Group) => {
        this.group = group;
      }, (error: any) => {
        // TODO proper error handling
      });
    }));

    
    CFM.linkKeyEventOnInputToAction("name", KeyEvents.keyUp, KeyEventKeys.enter, () => {
      CFM.getElementByID("description").focus();
    });
    CFM.linkKeyEventOnInputToAction("description", KeyEvents.keyUp, KeyEventKeys.enter, () => {
      CFM.getElementByID("admin").focus();
    });
    CFM.linkKeyEventOnInputToAction("admin", KeyEvents.keyUp, KeyEventKeys.enter, () => {
      CFM.getElementByID("create").click();
    });
  }

  ngOnDestroy() {
    this.subscriptions.forEach((value: Subscription) => {
      value.unsubscribe()
    })
  }

  cancel() {
    this.router.navigate(['../groups']);
  }

  update() {
    this.group.name = CFM.getElementByIDValue("name");
    this.group.description = CFM.getElementByIDValue("description");
    this.group.admin = CFM.getElementByIDValue("admin");

    this.serv.updateGroup(this.group, (group: Group) => {
      this.router.navigate(['../groups']);
    }, (error: any) => {
      // TODO proper error handling.
    });
  }
}
