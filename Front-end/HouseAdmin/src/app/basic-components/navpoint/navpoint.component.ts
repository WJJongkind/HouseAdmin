import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-navpoint',
  templateUrl: './navpoint.component.html',
  styleUrls: ['./navpoint.component.css']
})
export class NavpointComponent implements OnInit {

  @Input() name: String;
  constructor() { }

  ngOnInit() {
  }

}
