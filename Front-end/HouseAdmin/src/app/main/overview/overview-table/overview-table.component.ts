import { Component, OnInit, Input } from '@angular/core';
import { CFM } from '../../../common/common-functions.module';

@Component({
  selector: 'app-overview-table',
  templateUrl: './overview-table.component.html',
  styleUrls: ['./overview-table.component.css']
})
export class OverviewTableComponent implements OnInit {
  money = 0;

  @Input() name;
  categories: {}[];

  constructor() {}

  ngOnInit() {
  }

  calcMoney(start, end, roughData) {
    this.money = 0;

    roughData.forEach((entry) => {
      var entryDate = new Date(entry['date']);
      if(entryDate.valueOf() >= start.valueOf() && entryDate.valueOf() <= end.valueOf()) {
        this.money += entry['amount'];
      }
    });
  }

  initCategories(start: Date, end: Date, roughData) {
    this.calcMoney(start, end, roughData);

    this.categories = [];
    if(roughData == undefined || roughData == null) {
      return;
    }

    roughData.forEach((entry) => {
      let foundCat = CFM.findKeyValueInArray(this.categories, 'category', entry['category']);// this.getCategory(entry, this.categories);

      var entryDate = new Date(entry['date']);
      if(entryDate.valueOf() >= start.valueOf() && entryDate.valueOf() <= end.valueOf()) {
        if(foundCat != null && foundCat != undefined) {
          foundCat['amount'] += entry['amount'];
          foundCat['percentage'] = foundCat['amount'] / this.money * 100;
        } else {
          this.categories.push({category: entry['category'], amount: entry['amount'], percentage: (entry['amount'] / this.money * 100)});
        }
      }
    });
  }
}
