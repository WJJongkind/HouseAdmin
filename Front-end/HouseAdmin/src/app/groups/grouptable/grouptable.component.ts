import { Component, OnInit } from '@angular/core';
import { GroupService } from '../group.service';
import { CFM } from '../../common/common-functions.module';
import { Group } from '../../persistence/Group';

@Component({
  selector: 'app-grouptable',
  templateUrl: './grouptable.component.html',
  styleUrls: ['./grouptable.component.css']
})
export class GrouptableComponent implements OnInit {
  public groups: Group[];

  constructor(private serv: GroupService, private tool: CFM) { }

  ngOnInit() {
    this.serv.getAllUserGroups((groups: Group[]) => {
      this.groups = groups;
    }, (error: any) => {
      // TODO proper error handling
    });
  }

  delete(id) {
    this.serv.deleteGroup(id, () => {
      window.location.reload();
    }, (error: any) => {
      // TODO proper error handling
    });
  }

  edit(id) {
    window.location.href = "../editgroup/" + id;
  }

  manage(id) {
    window.location.href = "../managegroup/" + id;
  }

  setActiveGroup(id) {
    this.tool.setActivatedGroup(id);
  }
}
