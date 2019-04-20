import { Component, OnInit, ViewChild, AfterViewInit } from '@angular/core';
import { IncomeEntry } from '../../../persistence/Income';
import { CFM, KeyEvents, KeyEventKeys } from '../../../common/common-functions.module';
import { IncomeService } from './income.service';
import { FilterType, Filter } from '../../../common/Filter';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterIncomeComponent implements OnInit, AfterViewInit {
  // Prefill/default values
  prefilled: IncomeEntry
  categories: string[]

  // Filters
  start;
  end;

  // Data & derived values
  data: IncomeEntry[];
  filteredData: IncomeEntry[]

  // Reference HTML when making new category/type
  public newContent: {[string:string]:boolean} = {};

  // @ViewChild('overview') overviewTable;

  constructor(private serv: IncomeService, private tool: CFM) { }

  ngOnInit() {
    this.prefilled = new IncomeEntry();
    this.prefilled.date = new Date();
    this.end = new Date();
    this.start = CFM.getDate(0, -1);

    this.serv.obtainTransactions(this.tool.getActivatedGroup(), (data: IncomeEntry[]) => {
      this.data = data;
      this.refreshCategories();
      this.applyFilters()
    }, (error: any) => void {
      // TODO proper error handling
    });
  }

  ngAfterViewInit() {
    CFM.linkKeyEventOnInputToAction("comments", KeyEvents.keyUp, KeyEventKeys.enter, () => {
      this.submit()
    });
  } 
  
  refreshCategories() {
    this.categories = []

    for(var i = 0; i < this.data.length; i++) {
      let value = this.data[i];
      this.categories.push(value.category)
    }
  }

  applyFilters() {
    var filter = new Filter<IncomeEntry>()

    var startDate = CFM.getElementByIDValue("startDateFilter")
    if(startDate != null && startDate != undefined && startDate != "") {
      var value = CFM.makeDate(startDate)
      filter.addCondition("date", value, FilterType.equalOrMoreThan)
    }

    var endDate = CFM.getElementByIDValue("endDateFilter")
    if(endDate != null && endDate != undefined && endDate != "") {
      var value = CFM.makeDate(endDate)
      filter.addCondition("date", value, FilterType.equalOrLessThan)
    }

    var category = CFM.getElementByIDValue("categoryFilter")
    if(category != null && category != undefined && category != "") {
      filter.addCondition("category", category, FilterType.equals)
    }

    var min = CFM.getElementByIDValue("minAmountFilter")
    if(min != null && min != undefined && min != "") {
      filter.addCondition("amount", min, FilterType.equalOrMoreThan)
    }

    var max = CFM.getElementByIDValue("maxAmountFilter")
    if(max != null && max != undefined && max != "") {
      filter.addCondition("amount", max, FilterType.equalOrLessThan)
    }

    this.filteredData = filter.apply(this.data)
  }

  open(entry: IncomeEntry) {
    this.prefilled = entry;
    CFM.getElementByID("date").value = entry.date.toISOString();
    CFM.getElementByID("amount").value = "" + entry.amount;
    CFM.getElementByID("category").value = entry.category;
    CFM.getElementByID("comments").value = entry.comments;
  }

  delete(entry: IncomeEntry) {
    this.serv.deleteIncome(entry, () => {
      CFM.deleteFromArray(this.data, entry);
      this.applyFilters();
    }, (error: any) => {
      // TODO proper error handling
    });
  }

  newButtonPressed(origin: string) {
    this.newContent[origin] = true;
  }

  cancelButtonPressed(origin: string) {
    this.newContent[origin] = false;
    CFM.getElementByID(origin).value = "";
  }

  makeDateStringWrapper(date: Date): string {
    return CFM.makeDateString(date);
  }

  submit() {
    if(this.prefilled == undefined) {
      this.prefilled = new IncomeEntry();
    }
    this.prefilled.amount = +CFM.getElementByIDValue("amount");
    this.prefilled.category = CFM.getElementByIDValue("category");
    this.prefilled.date = CFM.makeDate(CFM.getElementByIDValue("date"));
    this.prefilled.comments = CFM.getElementByIDValue("comments");
    this.prefilled.group = this.tool.getActivatedGroup();

    this.serv.postIncome(this.prefilled, (response: IncomeEntry) => {
      if(this.prefilled.id == undefined) {
        this.prefilled.id = response.id
        this.data.push(this.prefilled);
        this.applyFilters()
      }
      this.reset();
    }, (error: any) => {
      // TODO proper error handling
    });

  }

  reset() {
    var date = this.prefilled.date
    this.prefilled = new IncomeEntry();
    this.prefilled.date = date;
    this.newContent = {};
    CFM.getElementByID("amount").value = "";
    CFM.getElementByID("comments").value = "";
    CFM.getElementByID("amount").focus();
  }
}
