import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CFM, SortMethod } from '../../common/common-functions.module';

/**
 * The FinanceTableComponent is used to display financial data in a properly formatted data. Numbers are automatically rounded to two digits behind the comma, and the total
 * sum of money that is displayed by the table is automatically calculated and displayed. It also offers functionality to notify observers when an entry has been clicked, or when
 * the user pressed the delete-button for an entry.
 */
@Component({
  selector: 'finance-table',
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