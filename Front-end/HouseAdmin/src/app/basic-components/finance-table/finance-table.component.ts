import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { CFM, SortMethod } from '../../common/common-functions.module';

@Component({
  selector: 'app-finance-table',
  templateUrl: './finance-table.component.html',
  styleUrls: ['./finance-table.component.css']
})

export class FinanceTableComponent {

  // MARK: - Inputs & outputs

  @Input() tableName;
  @Input() columns: any[];
  @Input() moneyColumn;
  @Input() set data(data: {}[]) {
    if(data != undefined) {
      this.sortedData = data;
      CFM.sortJSONArrayByProperty(this.sortedData, 'date', SortMethod.descending);
    }
  }
  @Output() onEntryClick = new EventEmitter<any>();
  @Output() deleteEntryClick = new EventEmitter<any>();

  // MARK: - Public properties

  sortedData: {}[] = [];

  // MARK: - Public methods

  calculateTotalMoneyAmount(): Number {
    var totalMoney = 0;
    this.sortedData.forEach((entry) => {
      totalMoney += entry['amount']
    });

    return totalMoney;
  }

  open(entry) {
    this.onEntryClick.emit(entry);
  }

  delete(entry) {
    this.deleteEntryClick.emit(entry);
  }

  makeDateString(date: Date): string {
    return CFM.makeDateString(date);
  }
}