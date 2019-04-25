import { Component, OnInit, ViewChild, AfterViewInit } from '@angular/core';
import { Expense } from '../../../persistence/Expense';
import { CFM, KeyEvents, KeyEventKeys } from '../../../common/common-functions.module';
import { ExpensesService } from './expenses.service';
import { CategoryTypeMapper } from '../CategoryTypeMapper';
import { Filter, FilterType } from '../../../common/Filter';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterExpensesComponent implements OnInit, AfterViewInit {
  // Prefill/default values
  prefilled: Expense
  knownTypes: Set<string> = new Set() // For creating a new entry
  knownTypesFilter: Set<string> = new Set() // For filtering the table with types that match

  // Filters
  start;
  end;

  // Data & derived values
  data: Expense[]
  filteredData: Expense[]
  public categoryTypeMapper = new CategoryTypeMapper(undefined);

  // Reference HTML when making new category/type
  public newContent: {[string:string]:boolean} = {};

  constructor(private serv: ExpensesService, private tool: CFM) { }

  ngOnInit() {
    this.prefilled = new Expense();
    this.prefilled.date = new Date();
    this.end = new Date();
    this.start = CFM.getDate(0, -1);

    this.serv.obtainTransactions(this.tool.getActivatedGroup(), (entries: Expense[]) => {
      this.data = entries;
      this.refreshCategoryTypes()
      this.knownTypesFilter = this.categoryTypeMapper.types
      this.applyFilters()
    }, (error: any) => {
      // TODO proper error handling
    });
  }

  ngAfterViewInit() {
    CFM.linkKeyEventOnInputToAction("comments", KeyEvents.keyUp, KeyEventKeys.enter, () => {
      this.submit()
    });
  }

  refreshCategoryTypes() {
    var dict: {category: string, type: string}[] = []

    for(var i = 0; i < this.data.length; i++) {
      let value = this.data[i];
      dict.push({category: value.category, type: value.type})
    }

    this.categoryTypeMapper = new CategoryTypeMapper(dict)
    this.updateTypes()
  }
  
  updateTypes() {
    let category = CFM.getElementByIDValue("category");
    
    if((category != undefined && category != "")) {
      this.knownTypes = this.categoryTypeMapper.getTypesForCategory(category);
    } else {
      let selectedCategory = this.categoryTypeMapper.categories.values().next().value
      this.knownTypes = this.categoryTypeMapper.getTypesForCategory(selectedCategory)
      CFM.getElementByID("category").value = selectedCategory
    }
  }

  open(entry: Expense) {
    this.prefilled = entry;
    CFM.getElementByID("date").value = CFM.makeDateString(entry.date);
    CFM.getElementByID("amount").value = "" + entry.amount;
    CFM.getElementByID("category").value = entry.category;
    this.updateTypes() // Required in order to prefill the "type" field
    CFM.getElementByID("type").value = entry.type;
    CFM.getElementByID("comments").value = entry.comments;
  }

  delete(entry: Expense) {
    this.serv.deleteExpense(entry, () => {
      CFM.deleteFromArray(this.data, entry);
      CFM.deleteFromArray(this.filteredData, entry)
      this.refreshCategoryTypes();
    }, (error: any) => void {
      // TODO proper error handling
    });
  }

  applyFilters() {
    var filter = new Filter<Expense>()

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

    var type = CFM.getElementByIDValue("typeFilter")
    if(type != null && type != undefined && type != "") {
      filter.addCondition("type", type, FilterType.equals)
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

  applyCategoryFilter() {
    let category = CFM.getElementByIDValue("categoryFilter")
    this.knownTypesFilter = category == "" ? this.categoryTypeMapper.types : this.categoryTypeMapper.getTypesForCategory(category)

    this.applyFilters()
  }

  resetFilters() {
    this.filteredData = this.data
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
      this.prefilled = new Expense();
    }

    this.prefilled.amount = +CFM.getElementByIDValue("amount");
    this.prefilled.category = CFM.getElementByIDValue("category");
    this.prefilled.type = CFM.getElementByIDValue("type");
    this.prefilled.date = CFM.makeDate(CFM.getElementByIDValue("date"));
    this.prefilled.comments = CFM.getElementByIDValue("comments");
    this.prefilled.group = this.tool.getActivatedGroup();
    this.serv.postExpense(this.prefilled, (expense: Expense) => {
      if(this.prefilled.id == undefined) {
        this.prefilled.id = expense.id
        this.data.push(this.prefilled);
      }
      this.reset();
      this.refreshCategoryTypes();
      this.applyCategoryFilter()
    }, (error: any) => {
      // TODO proper error handling
    });

  }

  reset() {
    var date = this.prefilled.date
    this.prefilled = new Expense();
    this.prefilled.date = date;
    this.newContent = {};
    CFM.getElementByID("amount").value = "";
    CFM.getElementByID("comments").value = "";
    CFM.getElementByID("amount").focus();
  }
}
