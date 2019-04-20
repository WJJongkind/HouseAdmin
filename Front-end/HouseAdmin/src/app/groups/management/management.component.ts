import { Component, OnInit } from '@angular/core';
import { GroupService } from '../group.service';
import { Route, ActivatedRoute } from '@angular/router';
import { CFM, KeyEvents, KeyEventKeys } from '../../common/common-functions.module';
import { Group } from '../../persistence/Group';
import { errorHandler } from '@angular/platform-browser/src/browser';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-management',
  templateUrl: './management.component.html',
  styleUrls: ['./management.component.css']
})
export class ManagementComponent implements OnInit {
  public members: any[];
  private id;
  private subscriptions: Subscription[] = []

  constructor(private serv: GroupService, private route: ActivatedRoute) { }

  ngOnInit() {
    this.subscriptions.push(this.route.params.subscribe(params => {
      this.id = params['id'];
      this.serv.getGroup(this.id, (group: Group) => {
        this.members = group.members;
        CFM.deleteFromArray(this.members, group.admin);
      }, (error: any) => {
        // TODO proper error handling
      });
    }))

    CFM.linkKeyEventOnInputToAction("email", KeyEvents.keyUp, KeyEventKeys.enter, () => {
      CFM.getElementByID("add").click();
    });
  }

  ngOnDestroy() {
    this.subscriptions.forEach((value: Subscription) => {
      value.unsubscribe()
    })
  }

  delete(email: string) {
    this.serv.deleteGroupMember(email, this.id, () => {
      this.members = CFM.deleteFromArray(this.members, email);
    }, (error: any) => {
      // TODO proper error handling
    })
  }

  addMember() {
    var member = CFM.getElementByIDValue("email");

    this.serv.addGroupMember(member, this.id, () => {
      this.members.push(member);
      (<HTMLInputElement> document.getElementById('email')).value = ""
    }, (error: any) => {
      // TODO proper error handling
    })
  }
}
