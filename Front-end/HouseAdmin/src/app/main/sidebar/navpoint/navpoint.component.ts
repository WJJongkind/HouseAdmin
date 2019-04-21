import { Component, OnInit, Input } from '@angular/core';

/**
 * The 
 */
@Component({
  selector: 'app-navpoint',
  templateUrl: './navpoint.component.html',
  styleUrls: ['./navpoint.component.css']
})
export class NavpointComponent {

  @Input() name: String;

}
