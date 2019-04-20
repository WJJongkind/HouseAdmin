import { Component, OnInit } from '@angular/core';
import { GrouptableComponent } from './grouptable/grouptable.component';
import { CFM } from '../common/common-functions.module';
import { Router } from '@angular/router';


@Component({
  selector: 'app-groups',
  templateUrl: './groups.component.html',
  styleUrls: ['./groups.component.css']
})
export class GroupsComponent implements OnInit {

  constructor(private router: Router, private CFM: CFM) { 
  }

  ngOnInit() {
  }

  logout() {
    this.CFM.logout(this.router)
  }

}
