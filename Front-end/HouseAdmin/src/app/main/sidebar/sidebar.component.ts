import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CFM } from '../../common/common-functions.module';

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.css']
})
export class SidebarComponent implements OnInit {

  constructor(private router: Router, private CFM: CFM) { }

  ngOnInit() {
  }

  logout() {
    this.CFM.logout(this.router);
  }
}
