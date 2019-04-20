import { Component, OnInit, Input, ViewChild } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { OverviewTableComponent } from './overview-table/overview-table.component';
import { Expense } from '../../persistence/Expense';
import { IncomeEntry } from '../../persistence/Income';
import { IncomeService } from '../register/register-income/income.service';
import { ExpensesService } from '../register/register-expenses/expenses.service';
import { CFM } from '../../common/common-functions.module';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-overview',
  templateUrl: './overview.component.html',
  styleUrls: ['./overview.component.css']
})
export class OverviewComponent implements OnInit {

  constructor(private incomeService: IncomeService, private expensesService: ExpensesService, private route: ActivatedRoute, private tool: CFM) { }

  start: string;
  end: string;
  incomeData: IncomeEntry[];
  expensesData: Expense[];
  income: number = 0;
  expenses: number = 0;

  @ViewChild("incomeTable") incomeTable: OverviewTableComponent
  @ViewChild("expensesTable") expensesTable: OverviewTableComponent

  private subscriptions: Subscription[] = []

  ngOnInit() {
    this.start = CFM.makeDateString(CFM.getDate(-7)); // Default start date
    this.end = CFM.makeDateString(CFM.getDate()); // Default end date = today

    this.subscriptions.push(this.route.params.subscribe(params => {
      var groupID = this.tool.getActivatedGroup();

      this.incomeService.obtainTransactions(groupID, (entries: IncomeEntry[]) => {
        this.incomeData = entries;
        this.income = 0;
        this.calculateBetweenDates(false);
      }, (error: any) => {
        // TODO proper error handling
      });

      this.expensesService.obtainTransactions(groupID, (entries: Expense[]) => {
        this.expensesData = entries;
        this.expenses = 0;
        this.calculateBetweenDates(false);
      }, (error: any) => {
        // TODO proper error handling
      });
    }))
  }

  ngOnDestroy() {
    this.subscriptions.forEach((value: Subscription) => {
      value.unsubscribe()
    })
  }

  calculateBetweenDates(update: boolean) {

    if(update) {
      this.start = CFM.makeDateString(CFM.makeDate(CFM.getElementByIDValue("start")));
      this.end = CFM.makeDateString(CFM.makeDate(CFM.getElementByIDValue("end")));
    }

    var start = new Date(this.start);
    var end = new Date(this.end);

    if(this.incomeData != null) {
      this.incomeTable.initCategories(start, end, this.incomeData);
    }
    if(this.expensesData != null) {
      this.expensesTable.initCategories(start, end, this.expensesData);
    }

    this.expenses = 0;
    this.income = 0;

    if(this.expensesData != null) {
      this.expensesData.forEach((entry) => {
        let expenseDate = new Date(entry['date']);
        if(expenseDate.valueOf() >= start.valueOf() && expenseDate.valueOf() <= end.valueOf()) {
          this.expenses += entry['amount'];
        }
      })
    }

    if(this.incomeData != null) {
      this.incomeData.forEach((entry) => {
        let incomeDate = new Date(entry['date']);
        if(incomeDate.valueOf() >= start.valueOf() && incomeDate.valueOf() <= end.valueOf()) {
          this.income += entry['amount'];
        }
      })
    }
  }
}
